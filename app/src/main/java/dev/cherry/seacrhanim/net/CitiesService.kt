package dev.cherry.seacrhanim.net

import dev.cherry.seacrhanim.entity.CitiesBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Artemii Vishnevskii
 * @since 07.03.2017.
 */
interface CitiesService {

    @GET("autocomplete?")
    fun getCities(@Query("term") name: String,
                  @Query("lang") lang: String): Call<CitiesBean>
}