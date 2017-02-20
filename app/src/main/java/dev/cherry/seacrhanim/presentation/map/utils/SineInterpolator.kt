package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dev.cherry.seacrhanim.R
import java.util.*

/**
 * Interpolator class. Interpolate specified route as a curve 1 sine period long
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
class SineInterpolator(start: LatLng, end: LatLng) {

    /** Needs to project geo coordinates to a plane using Mercator projection */
    val WORLD_WIDTH = 90.0

    /** Data class to store plane coordinates */
    data class Point(val x: Double, val y: Double)

    // Start point of route
    private var startPoint: Point

    // End point of route
    private var endPoint: Point

    // Route length
    var length = 0.0

    // Angle between route line and 0 x-axis
    var angle = 0.0

    // Sine wave amplitude
    var amplitude = 0.0

    init {
        // Convert geo coordinates to planeS
        startPoint = toPoint(start)
        endPoint = toPoint(end)

        // calculate parameters
        length = length(startPoint, endPoint)
        angle = getAngle(startPoint, endPoint)
        amplitude = 15 * length / WORLD_WIDTH
    }

    /**
     * Translates point of the route to appropriate position
     *
     * @param start [Point] start of the route
     * @param point [point] point to translate
     * @param angle route to x-axis angle
     */
    fun translate(start: Point, point: Point, angle: Double): Point {
        val dx = point.x - start.x
        val dy = point.y - start.y
        if (dx == 0.0 || dy == 0.0) return point
        val R = Math.sqrt((dx * dx + dy * dy))

        val lambda = Math.atan((dy / dx))
        val theta = Math.PI / 180f * angle

        val alpha = theta + lambda + if (dx < 0) Math.PI else 0.0
        val newX = start.x + R * Math.cos(alpha)
        val newY = start.y + R * Math.sin(alpha)
        return Point(newX, newY)
    }

    /**
     * Computes route to x-axis angle
     */
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

    fun interpolateY(x: Double): Double {
        val degreesPerStep = 360.0 / length
        val degrees = 180 + x * degreesPerStep
        return amplitude * Math.sin(Math.toRadians(degrees))
    }

    fun toPoint(latLng: LatLng): Point {
        val x = latLng.longitude / 360.0 + 0.5
        val siny = Math.sin(Math.toRadians(latLng.latitude))
        val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / -6.283185307179586 + 0.5
        return Point(x * WORLD_WIDTH, y * WORLD_WIDTH)
    }

    fun toLatLng(point: Point): LatLng {
        val x = point.x / WORLD_WIDTH - 0.5
        val lng = x * 360.0
        val y = 0.5 - point.y / WORLD_WIDTH
        val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * 3.141592653589793)) * 2.0)
        return LatLng(lat, lng)
    }

    fun drawLine(googleMap: GoogleMap): List<LatLng> {
        var position = 0.0
        val pointsList = ArrayList<LatLng>()
        val step = length / (Math.sqrt(2 * length) + 12)
        while (position <= length) {
            val translated = countPoint(startPoint, position)
            val newLatLng = toLatLng(translated)
            val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_line_dot)
            googleMap.addMarker(MarkerOptions().icon(icon).position(newLatLng).flat(true).zIndex(0f))
            pointsList.add(newLatLng)
            position += step
        }
        return pointsList
    }

    fun countPoint(start: Point, shift: Double): Point {
        val point = Point(start.x + shift, start.y + interpolateY(shift))
        return translate(start, point, angle)
    }

    fun interpolateLatLng(shift: Double): LatLng {
        return toLatLng(countPoint(startPoint, shift))
    }

    fun calculateBearing(start: LatLng, end: LatLng): Float {
        val startLon = start.longitude
        val startLat = Math.toRadians(start.latitude)

        val endLon = end.longitude
        val endLat = Math.toRadians(end.latitude)
        val lonDiff = Math.toRadians(endLon - startLon)

        val y = Math.sin(lonDiff) * Math.cos(endLat)
        val x = Math.cos(startLat) * Math.sin(endLat) - Math.sin(startLat) * Math.cos(endLat) * Math.cos(lonDiff)

        return Math.toDegrees(Math.atan2(y, x)).toFloat()
    }
}