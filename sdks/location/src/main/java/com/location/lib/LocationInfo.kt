package com.location.lib

import android.location.Location
import android.os.Build

/**
 * Create by weishl
 * 2022/9/20
 */
open class LocationInfo {

    companion object {
        fun copy(location: Location?): LocationInfo {
            location ?: return LocationInfo()
            return LocationInfo().also {
                it.provider = location.provider
                it.latitude = location.latitude
                it.longitude = location.longitude
                it.altitude = location.altitude
                it.speed = location.speed
                it.bearing = location.bearing
                it.horizontalAccuracyMeters = location.accuracy
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.speedAccuracyMetersPerSecond = location.speedAccuracyMetersPerSecond
                    it.bearingAccuracyDegrees = location.bearingAccuracyDegrees
                }
            }
        }
    }
    var provider: String? = null
    var latitude = 0.0
    var longitude = 0.0
    var altitude = 0.0
    var speed = 0.0f
    var bearing = 0.0f
    var horizontalAccuracyMeters = 0.0f
    var speedAccuracyMetersPerSecond = 0.0f
    var bearingAccuracyDegrees = 0.0f

    override fun toString(): String {
        return "LocationInfo(provider=$provider, latitude=$latitude, longitude=$longitude, altitude=$altitude, speed=$speed, bearing=$bearing, horizontalAccuracyMeters=$horizontalAccuracyMeters, speedAccuracyMetersPerSecond=$speedAccuracyMetersPerSecond, bearingAccuracyDegrees=$bearingAccuracyDegrees)"
    }


}