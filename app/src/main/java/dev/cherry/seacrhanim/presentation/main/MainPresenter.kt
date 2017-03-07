package dev.cherry.seacrhanim.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dev.cherry.seacrhanim.App
import dev.cherry.seacrhanim.entity.CitiesBean
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.repository.CitiesRepository
import javax.inject.Inject

/**
 * Presenter for main screen
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    // repository to ask for data
    lateinit var citiesRepository: CitiesRepository

    // system locate
    private val locale: String = App.locale

    init {
        App.graph.inject(this)
    }

    /**
     * Requests cities list from repository by specified name
     *
     * @param name city name for request
     * @return [List] of filtered [City]
     */
    fun getCities(name: String): CitiesBean {
        try {
            return citiesRepository.getCities(name, locale)
        } catch (e: Exception) {
            viewState.showError(e)
            return CitiesBean()
        }
    }

    /**
     * Save selected source point
     *
     * @param source [City] that selected as source
     */
    fun sourceSelected(source: City?) {
        citiesRepository.source = source
    }

    /**
     * Save selected destination point
     *
     * @param destination [City] that selected as source
     */
    fun destinationSelected(destination: City?) {
        citiesRepository.destination = destination
    }

    /** Handles search button click */
    fun searchClick() {
        // check source selected, if not show notification
        if (citiesRepository.source == null) {
            viewState.showSourceNotSelectedError()
            return
        }

        // check destination selected, if not show notification
        if (citiesRepository.destination == null) {
            viewState.showDestinationNotSelectedError()
            return
        }

        // ask view to navigate to map screen with selected points
        viewState.navigateToMap()
    }
}