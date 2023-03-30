package com.location.lib

import android.location.Location

internal object LocationStorage {

    private const val MODULE_NAME = "location"
    private const val LATITUDE = "latitude"
    private const val LONGITUDE = "longitude"
    private val mSharedPreference by lazy {
        OLocationManager.applicationContext.getSharedPreferences(
            MODULE_NAME,
            android.content.Context.MODE_PRIVATE
        )
    }

    private var mLatitude: String
        get() = mSharedPreference.getString(
            LATITUDE,
            ""
        ).orEmpty()
        set(value) = mSharedPreference.edit().putString(
            LATITUDE, value
        ).apply()

    private var mLongitude: String
        get() = mSharedPreference.getString(
            LONGITUDE,
            ""
        ).orEmpty()
        set(value) = mSharedPreference.edit().putString(
            LONGITUDE, value
        ).apply()


    fun getLastLocation(): Location? {
        if (mLatitude.isEmpty() || mLongitude.isEmpty()) {
            return null
        }
        var location: Location? = null
        try {
            location = Location("").apply {
                latitude = mLatitude.toDouble()
                longitude = mLongitude.toDouble()
            }
        } catch (e: Exception) {

        }
        return location
    }

    fun updateLocation(location: Location) {
        if (location.isValid()) {
            location.apply {
                mLatitude = latitude.toString()
                mLongitude = longitude.toString()
            }
        }
    }
}
