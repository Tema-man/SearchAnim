package dev.cherry.seacrhanim.presentation.main

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dev.cherry.seacrhanim.App
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.repository.CitiesRepository
import java.util.*
import javax.inject.Inject

/**
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    val TAG: String = "MainPresenter"

    @Inject
    lateinit var mCityRepo: CitiesRepository

    var mSource: City? = null
    var mDestination: City? = null

    init {
        App.graph.inject(this)
    }

    fun getCities(name: String, lang: String): List<City> {
        try {
            return mCityRepo.getCities(name, lang)
        } catch (e: Exception) {
            viewState.showError(e)
            return Collections.emptyList()
        }
    }

    fun sourceSelected(source: City) {
        mSource = source
        Log.d(TAG, "source selected: $source")
    }

    fun destinationSelected(destination: City) {
        mDestination = destination
        Log.d(TAG, "destination selected: $destination")
    }

    fun fabClick() {
        if (mSource == null) {
            viewState.showSourceNotSelectedError()
            return
        }

        if (mDestination == null) {
            viewState.showDestinationNotSelectedError()
            return
        }

        viewState.navigateToMap(mSource as City, mDestination as City)
    }
}