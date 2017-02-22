package dev.cherry.seacrhanim.repository

import android.accounts.NetworkErrorException
import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.net.RestApi
import dev.cherry.seacrhanim.utils.JsonParser
import okhttp3.Request
import org.json.JSONObject

/**
 * Repository class. Provides [City] data.
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
class CitiesRepository(val restApi: RestApi) {

    /**
     * Requests a cities list from server. Call is blocking, because of network request.
     *
     * @param name city name or iata code
     * @param lang language for result
     * @return [List] of [City]
     */
    fun getCities(name: String, lang: String): List<City> {
        try {
            // create get request
            val request = Request.Builder()
                    .url("https://yasen.hotellook.com/autocomplete?term=$name&lang=$lang")
                    .get().build()

            // receiving data
            val response = restApi.makeRequest(request)

            // find cities collection
            val obj = JSONObject(response)
            val citiesStr = obj.getJSONArray("cities").toString()

            // parsing result
            return JsonParser.arrayList(citiesStr, City::class.java)
        } catch (e: Exception) {
            throw NetworkErrorException(e)
        }
    }
}
