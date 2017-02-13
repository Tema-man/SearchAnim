package dev.cherry.seacrhanim.repository

import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.utils.JsonParser
import io.reactivex.Observable
import okhttp3.Request

/**
 * @author DVLP_2
 * *
 * @since 13.02.2017.
 */

class CitiesRepository(private val mRestApi: RestApi) {

    fun getCities(name: String, lang: String): Observable<List<City>> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(requestCities(name, lang))
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    private fun requestCities(name: String, lang: String): List<City> {
        val request = Request.Builder()
                .url("https://yasen.hotellook.com/autocomplete?$name=mow&lang=$lang")
                .get().build()

        return JsonParser.arrayList(mRestApi.makeRequest(request), City::class.java)
    }
}
