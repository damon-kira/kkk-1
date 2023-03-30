package com.location.lib


/**
 * Create by weishl
 * 2022/9/19
 */
interface ILocationService {

    fun onLocationStart()

    fun onLocationStop()

    fun requestLocationUpdates()

    fun requestOnceLocation(callback: (LocationInfo?) -> Unit)

    fun requestLocation(callback: (LocationInfo?) -> Unit)

    fun getLastLocation(): LocationInfo?
}