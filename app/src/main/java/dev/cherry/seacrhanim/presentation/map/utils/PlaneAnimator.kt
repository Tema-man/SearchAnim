package dev.cherry.seacrhanim.presentation.map.utils

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.model.Marker

/**
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 * @since 19.02.2017
 */
class PlaneAnimator(val mSineInterpolator: SineInterpolator, planeSpeed: Double) {

    private val mAnimator = ValueAnimator()
    private var duration: Long = Math.round(mSineInterpolator.length / planeSpeed * 1000)

    fun animate(marker: Marker) {
        mAnimator.addUpdateListener { animation ->
            val currentShift = animation.animatedFraction * mSineInterpolator.length
            val currentPoint = marker.position

            val newPosition = mSineInterpolator.interpolate(currentShift)
            marker.position = newPosition
            marker.rotation = mSineInterpolator.calculateBearing(currentPoint, newPosition)
        }
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.repeatMode = ValueAnimator.RESTART
        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.duration = duration
        mAnimator.startDelay = 500
        mAnimator.setFloatValues(0f, 1f)
    }

    fun start() {
        mAnimator.start()
    }

    fun stop() {
        mAnimator.cancel()
    }
}