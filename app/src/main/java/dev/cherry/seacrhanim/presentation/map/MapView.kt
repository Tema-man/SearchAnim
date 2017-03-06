package dev.cherry.seacrhanim.presentation.map

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * MapView intrface
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MapView : MvpView {

    /** Starts plane animation */
    fun startPlaneAnimation(animationTime: Long)

    fun stopPlaneAnimation()
}