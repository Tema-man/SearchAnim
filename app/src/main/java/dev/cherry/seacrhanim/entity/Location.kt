package dev.cherry.seacrhanim.entity

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author DVLP_2
 * @since 14.02.2017.
 */
class Location {

    @JsonProperty(value = "lat")
    var lat: Double? = null

    @JsonProperty(value = "lon")
    var lon: Double? = null
}