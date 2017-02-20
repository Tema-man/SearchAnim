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
import dev.cherry.seacrhanim.presentation.map.utils.PlaneAnimator
import dev.cherry.seacrhanim.presentation.map.utils.SineInterpolator
import kotlinx.android.synthetic.main.activity_map.*


/**
 * @author Artemii Vishnevskii
 * @since 15.02.2017.
 */


class MapActivity : MvpAppCompatActivity(), MapView, OnMapReadyCallback {
    companion object {

        val SOURCE: String = "extra_source"
        val DESTINATION: String = "extra_destination"
    }

    val PLANE_SPEED = 2.0

    @InjectPresenter
    lateinit var mPresenter: MapPresenter

    lateinit var source: City
    lateinit var destination: City
    lateinit var mGoogleMap: GoogleMap
    private var isAnimating = false

    private lateinit var mPlaneAnimator: PlaneAnimator
    private lateinit var mSineInterpolator: SineInterpolator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        (map as SupportMapFragment).getMapAsync(this)

        source = intent.getParcelableExtra(SOURCE)
        destination = intent.getParcelableExtra(DESTINATION)
    }

    override fun onStart() {
        super.onStart()
        if (isAnimating) mPlaneAnimator.start()
    }

    override fun onStop() {
        super.onPause()
        mPlaneAnimator.stop()
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
        val srcMark = drawCityMarker(source)
        val dstMark = drawCityMarker(destination)
        fitCameraView(srcMark, dstMark)

        mSineInterpolator = SineInterpolator(srcMark.position, dstMark.position)
        mSineInterpolator.drawLine(mGoogleMap)

        runPlaneAnimation(srcMark.position)
    }

    fun fitCameraView(src: Marker, dst: Marker) {
        val builder = LatLngBounds.Builder()
        builder.include(src.position)
        builder.include(dst.position)
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
    }

    fun drawCityMarker(city: City): Marker {
        val options = MarkerOptions()
        options.position(LatLng(city.location.lat, city.location.lon))
        options.icon(BitmapDescriptorFactory.fromBitmap(makeCityIcon(city)))
        options.zIndex(1f)
        return mGoogleMap.addMarker(options)
    }

    fun makeCityIcon(city: City): Bitmap {
        val view = layoutInflater.inflate(R.layout.marker_city_name, null)
        val cityName = view.findViewById(R.id.cityName) as TextView
        cityName.text = if (!city.iata.isEmpty()) city.iata[0] else city.city

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()

        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun runPlaneAnimation(start: LatLng) {
        val plane = mGoogleMap.addMarker(MarkerOptions()
                .position(start)
                .anchor(0.4f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_plane))
                .zIndex(2f))
        mPlaneAnimator = PlaneAnimator(mSineInterpolator, PLANE_SPEED)
        mPlaneAnimator.animate(plane)
        mPlaneAnimator.start()
        isAnimating = true
    }
}