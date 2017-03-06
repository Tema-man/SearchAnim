package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.model.LatLng

/**
 * Interface for different coordinates projectors. Use to project sphere geo coordinates to plane
 *
 * @author Artemii Vishnevskii
 * @author Temaa.mann@gmail.com
 */
interface GeoProjector {

    /** Data class to store plane coordinates */
    data class Point(val x: Double, val y: Double)

    /**
     * Project [LatLng] to plane
     *
     * @param latLng [LatLng] latLng to project
     * @return [Point] projected point
     */
    fun toPoint(latLng: LatLng): Point

    /**
     * Unproject [Point] from plane back to [LatLng]
     *
     * @param point [Point] point to unproject
     * @return [LatLng] projected latLng
     */
    fun toLatLng(point: Point): LatLng

    /**
     * Returns world size to fix 0-meridian translation
     *
     * @return [Double] world size
     */
    fun getWorldSize(): Double

    /**
     * GeoProjector implementation. Used Mercator projection to project geo coordinates to plane
     */
    class Mercator : GeoProjector {

        /** Needs to project geo coordinates to a plane using Mercator projection */
        private val DEFAULT_WORLD_WIDTH = 100.0

        private var worldWidth = DEFAULT_WORLD_WIDTH

        override fun toPoint(latLng: LatLng): Point {
            val x = latLng.longitude / 360.0 + 0.5
            val siny = Math.sin(Math.toRadians(latLng.latitude))
            val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / -6.283185307179586 + 0.5
            return Point(x * worldWidth, y * worldWidth)
        }

        override fun toLatLng(point: Point): LatLng {
            val x = point.x / worldWidth - 0.5
            val lng = x * 360.0
            val y = 0.5 - point.y / worldWidth
            val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * 3.141592653589793)) * 2.0)
            return LatLng(lat, lng)
        }

        override fun getWorldSize(): Double {
            return worldWidth
        }
    }
}