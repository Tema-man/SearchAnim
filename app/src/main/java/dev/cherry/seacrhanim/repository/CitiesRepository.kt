package dev.cherry.seacrhanim.repository

import android.accounts.NetworkErrorException
import dev.cherry.seacrhanim.App
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.utils.JsonParser
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author DVLP_2
 * *
 * @since 13.02.2017.
 */
class CitiesRepository {

    @Inject
    lateinit var mRestApi: RestApi

    init {
        App.graph.inject(this)
    }

    fun getCities(name: String, lang: String): List<City> {
        try {
            return requestCities(name, lang)
        } catch (e: Exception) {
            throw NetworkErrorException(e)
        }
    }

    private fun requestCities(name: String, lang: String): List<City> {
        val request = Request.Builder()
                .url("https://yasen.hotellook.com/autocomplete?term=$name&lang=$lang")
                .get().build()

        val response: String = mRestApi.makeRequest(request)
        val obj: JSONObject = JSONObject(response)
        val citiesStr = obj.getJSONArray("cities").toString()

        return JsonParser.arrayList(citiesStr, City::class.java)
    }
}
