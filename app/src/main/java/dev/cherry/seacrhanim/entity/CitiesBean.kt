package dev.cherry.seacrhanim.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * @author Artemii Vishnevskii
 * *
 * @since 07.03.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class CitiesBean {

    @JsonProperty(value = "cities")
    var cities: List<City> = emptyList()
}
