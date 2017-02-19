package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.cherry.seacrhanim.R
import java.util.*

/**
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
class SineLine(val mWorldWidth: Double) {

    data class Point(val x: Double, val y: Double)

    fun translate(start: Point, p1: Point, angle: Double): Point {
        val dx = p1.x - start.x
        val dy = p1.y - start.y
        val R = Math.sqrt((dx * dx + dy * dy))

        val lambda = Math.atan((dy / dx))
        val theta = Math.PI / 180f * angle

        val alpha = theta + lambda + if (dx < 0) Math.PI else 0.0
        val newX = start.x + R * Math.cos(alpha)
        val newY = start.y + R * Math.sin(alpha)
        return Point(newX, newY)
    }

    fun getAngle(start: Point, stop: Point): Double {
        val dx = stop.x - start.x
        val dy = stop.y - start.y
        val slope = dy / dx.toFloat()
        var angle = Math.atan(slope)
        if (dx < 0) angle += Math.PI
        angle = Math.toDegrees(angle)
        return angle
    }

    fun length(start: Point, end: Point): Double {
        val a = end.x - start.x
        val b = end.y - start.y
        return Math.sqrt(a * a + b * b)
    }

    fun getY(x: Double, wavelength: Double, amplitude: Double): Double {
        val degreesPerStep = 360.0 / wavelength
        val degrees = 180 + x * degreesPerStep
        return amplitude * Math.sin(Math.toRadians(degrees))
    }

    fun toPoint(latLng: LatLng, worldWidth: Double): Point {
        val x = latLng.longitude / 360.0 + 0.5
        val siny = Math.sin(Math.toRadians(latLng.latitude))
        val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / -6.283185307179586 + 0.5
        return Point(x * worldWidth, y * worldWidth)
    }

    fun toLatLng(point: Point, worldWidth: Double): LatLng {
        val x = point.x / worldWidth - 0.5
        val lng = x * 360.0
        val y = 0.5 - point.y / worldWidth
        val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * 3.141592653589793)) * 2.0)
        return LatLng(lat, lng)
    }

    fun drawLine(start: LatLng, end: LatLng, googleMap: GoogleMap): MutableList<LatLng> {
        val startPoint = toPoint(start, mWorldWidth)
        val endPoint = toPoint(end, mWorldWidth)

        val angle = getAngle(startPoint, endPoint)
        val length = length(startPoint, endPoint)

        val step = length / (Math.sqrt(2 * length) + 7)
        val amplitude = 15 * length / mWorldWidth

        var i = step
        val pointsList = ArrayList<LatLng>()
        while (i <= length) {
            val p1 = Point(startPoint.x + i, startPoint.y + getY(i, length, amplitude))
            val p3 = translate(startPoint, p1, angle)

            val newLatLng = toLatLng(p3, mWorldWidth)
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_line_dot)
            googleMap.addMarker(MarkerOptions().icon(icon).position(newLatLng).flat(true))
            pointsList.add(newLatLng)

            i += step
        }
        return pointsList
    }
}