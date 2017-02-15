package dev.cherry.seacrhanim.entity

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Data class for City model
 *
 * @author Artemii Vishnevskii
 * @since 13.02.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class City() : Parcelable {
    @JsonProperty("fullname")
    var fullname: String? = null

    @JsonProperty("location")
    var location: Location? = null

    @JsonProperty("iata")
    lateinit var iata: List<String>

    override fun toString(): String {
        return "$fullname " + if (iata.isEmpty()) "" else "(${iata[0]})"
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<City> = object : Parcelable.Creator<City> {
            override fun createFromParcel(source: Parcel): City = City(source)
            override fun newArray(size: Int): Array<City?> = arrayOfNulls(size)
        }
    }

    override fun describeContents() = 0

    constructor(source: Parcel) : this() {
        fullname = source.readString()
        source.readList(iata, List::class.java.classLoader)
        location = source.readParcelable(Location::class.java.classLoader)
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(fullname)
        dest?.writeList(iata)
        dest?.writeParcelable(location, flags)
    }
}
