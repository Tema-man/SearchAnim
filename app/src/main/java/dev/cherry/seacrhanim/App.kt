package dev.cherry.seacrhanim

import android.app.Application
import android.os.Build
import dev.cherry.seacrhanim.di.AppComponent
import dev.cherry.seacrhanim.di.AppModule
import dev.cherry.seacrhanim.di.DaggerAppComponent

/**
 * Custom Android Application implementation. Holds Dagger dependencies graph.
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
class App : Application() {

    companion object {
        // dagger graph object
        lateinit var graph: AppComponent
        lateinit var locale: String
    }

    override fun onCreate() {
        super.onCreate()

        // create dependency graph
        graph = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()

        // get device primary locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = resources.configuration.locales.getFirstMatch(resources.assets.locales).country
        } else {
            @Suppress("DEPRECATION")
            locale = resources.configuration.locale.country
        }
    }
}