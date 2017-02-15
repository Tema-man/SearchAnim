package dev.cherry.seacrhanim.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import dev.cherry.seacrhanim.entity.City

/**
 * @author DVLP_2
 * @since 14.02.2017.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {
    fun showError(t: Throwable?)
    fun navigateToMap(mSource: City, mDestination: City)
}