package dev.cherry.seacrhanim.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.repository.CitiesRepository
import javax.inject.Singleton

/**
 * @author DVLP_2
 * @since 13.02.2017.
 */
@Module
class AppModule(val mAppContext: Context) {

    @Provides
    @Singleton
    fun provideRestApi(): RestApi = RestApi(mAppContext)

    @Provides
    @Singleton
    fun provideCitiesRepository(): CitiesRepository = CitiesRepository()
}