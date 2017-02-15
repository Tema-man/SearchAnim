package dev.cherry.seacrhanim.entity

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Data class for Location model
 *
 * @author Artemii Vishnevskii
 * @since 14.02.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class Location() : Parcelable {
    @JsonProperty(value = "lat")
    var lat: Double = 0.0

    @JsonProperty(value = "lon")
    var lon: Double = 0.0

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Location> = object : Parcelable.Creator<Location> {
            override fun createFromParcel(source: Parcel): Location = Location(source)
            override fun newArray(size: Int): Array<Location?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this() {
        lat = source.readDouble()
        lon = source.readDouble()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeDouble(lat)
        dest?.writeDouble(lon)
    }

    override fun describeContents() = 0
}