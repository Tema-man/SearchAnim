package dev.cherry.seacrhanim.presentation.map.utils

import android.animation.ValueAnimator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

/**
 * Animator for plane marker on map
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 *
 * @param interpolator [GeoInterpolator] instance to calculate new geo position
 * @param duration animation duration
 */
class PlaneAnimator(val interpolator: GeoInterpolator, private val duration: Long) {

    /** Android [ValueAnimator] instance. Used to animate parameters in main thread */
    private val animator = ValueAnimator()

    /**
     * Setup [animator]
     *
     * @param marker [Marker] to animate
     */
    fun animate(marker: Marker) {
        animator.addUpdateListener { animation ->
            // get animation fraction
            val fraction = animation.animatedFraction.toDouble()

            // save last position to calculate bearing
            val lastPosition = marker.position

            // get interpolated position
            val newPosition = interpolator.interpolate(fraction)

            // calculate marker bearing
            val bearing = calculateBearing(lastPosition, newPosition)

            // assign parameters to marker
            marker.position = newPosition
            marker.rotation = bearing
        }
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.duration = duration

        // animate fracture from 0 to 1
        animator.setFloatValues(0f, 1f)
    }

    /**
     * Calculates bearing in degrees from [start] to [end] position
     *
     * @param start [LatLng] start position
     * @param end [LatLng] end position
     * @return calculated bearing in degrees
     */
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

    /** Starts an animation */
    fun start() {
        animator.start()
    }

    /** Cancel an animation */
    fun stop() {
        animator.cancel()
    }
}