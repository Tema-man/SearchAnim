package dev.cherry.seacrhanim.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Mvp View interface for main screen
 *
 * @author Artemii Vishnevskii
 * @since 14.02.2017.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {

    /**
     * Displays an error
     *
     * @param t [Throwable] error for display
     */
    fun showError(t: Throwable?)

    /** Navigates to map screen */
    fun navigateToMap()

    /** Display error message, that source point not selected */
    fun showSourceNotSelectedError()

    /** Display error message, that destination point not selected */
    fun showDestinationNotSelectedError()
}