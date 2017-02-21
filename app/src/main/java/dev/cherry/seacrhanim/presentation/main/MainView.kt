package dev.cherry.seacrhanim.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import dev.cherry.seacrhanim.entity.City

/**
 * Mvp View interface for main screen
 *
 * @author Artemii Vishnevskii
 * @since 14.02.2017.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView : MvpView {

    /**
     * Displays an error
     *
     * @param t [Throwable] error for display
     */
    fun showError(t: Throwable?)

    /**
     * Navigates to map screen. And pass to map position data
     *
     * @param source [City] source point
     * @param destination [City] destination point
     */
    fun navigateToMap(source: City, destination: City)

    /** Display error message, that source point not selected */
    fun showSourceNotSelectedError()

    /** Display error message, that destination point not selected */
    fun showDestinationNotSelectedError()
}