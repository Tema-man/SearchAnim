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
     * @return [GeoInterpolator.Point] projected point
     */
    fun toPoint(latLng: LatLng): GeoInterpolator.Point

    /**
     * Unproject [GeoInterpolator.Point] from plane back to [LatLng]
     *
     * @param point [GeoInterpolator.Point] point to unproject
     * @return [LatLng] projected latLng
     */
    fun toLatLng(point: GeoInterpolator.Point): LatLng

    /**
     * GeoProjector implementation. Used Mercator projection to project geo coordinates to plane
     */
    class Mercator() : GeoProjector {

        /** Needs to project geo coordinates to a plane using Mercator projection */
        private val DEFAULT_WORLD_WIDTH = 100.0

        constructor (width: Double) : this() {
            worldWidth = width
        }

        private var worldWidth = DEFAULT_WORLD_WIDTH

        override fun toPoint(latLng: LatLng): GeoInterpolator.Point {
            var x = latLng.longitude / 360.0 + 0.5
            x = if (x > 0.5) 0.5 + (1.0 - x) else (0.5 - x)
            val siny = Math.sin(Math.toRadians(latLng.latitude))
            val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / (-2.0 * Math.PI) + 0.5
            return GeoInterpolator.Point(x * worldWidth, y * worldWidth)
        }

        override fun toLatLng(point: GeoInterpolator.Point): LatLng {
            var x = if (point.x > 0.5) (1.5 - point.x) else (0.5 - point.x)
            x = x / worldWidth - 0.5
            val lng = x * 360.0
            val y = 0.5 - point.y / worldWidth
            val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * Math.PI)) * 2.0)
            return LatLng(lat, lng)
        }
    }
}