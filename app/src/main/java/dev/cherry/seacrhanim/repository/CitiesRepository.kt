package dev.cherry.seacrhanim.repository

import dev.cherry.seacrhanim.entity.City
import dev.cherry.seacrhanim.net.CitiesService
import dev.cherry.seacrhanim.net.RestApi

/**
 * Repository class. Provides [City] data.
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
class CitiesRepository(val restApi: RestApi, val citiesService: CitiesService) {

    // selected source point
    var source: City? = null

    //selected destinatoin point
    var destination: City? = null

    /**
     * Requests a cities list from server. Call is blocking, because of network request.
     *
     * @param name city name or iata code
     * @param lang language for result
     * @return [List] of [City]
     */
    fun getCities(name: String, lang: String): List<City> {
        try {
            val call = citiesService.getCities(name, lang)
            return restApi.execute(call).cities
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }
}
