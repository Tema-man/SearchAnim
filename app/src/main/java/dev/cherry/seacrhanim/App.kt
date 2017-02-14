package dev.cherry.seacrhanim

import android.app.Application
import dev.cherry.seacrhanim.di.AppComponent
import dev.cherry.seacrhanim.di.AppModule
import dev.cherry.seacrhanim.di.DaggerAppComponent

/**
 * @author DVLP_2
 * @since 13.02.2017.
 */
class App : Application() {

    companion object {
        lateinit var graph: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        graph = DaggerAppComponent.builder().appModule(AppModule(applicationContext)).build()
    }
}