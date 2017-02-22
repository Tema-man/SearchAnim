package dev.cherry.seacrhanim.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.cherry.seacrhanim.BuildConfig
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.repository.CitiesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * Dagger Module for app classes
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@Module
class AppModule(val appContext: Context) {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(interceptor)
        }

        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRestApi(): RestApi = RestApi(appContext)

    @Provides
    @Singleton
    fun provideCitiesRepository(restApi: RestApi): CitiesRepository = CitiesRepository(restApi)
}