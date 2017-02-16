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

    @InjectPresenter
    lateinit var mPresenter: MapPresenter

    lateinit var source: City
    lateinit var destination: City
    lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        (map as SupportMapFragment).getMapAsync(this)

        source = intent.getParcelableExtra(SOURCE)
        destination = intent.getParcelableExtra(DESTINATION)
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
        val srcMark = drawCityMarker(source)
        val dstMark = drawCityMarker(destination)
        val builder = LatLngBounds.Builder()
        builder.include(srcMark.position)
        builder.include(dstMark.position)

        createDashedLine(srcMark.position, dstMark.position)

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200))
    }

    fun drawCityMarker(city: City): Marker {
        val options = MarkerOptions()
        options.position(LatLng(city.location.lat, city.location.lon))
        options.icon(BitmapDescriptorFactory.fromBitmap(makeCityIcon(city)))


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

    fun createDashedLine(start: LatLng, end: LatLng) {
//        var x = latLng.longitude / 360 + .5
//        val siny = Math.sin(Math.toRadians(latLng.latitude))
//        var y = 0.5 * Math.log((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5

        var nextPoint = start
        val numPoints = 20

        val difLat = end.latitude - start.latitude
        val difLon = end.longitude - start.longitude

        val latStep = difLat / numPoints
        val lonStep = difLon / numPoints

        val length = 2 * Math.PI
        val step = length / numPoints

        var angle = 0.0
        while (angle < length) {

            mGoogleMap.addMarker(MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_line_dot))
                    .position(nextPoint)
                    .flat(true))

            val nextLat = nextPoint.latitude * Math.sin(angle)
            val nextLon = nextPoint.longitude + lonStep

            nextPoint = LatLng(nextLat, nextLon)
            angle += step
        }
    }
}