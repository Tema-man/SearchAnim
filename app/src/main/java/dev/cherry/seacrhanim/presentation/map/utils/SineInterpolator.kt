package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.model.LatLng

/**
 * Interpolator class. Interpolate specified route as a curve 1 sine period long
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
class SineInterpolator(start: LatLng, end: LatLng) {

    /** Needs to project geo coordinates to a plane using Mercator projection */
    val WORLD_WIDTH = 100.0

    /** Data class to store plane coordinates */
    data class Point(val x: Double, val y: Double)

    // Start point of route
    private var startPoint: Point

    // End point of route
    private var endPoint: Point

    private var mGeoProjector = GeoProjector.Mercator(WORLD_WIDTH)

    // Route length
    var length = 0.0

    // Angle between route line and 0 x-axis
    var angle = 0.0

    // Sine wave amplitude
    var amplitude = 0.0

    /** Initialization */
    init {
        // Convert geo coordinates to plane
        startPoint = mGeoProjector.toPoint(start)
        endPoint = mGeoProjector.toPoint(end)

        // calculateY parameters
        length = length(startPoint, endPoint)
        angle = getAngle(startPoint, endPoint)
        amplitude = 15 * length / WORLD_WIDTH
    }

    /**
     * Translates point of the route to appropriate position
     *
     * @param start [Point] start of the route
     * @param point [point] point to translate
     * @param angle route to x-axis angle in radians
     * @return [Point] translated point
     */
    fun translate(start: Point, point: Point, angle: Double): Point {
        val dx = point.x - start.x
        val dy = point.y - start.y
        val alpha = getAngle(dx, dy) + angle

        val R = Math.sqrt((dx * dx + dy * dy))
        val newX = start.x + R * Math.cos(alpha)
        val newY = start.y + R * Math.sin(alpha)
        return Point(newX, newY)
    }

    /**
     * Computes angle between route line and x-axis
     *
     * @param start [Point] start point of route
     * @param stop [Point] end point of route
     * @return angle in radians
     */
    fun getAngle(start: Point, stop: Point): Double {
        val dx = stop.x - start.x
        val dy = stop.y - start.y
        return getAngle(dx, dy)
    }

    /**
     * Computes angle by coordinates delta
     *
     * @param dx x-coordinate increment
     * @param dy y-coordinate increment
     * @return angle in radians
     */
    fun getAngle(dx: Double, dy: Double): Double {
        if (dx == 0.0 || dy == 0.0) return 0.0
        return Math.atan(dy / dx) + if (dx < 0) Math.PI else 0.0
    }

    /**
     * Calculates length of route line
     *
     * @param start [Point] start point of route
     * @param end [Point] end point of route
     * @return length of route as vector length
     */
    fun length(start: Point, end: Point): Double {
        val a = end.x - start.x
        val b = end.y - start.y
        return Math.sqrt(a * a + b * b)
    }

    /**
     * Calculate an y-coordinate of curve
     *
     * @param x x-coordinate
     * @return calculated y-coordinate
     */
    fun calculateY(x: Double): Double {
        val degreesPerStep = 360.0 / length
        val degrees = 180 + x * degreesPerStep
        return amplitude * Math.sin(Math.toRadians(degrees))
    }

    fun interpolate(fraction: Double): LatLng {
        val point = Point(startPoint.x + fraction, startPoint.y + calculateY(fraction))
        val translated = translate(startPoint, point, angle)
        return mGeoProjector.toLatLng(translated)
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