package dev.cherry.seacrhanim.presentation.map

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter

/**
 *
 * Mvp Presenter for MapView
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
@InjectViewState
class MapPresenter : MvpPresenter<MapView>() {

    private var animationTime = 0L

    /** Calls when map is ready */
    fun mapReady() {
        viewState.startPlaneAnimation(animationTime)
    }

    fun animationPaused(time: Long) {
        animationTime = time
    }
}