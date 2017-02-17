package dev.cherry.seacrhanim.presentation.map.utils

import com.google.android.gms.maps.model.LatLng

/**
 * @author Artemii Vishnevskii
 * @since 17.02.2017.
 */
class SphericalMercatorProjection(internal val mWorldWidth: Double) {

    data class Point(val x: Double, val y: Double)

    fun toPoint(latLng: LatLng): Point {
        val x = latLng.longitude / 360.0 + 0.5
        val siny = Math.sin(Math.toRadians(latLng.latitude))
        val y = 0.5 * Math.log((1.0 + siny) / (1.0 - siny)) / -6.283185307179586 + 0.5
        return Point(x * this.mWorldWidth, y * this.mWorldWidth)
    }

    fun toLatLng(point: Point): LatLng {
        val x = point.x / this.mWorldWidth - 0.5
        val lng = x * 360.0
        val y = 0.5 - point.y / this.mWorldWidth
        val lat = 90.0 - Math.toDegrees(Math.atan(Math.exp(-y * 2.0 * 3.141592653589793)) * 2.0)
        return LatLng(lat, lng)
    }
}
