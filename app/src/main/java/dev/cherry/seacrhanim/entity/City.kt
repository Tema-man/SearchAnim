package dev.cherry.seacrhanim.entity

import android.location.Location
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * @author DVLP_2
 * @since 13.02.2017.
 */
class City {
    @JsonProperty("countryCode")
    var countryCode: String? = null

    @JsonProperty("country")
    var country: String? = null

    @JsonProperty("latinFullName")
    var latinFullName: String? = null

    @JsonProperty("fullname")
    var fullname: String? = null

    @JsonProperty("clar")
    var clar: String? = null

    @JsonProperty("latinClar")
    var latinClar: String? = null

    @JsonProperty("location")
    var location: Location? = null

    @JsonProperty("hotelsCount")
    var hotelsCount: Int = 0

    @JsonProperty("iata")
    var iata: List<String>? = null

    @JsonProperty("city")
    var city: String? = null

    @JsonProperty("latinCity")
    var latinCity: String? = null

    @JsonProperty("timezone")
    var timezone: String? = null

    @JsonProperty("timezonesec")
    var timezonesec: Int = 0

    @JsonProperty("latinCountry")
    var latinCountry: String? = null

    @JsonProperty("id")
    var id: Int = 0

    @JsonProperty("countryId")
    var countryId: Int = 0

    @JsonProperty("_score")
    var score: Int = 0

    @JsonProperty("state")
    var state: Any? = null
}