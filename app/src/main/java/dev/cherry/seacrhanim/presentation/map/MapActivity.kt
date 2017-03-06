package dev.cherry.seacrhanim.presentation.map

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dev.cherry.seacrhanim.R
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.presentation.map.utils.GeoInterpolator
import dev.cherry.seacrhanim.presentation.map.utils.GeoProjector
import dev.cherry.seacrhanim.presentation.map.utils.PlaneAnimator
import dev.cherry.seacrhanim.presentation.view.MarkerTextView
import kotlinx.android.synthetic.main.activity_map.*


/**
 * Map screen. Displays plane animation between two points
 *
 * @author Artemii Vishnevskii
 * @since 15.02.2017.
 */
class MapActivity : MvpAppCompatActivity(), MapView, OnMapReadyCallback {

    // constant names for Intents
    companion object {
        val SOURCE: String = "extra_source"
        val DESTINATION: String = "extra_destination"
    }

    /** */
    private val SAVED_ANIMATION_TIME: String = "saved_animation_time"

    /** Plane animation speed parameter */
    private val ANIMATION_DURATION = 3000L

    @InjectPresenter
    lateinit var presenter: MapPresenter

    // selected source point
    private lateinit var source: City

    // selected destination point
    private lateinit var destination: City

    // Google map
    private lateinit var googleMap: GoogleMap

    // animator for plane marker
    private lateinit var planeAnimator: PlaneAnimator

    // interpolator for geo points
    private lateinit var geoInterpolator: GeoInterpolator

    private var isAnimating = false
    private var savedAnimationTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // request far from SupportMapFragment
        (mapFragment as SupportMapFragment).getMapAsync(this)

        // get data from intent
        source = intent.getParcelableExtra(SOURCE)
        destination = intent.getParcelableExtra(DESTINATION)
    }

    override fun onResume() {
        super.onResume()

        // resume animation
        if (isAnimating) {
            planeAnimator.setCurrentPlayTime(savedAnimationTime)
            planeAnimator.start()
        }
    }

    override fun onPause() {
        super.onPause()

        // stop animation to prevent leaks and save its state
        savedAnimationTime = planeAnimator.getCurrentPlayTime()
        planeAnimator.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        planeAnimator.stop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putLong(SAVED_ANIMATION_TIME, savedAnimationTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedAnimationTime = savedInstanceState?.getLong(SAVED_ANIMATION_TIME) ?: 0L
    }

    /**
     * Map ready callback implementation. Provides Google map
     *
     * @see [OnMapReadyCallback.onMapReady]
     */
    override fun onMapReady(map: GoogleMap) {
        // save map
        googleMap = map

        // draw city markers
        val srcMarker = drawCityMarker(source)
        val dstMarker = drawCityMarker(destination)

        // set map view to fit city markers
        fitCameraView(srcMarker, dstMarker)

        // create geoInterpolator
        geoInterpolator = GeoInterpolator.SineInterpolator(
                GeoProjector.Mercator(), srcMarker.position, dstMarker.position)

        // draw plane trajectory
        drawSineLine()

        // start animation
        runPlaneAnimation(srcMarker.position)
    }

    /** Draw flight trajectory as dotted sine line. Line consists from map [Marker] */
    fun drawSineLine() {
        // number of points that will be drawn on map, depends on route length
        val numPoints = (Math.sqrt(2 * geoInterpolator.length) + 20)

        val step = 1.0 / numPoints
        var fraction = 0.0
        while (fraction <= 1.0) {
            // get interpolated position
            val newLatLng = geoInterpolator.interpolate(fraction)

            // create map dot marker
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_line_dot)
            googleMap.addMarker(MarkerOptions()
                    .icon(icon)
                    .anchor(0.5f, 0.5f)
                    .position(newLatLng)
                    .flat(true)
                    .zIndex(0f))

            fraction += step
        }
    }

    /**
     * Set camera view to fit city markers if possible
     *
     * @param src [Marker] source marker
     * @param dst [Marker] destination marker
     */
    fun fitCameraView(src: Marker, dst: Marker) {
        // compute LatLngBounds
        val builder = LatLngBounds.Builder()
        builder.include(src.position)
        builder.include(dst.position)

        // set map camera view
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
    }

    /**
     * Draw a city marker on map
     *
     * @param city [City] to draw
     * @return [Marker] created marker
     */
    fun drawCityMarker(city: City): Marker {
        val cityName = MarkerTextView(this)

        // display city iata code if present, city name otherwise
        cityName.text = if (!city.iata.isEmpty()) city.iata[0] else city.city

        return googleMap.addMarker(MarkerOptions()
                .position(LatLng(city.location.lat, city.location.lon))
                .icon(cityName.asBitmapDescriptor())
                .zIndex(1f))
    }

    /**
     * Starts plane animation
     *
     * @param start [LatLng] start point of route
     */
    fun runPlaneAnimation(start: LatLng) {
        // create and add plane marker
        val plane = googleMap.addMarker(MarkerOptions()
                .position(start)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                .flat(true)
                .zIndex(2f))

        // create plane animator and start animation
        planeAnimator = PlaneAnimator(geoInterpolator, ANIMATION_DURATION)
        planeAnimator.animate(plane)
        planeAnimator.setCurrentPlayTime(savedAnimationTime)
        planeAnimator.start()

        // we are animating now
        isAnimating = true
    }
}