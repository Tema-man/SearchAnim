package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.model.LatLng

/**
 * Interface for different coordinates projectors. Use to project sphere geo coordinates to plane
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
interface GeoProjector {

    /**
     * Project [LatLng] to plane
     *
     * @param latLng [LatLng] latLng to project
     * @return [SineInterpolator.Point] projected point
     */
    fun toPoint(latLng: LatLng): SineInterpolator.Point

    /**
     * Unproject [SineInterpolator.Point] from plane back to [LatLng]
     *
     * @param point [SineInterpolator.Point] point to unproject
     * @return [LatLng] projected latLng
     */
    fun toLatLng(point: SineInterpolator.Point): LatLng

    /**
     * GeoProjector implementation. Used Mercator projection to project geo coordinates to plane
     *
     * @param worldWidth size of world
     */
    open class Mercator(val worldWidth: Double) : GeoProjector {

        override fun toPoint(latLng: LatLng): SineInterpolator.Point {
            val x = latLng.longitude / 360.0 + 0.5
            val siny = Math.sin(Math.toRadians(latLng.latitude))
            val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / -6.283185307179586 + 0.5
            return SineInterpolator.Point(x * worldWidth, y * worldWidth)
        }

        override fun toLatLng(point: SineInterpolator.Point): LatLng {
            val x = point.x / worldWidth - 0.5
            val lng = x * 360.0
            val y = 0.5 - point.y / worldWidth
            val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * 3.141592653589793)) * 2.0)
            return LatLng(lat, lng)
        }
    }
}