package dev.cherry.seacrhanim.presentation.map

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import dev.cherry.seacrhanim.App
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.repository.CitiesRepository
import javax.inject.Inject

/**
 *
 * Mvp Presenter for MapView
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
@InjectViewState
class MapPresenter : MvpPresenter<MapView>() {

    @Inject
    lateinit var citiesRepository: CitiesRepository

    init {
        App.graph.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewState.stopPlaneAnimation()
    }

    /** Calls when map is ready */
    fun mapReady() {
        val src: City = citiesRepository.source ?: City()
        val dst: City = citiesRepository.destination ?: City()

        viewState.drawCitiesAndTrajectory(src, dst)
        viewState.startPlaneAnimation()
    }
}