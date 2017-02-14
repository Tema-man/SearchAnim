package dev.cherry.seacrhanim.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dev.cherry.seacrhanim.App
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.repository.CitiesRepository
import javax.inject.Inject

/**
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var mCityRepo: CitiesRepository

    init {
        App.graph.inject(this)
    }

    fun getCities(name: String, lang: String): List<City> {
        return mCityRepo.getCities(name, lang)
    }
}