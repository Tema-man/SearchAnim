package dev.cherry.seacrhanim.presentation.map.utils

import android.animation.ValueAnimator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

/**
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 * @since 19.02.2017
 */
class PlaneAnimator(
        val points: MutableList<LatLng>,
        val interpolator: LatLngInterpolator,
        val duration: Long) {

    val mAnimator = ValueAnimator()
    lateinit var currentPoint: LatLng
    lateinit var nextPoint: LatLng

    fun animate(marker: Marker) {
        mAnimator.addUpdateListener { animation ->
            val v = animation.animatedFraction / points.size
            val newPosition = interpolator.interpolate(v, currentPoint, nextPoint)
            marker.position = newPosition
        }
        mAnimator.setFloatValues(0f, 1f)
        mAnimator.duration = duration
    }

    fun start() {
        mAnimator.start()
    }

    fun stop() {
        mAnimator.cancel()
    }
}