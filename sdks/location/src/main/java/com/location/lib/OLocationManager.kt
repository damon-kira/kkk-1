package com.location.lib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import com.util.lib.log.logger_e


object OLocationManager {

    private const val TAG = "debug_OLocationManager"

    private var locationService: ILocationService? = null
    lateinit var applicationContext: Context

    fun bindLifeCycle(lifecycle: Lifecycle, context: Context) {
        applicationContext = context
        locationService = LocationLifecycleService(lifecycle, context.applicationContext)
    }

    fun unbindLifeCycle() {
        locationService = null
    }

    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ]
    )

    fun create(context: Context) {
        applicationContext = context
        if (locationService == null) {
            locationService = LocationUpdatesHelper(applicationContext)
        }
        locationService?.onLocationStart()
    }

    fun stop() {
        locationService?.onLocationStop()
    }

    /**
     * 只获取一次回调，优先读取缓存
     */
    fun getOnceLocationCallbackByAsync(callback: (LocationInfo?) -> Unit) {
        requestOnceCallbackLocation(callback)
    }

    /**
     * 会获取多次回调，除非定位不可用
     */
    fun addLocationUpdateListener(locationUpdateListener: (LocationInfo?) -> Unit) {
        requestLocation(locationUpdateListener)
    }


    @RequiresPermission(
        allOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ]
    )
    fun requestLocationUpdates() {
        requestLocation {  }
    }

    fun getLastLocation(): LocationInfo? {
        return locationService?.getLastLocation()
    }

    @Synchronized
    private fun requestOnceCallbackLocation(callback: (LocationInfo?) -> Unit) {
        if (locationService == null) {
            logger_e(TAG, "no location service available")
            callback.invoke(locationService?.getLastLocation())
        }
        locationService?.requestOnceLocation(callback)
    }

    @Synchronized
    private fun requestLocation(callback: (LocationInfo?) -> Unit) {
        if (locationService == null) {
            logger_e(TAG, "no location service available")
            callback.invoke(locationService?.getLastLocation())
        }
        locationService?.requestLocation(callback)
    }

    fun openGPS(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivityForResult(intent, 811)
        } catch (e: Exception) {
            logger_e(TAG, e.message.orEmpty())
        }

    }

    fun checkPermissions(context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestPermissions(activity: Activity, requestCode: Int, view: View) {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Snackbar.make(
                view,
                R.string.permission_rationale,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        requestCode
                    )
                }
                .show()
        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
        }
    }

}

fun Location?.isValid(): Boolean {
    return this != null
}

