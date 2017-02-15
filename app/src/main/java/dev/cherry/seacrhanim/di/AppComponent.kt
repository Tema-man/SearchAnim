package dev.cherry.seacrhanim.di

import dagger.Component
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.presentation.main.MainPresenter
import dev.cherry.seacrhanim.repository.CitiesRepository
import javax.inject.Singleton

/**
 * Dagger Component interface
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun inject(restApi: RestApi)

    fun inject(citiesRepository: CitiesRepository)

    fun inject(mainPresenter: MainPresenter)
}