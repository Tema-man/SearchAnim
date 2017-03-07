package dev.cherry.seacrhanim.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dev.cherry.seacrhanim.BuildConfig
import dev.cherry.seacrhanim.net.CitiesService
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.repository.CitiesRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
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
    fun provideRestApi(): RestApi = RestApi(appContext)

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl("https://yasen.hotellook.com/")
                .addConverterFactory(JacksonConverterFactory.create())

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.client(OkHttpClient.Builder().addInterceptor(interceptor).build())
        }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideCitiesService(retrofit: Retrofit):
            CitiesService = retrofit.create(CitiesService::class.java)

    @Provides
    @Singleton
    fun provideCitiesRepository(restApi: RestApi, citiesService: CitiesService):
            CitiesRepository = CitiesRepository(restApi, citiesService)
}