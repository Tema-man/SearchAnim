package dev.cherry.seacrhanim.entity

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


/**
 * Data class for City model
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class City() : Parcelable {
    @JsonProperty("city")
    var city: String = ""

    @JsonProperty("fullname")
    var fullname: String = ""

    @JsonProperty("location")
    var location: Location = Location()

    @JsonProperty("iata")
    var iata: ArrayList<String> = ArrayList()

    override fun toString(): String {
        return "$fullname " + if (iata.isEmpty()) "" else "(${iata.get(0)})"
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
            override fun createFromParcel(source: Parcel): City = City(source)
            override fun newArray(size: Int): Array<City?> = arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0

    constructor(source: Parcel) : this() {
        city = source.readString()
        fullname = source.readString()
        source.readStringList(iata)
        location = source.readParcelable(Location::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(city)
        dest?.writeString(fullname)
        dest?.writeStringList(iata)
        dest?.writeParcelable(location, flags)
    }
}
