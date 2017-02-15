package dev.cherry.seacrhanim

import android.app.Application
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
    }

    override fun onCreate() {
        super.onCreate()

        // create dependency graph
        graph = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }
}