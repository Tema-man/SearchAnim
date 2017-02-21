package dev.cherry.seacrhanim.presentation.map

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import kotlinx.android.synthetic.main.activity_map.*


/**
 * Map screen. Displays plane animation between two points
 *
 * @author Artemii Vishnevskii
 * @since 15.02.2017.
 */
class MapActivity : MvpAppCompatActivity(), MapView, OnMapReadyCallback {

    // constants for Intents
    companion object {
        val SOURCE: String = "extra_source"
        val DESTINATION: String = "extra_destination"
    }

    /** Plane animation speed parameter */
    val ANIMATION_DURATION = 3000L

    /** Needs to project geo coordinates to a plane using Mercator projection */
    val WORLD_WIDTH = 100.0

    @InjectPresenter
    lateinit var presenter: MapPresenter

    // selected source point
    lateinit var source: City

    // selected destination point
    lateinit var destination: City

    // Google map
    lateinit var googleMap: GoogleMap

    // flag for restart animation after restore app from background
    private var isAnimating = false

    // animator for plane marker
    private lateinit var planeAnimator: PlaneAnimator

    // interpolator for geo points
    private lateinit var geoInterpolator: GeoInterpolator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // request far from SupportMapFragment
        (map as SupportMapFragment).getMapAsync(this)

        // get data from intent
        source = intent.getParcelableExtra(SOURCE)
        destination = intent.getParcelableExtra(DESTINATION)
    }

    override fun onStart() {
        super.onStart()
        // check if animation was start then resume it
        if (isAnimating) planeAnimator.start()
    }

    override fun onStop() {
        super.onPause()

        // stop animation to prevent leaks
        planeAnimator.stop()
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
        val srcMark = drawCityMarker(source)
        val dstMark = drawCityMarker(destination)

        // set map view to fit city markers
        fitCameraView(srcMark, dstMark)

        // create geoInterpolator
        geoInterpolator = GeoInterpolator.SineInterpolator(
                GeoProjector.Mercator(WORLD_WIDTH), srcMark.position, dstMark.position)

        // draw plane trajectory
        drawSineLine()

        // start animation
        runPlaneAnimation(srcMark.position)
    }

    /** Draw flight trajectory as dotted sine line. Line consists from map [Marker] */
    fun drawSineLine() {
        // number of points that will be drawn on map, depends on route length
        val numPoints = (Math.sqrt(2 * geoInterpolator.length) + 20)

        // fraction increment
        val step = 1.0 / numPoints

        // loop parameter
        var fraction = 0.0
        while (fraction <= 1.0) {
            // get interpolated position
            val newLatLng = geoInterpolator.interpolate(fraction)

            // create map dot marker
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_line_dot)
            googleMap.addMarker(MarkerOptions().icon(icon).anchor(0.5f, 0.5f)
                    .position(newLatLng).flat(true).zIndex(0f))

            // increment fraction
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
        return googleMap.addMarker(MarkerOptions()
                .position(LatLng(city.location.lat, city.location.lon))
                .icon(BitmapDescriptorFactory.fromBitmap(makeCityIcon(city)))
                .zIndex(1f))
    }

    /**
     * Convert [city] name view to bitmap for display on map
     *
     * @param city [City] to draw
     * @return [Bitmap] for display on map
     */
    fun makeCityIcon(city: City): Bitmap {
        // inflate view layout
        val view = layoutInflater.inflate(R.layout.marker_city_name, null)

        // find text view for city name
        val cityName = view.findViewById(R.id.cityName) as TextView

        // display city iata code if present, city name otherwise
        cityName.text = if (!city.iata.isEmpty()) city.iata[0] else city.city

        // count view parameters
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()

        // convert view to bitmap and return
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
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
                .zIndex(2f))

        // create plane animator and start animation
        planeAnimator = PlaneAnimator(geoInterpolator, ANIMATION_DURATION)
        planeAnimator.animate(plane)
        planeAnimator.start()

        // we are animating now
        isAnimating = true

        // assign camera move listener
        googleMap.setOnCameraMoveListener {
            // notify animator that camera bearing has changed
            planeAnimator.cameraBearing = googleMap.cameraPosition.bearing
        }
    }
}