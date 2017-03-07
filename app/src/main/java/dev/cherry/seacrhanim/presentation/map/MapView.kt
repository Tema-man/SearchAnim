package dev.cherry.seacrhanim.presentation.map

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import dev.cherry.seacrhanim.entity.City

/**
 * MapView intrface
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MapView : MvpView {

    /**
     * Draws cities markers and trajectory between them
     *
     * @param src [City] source city
     * @param dst [City] destination city
     */
    fun drawCitiesAndTrajectory(src: City, dst: City)

    /** Starts plane animation */
    fun startPlaneAnimation()

    /** Stops plane animation */
    fun stopPlaneAnimation()
}