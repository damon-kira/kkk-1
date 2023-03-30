package com.location.lib

import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


internal class LocationLifecycleService(
    private val lifecycle: Lifecycle,
    private val applicationContext: Context
) : LifecycleObserver, ILocationService {

    init {
        lifecycle.addObserver(this)
    }

    private val mLocationHelper by lazy {
        LocationUpdatesHelper(applicationContext)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onLocationStart() {
        mLocationHelper.onLocationStart()
    }

    override fun onLocationStop() {
        mLocationHelper.onLocationStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        onLocationStop()
    }


    override fun getLastLocation(): LocationInfo? {
        return mLocationHelper.getLastLocation()
    }

    override fun requestLocationUpdates() {
        mLocationHelper.requestLocationUpdates()
    }

    override fun requestOnceLocation(callback: (LocationInfo?) -> Unit) {
        mLocationHelper.requestLocation(callback)
    }

    override fun requestLocation(callback: (LocationInfo?) -> Unit) {
        mLocationHelper.requestLocation(callback)
    }
}