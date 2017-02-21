package dev.cherry.seacrhanim.presentation.map.utils

import android.graphics.Point
import com.google.android.gms.maps.model.LatLng

/**
 * Class for interpolate geo position for animation. To use
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 *
 * @param start [LatLng] position of start point of the route
 * @param end [LatLng] position of end point of the route
 */
abstract class GeoInterpolator(projector: GeoProjector, start: LatLng, end: LatLng) {

    /** Route length. Can be used for calculate some length - dependent values */
    var length = 0.0
        get
        protected set

    protected val geoProjector = projector

    // Start point of route
    protected var startPoint: Point = geoProjector.toPoint(start)

    // End point of route
    protected var endPoint: Point = geoProjector.toPoint(end)


    /**
     * Interpolate position on route by specified fraction
     *
     * @param fraction fraction of animation
     * @return [LatLng] interpolated LatLng
     */
    abstract fun interpolate(fraction: Double): LatLng

    /** Data class to store plane coordinates */
    data class Point(val x: Double, val y: Double)

    /** GeoInterpolator implementation. Interpolate specified route as a curve 1 sine period long */
    class SineInterpolator(projector: GeoProjector, start: LatLng, end: LatLng)
        : GeoInterpolator(projector, start, end) {

        // Angle between route line and 0 x-axis
        private var angle = 0.0

        // Sine wave amplitude
        private var amplitude = 0.0

        /** Initialization */
        init {
            // calculate parameters
            length = length(startPoint, endPoint)
            angle = getAngle(startPoint, endPoint)
            amplitude = length / 7
        }

        /**
         * Translates point of the route to appropriate position
         *
         * @param start [Point] start of the route
         * @param point [point] point to translate
         * @param angle route to x-axis angle in radians
         * @return [Point] translated point
         */
        private fun translate(start: Point, point: Point, angle: Double): Point {
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
        private fun getAngle(start: Point, stop: Point): Double {
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
        private fun getAngle(dx: Double, dy: Double): Double {
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
        private fun length(start: Point, end: Point): Double {
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
        private fun calculateY(x: Double): Double {
            val degreesPerStep = 360.0 / length
            val degrees = 180 + x * degreesPerStep
            return amplitude * Math.sin(Math.toRadians(degrees))
        }

        override fun interpolate(fraction: Double): LatLng {
            val step = fraction * length
            val point = Point(startPoint.x + step, startPoint.y + calculateY(step))
            val translated = translate(startPoint, point, angle)
            return geoProjector.toLatLng(translated)
        }
    }
}