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

    private val SAVED_ANIMATION_TIME = "saved_animation_time"

    /** Plane animation speed parameter */
    private val ANIMATION_DURATION = 3000L

    @InjectPresenter
    lateinit var presenter: MapPresenter

    private lateinit var source: Marker
    private lateinit var destination: Marker
    private lateinit var googleMap: GoogleMap

    // animator for plane marker
    private lateinit var planeAnimator: PlaneAnimator

    // interpolator for geo points
    private lateinit var geoInterpolator: GeoInterpolator

    // used for save and restore animation progress
    private var isAnimating = false
    private var savedAnimationTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        (mapFragment as SupportMapFragment).getMapAsync(this)
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
        stopPlaneAnimation()
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
        googleMap = map
        presenter.mapReady()
    }

    override fun drawCitiesAndTrajectory(src: City, dst: City) {
        source = drawCityMarker(src)
        destination = drawCityMarker(dst)

        fitCameraView(source, destination)

        geoInterpolator = GeoInterpolator.SineInterpolator(
                GeoProjector.Mercator(), source.position, destination.position)

        drawSineLine()
    }

    /** Creates plane marker and starts animation */
    override fun startPlaneAnimation() {
        // create and add plane marker
        val plane = googleMap.addMarker(MarkerOptions()
                .position(source.position)
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

    override fun stopPlaneAnimation() {
        savedAnimationTime = planeAnimator.getCurrentPlayTime()
        planeAnimator.stop()
    }

    /** Draw flight trajectory as dotted sine line. Line consists from map [Marker] */
    private fun drawSineLine() {
        // number of points that will be drawn on map, depends on route length
        val numPoints = (Math.sqrt(2 * geoInterpolator.length) + 20)

        val step = 1.0 / numPoints
        var fraction = 0.0
        while (fraction <= 1.0) {
            val newLatLng = geoInterpolator.interpolate(fraction)

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
    private fun fitCameraView(src: Marker, dst: Marker) {
        val builder = LatLngBounds.Builder()
        builder.include(src.position)
        builder.include(dst.position)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                resources.getDimensionPixelSize(R.dimen.map_padding)))
    }

    /**
     * Draw a city marker on map
     *
     * @param city [City] to draw
     * @return [Marker] created marker
     */
    private fun drawCityMarker(city: City): Marker {
        val cityName = MarkerTextView(this)

        // display city iata code if present, city name otherwise
        cityName.text = if (!city.iata.isEmpty()) city.iata[0] else city.city

        return googleMap.addMarker(MarkerOptions()
                .position(LatLng(city.location.lat, city.location.lon))
                .icon(cityName.asBitmapDescriptor())
                .zIndex(1f))
    }
}