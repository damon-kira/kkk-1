package com.location.lib

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.util.*

/**
 * Create by weishl
 * 2022/9/19
 */
internal class LocationUpdatesHelper(private val context: Context) : ILocationService {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationCallback: LocationCallback? = null
    private var mLocationRequest: LocationRequest? = null
    private var lastUpdateLocation: Long = 0
    private var mServiceHandler: Handler? = null
    private var mTimeoutHandler: Handler? = null
    private var mHandlerThread: HandlerThread? = null
    private var mLastLocation: LocationInfo? = null


    //receive all callbacks
    private val listenerLocationSync: Vector<(LocationInfo?) -> Unit> = Vector()

    //only receive your own callback, may be replaced by other's callback
    private val listenerOnceLocationSync: Vector<(LocationInfo?) -> Unit> = Vector()

    private var mLocationManager: LocationManager? = null
    private var mLocationListener: LocationListener? = null
    private var isOnceOnly = true


    @SuppressLint("MissingPermission")
    override fun onLocationStart() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient?.setMockMode(false)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.lastLocation.isValid()) {
                    onNewLocation(locationResult!!.lastLocation)
                    logger_d(TAG, "FusedLocationProviderClient  ${locationResult.lastLocation}")
                } else {
                    onEmptyLocation()

                }
            }

            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                val bLocationAvailability = p0?.isLocationAvailable == true
                logger_d(TAG, "onLocationAvailability:$bLocationAvailability")

                if (!bLocationAvailability) {//定位不可用就系统api定位
                    apiLocation()
                }
//                if (bLocationAvailability) {//ensure that callbacks are received
//
//                    if (mTimeoutHandler?.hasMessages(TIMEOUT_MESSAGE) == true) {
//                        mTimeoutHandler?.removeMessages(TIMEOUT_MESSAGE)
//                    }
//
//                    mTimeoutHandler?.sendMessageDelayed(Message.obtain().apply {
//                        what = TIMEOUT_MESSAGE
//
//                    }, TIMEOUT_MILLISECONDS)
//
//                } else {
//                    apiLocation()
//                }
            }
        }

        mHandlerThread = HandlerThread(TAG)
        mHandlerThread?.start()
        mServiceHandler = Handler(mHandlerThread!!.looper)
        createLocationRequest()
        mTimeoutHandler = TimeoutHandler(mHandlerThread?.looper ?: return)
    }

    /**
     * LocationManager
     */
    @SuppressLint("MissingPermission")
    private fun apiLocation() {
        try {
            logger_d(TAG, "locationManager: start")
            mLocationListener?.also {
                mLocationManager?.removeUpdates(it)
            }
            if (mLocationManager == null) {
                mLocationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }

            mLocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    onNewLocation(location)
                    logger_d(TAG, "locationManager: $location")
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    logger_d(TAG, "locationManager: onStatusChanged $provider $status")
                }

                override fun onProviderEnabled(provider: String) {
                    logger_d(TAG, "locationManager: Enabled $provider ")
                }

                override fun onProviderDisabled(provider: String) {
                    logger_d(TAG, "locationManager: Disabled $provider ")
                }
            }

            mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).let {
                if (it != null) {
                    onNewLocation(it)
                }
                logger_d(TAG, "locationManager: requestLocationUpdates ")

                val providerList = mLocationManager?.getProviders(true)
                if (providerList != null) {
                    for (provider in providerList) {
                        if (android.location.LocationManager.GPS_PROVIDER == provider) {
                            mLocationManager?.requestLocationUpdates(
                                android.location.LocationManager.GPS_PROVIDER,
                                UPDATE_INTERVAL_IN_MILLISECONDS,
                                1F,
                                mLocationListener ?: return@let
                            )
                        } else if (android.location.LocationManager.NETWORK_PROVIDER == provider) {
                            mLocationManager?.requestLocationUpdates(
                                android.location.LocationManager.NETWORK_PROVIDER,
                                UPDATE_INTERVAL_IN_MILLISECONDS,
                                1F,
                                mLocationListener ?: return@let
                            )
                        } else if (android.location.LocationManager.PASSIVE_PROVIDER == provider) {
                            mLocationManager?.requestLocationUpdates(
                                android.location.LocationManager.PASSIVE_PROVIDER,
                                UPDATE_INTERVAL_IN_MILLISECONDS,
                                1F,
                                mLocationListener ?: return@let
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            onEmptyLocation()
        }
    }

    @Synchronized
    private fun onNewLocation(location: Location?) {
        val locationInfo = LocationInfo.copy(location)
        onNewLocation(locationInfo)
    }

    @Synchronized
    private fun onNewLocation(location: LocationInfo?) {
        logger_i(TAG, "onNewLocation lat:" + location?.latitude + "  lng:" + location?.longitude)
        dispatchLocationChanged(location)
        if (isOnceOnly && location != null) {
            removeLocationUpdates()
            mLastLocation = location
            mTimeoutHandler?.removeMessages(TIMEOUT_MESSAGE)
        }
    }

    private fun dispatchLocationChanged(location: LocationInfo?) {
        listenerLocationSync.forEach { callback ->
            callback.invoke(location)
        }
        listenerOnceLocationSync.forEach { callback ->
            callback.invoke(location)
        }
        removeAllOnceListener()
    }


    override fun requestLocationUpdates() {
        try {
            if (!isGPSAvailable(context)) {
                onEmptyLocation()
                return
            }
            var delay = 0L
            if (System.currentTimeMillis() - lastUpdateLocation < DELAY_MILLISECONDS) {
                logger_d(
                    TAG,
                    "update too fast :" + (System.currentTimeMillis() - lastUpdateLocation)
                )

                delay = DELAY_MILLISECONDS
            }
            lastUpdateLocation = System.currentTimeMillis()
            mServiceHandler?.postDelayed({
                logger_d(TAG, "Requesting location updates")
                removeLocationUpdates()

                createLocationRequest()

                if (GoogleApiAvailability.getInstance()
                        .isGooglePlayServicesAvailable(context) ==
                    ConnectionResult.SUCCESS
                ) {
                    mFusedLocationClient?.requestLocationUpdates(
                        mLocationRequest,
                        mLocationCallback, Looper.myLooper()
                    )
                } else {
                    apiLocation()
                }
                if (mTimeoutHandler?.hasMessages(TIMEOUT_MESSAGE) == true) {
                    mTimeoutHandler?.removeMessages(TIMEOUT_MESSAGE)
                }
                mTimeoutHandler?.sendMessageDelayed(Message.obtain().apply {
                    what = TIMEOUT_MESSAGE

                }, TIMEOUT_MILLISECONDS)
            }, delay)

        } catch (unlikely: SecurityException) {
            logger_e(TAG, "Lost location permission. Could not request updates. $unlikely")
        } catch (except: Exception) {
            logger_e(TAG, "requestLocationUpdates exception:$except")
        }
    }

    override fun requestOnceLocation(callback: (LocationInfo?) -> Unit) {
        isOnceOnly = true
        removeAllOnceListener()
        listenerOnceLocationSync.add(callback)
        requestLocationUpdates()
    }

    override fun requestLocation(callback: (LocationInfo?) -> Unit) {
        isOnceOnly = false
        listenerLocationSync.add(callback)
        requestLocationUpdates()
    }

    override fun getLastLocation(): LocationInfo? {
        return mLastLocation
    }

    private fun removeAllListener() {
        listenerLocationSync.clear()
        removeAllOnceListener()
    }

    private fun removeAllOnceListener() {
        listenerOnceLocationSync.clear()
    }

    private fun removeLocationUpdates() {
        logger_d(TAG, "Removing location updates")
        try {
            mFusedLocationClient?.removeLocationUpdates(mLocationCallback)
            mLocationListener?.also {
                mLocationManager?.removeUpdates(it)
            }

        } catch (unlikely: SecurityException) {
            logger_d(TAG, "Lost location permission. Could not remove updates. $unlikely")
        } catch (e: Exception) {
            logger_e(TAG, e.message.toString())
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun onLocationStop() {
        removeLocationUpdates()
        removeAllListener()
        mHandlerThread?.quit()
        mTimeoutHandler?.removeMessages(TIMEOUT_MESSAGE)
        mFusedLocationClient = null
        mLocationManager = null
    }


    private fun onEmptyLocation() {
        onNewLocation(mLastLocation)
    }

    fun isGPSAvailable(context: Context): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps || network) {
            return true
        }

        return false
    }


    inner class TimeoutHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                TIMEOUT_MESSAGE -> {
                    onEmptyLocation()
                    apiLocation()
                }
            }
        }
    }


    companion object {
        const val TAG = "LocationUpdatesService"
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000L
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2

        //ensure that callbacks are received
        private const val TIMEOUT_MESSAGE = 1
        private const val TIMEOUT_MILLISECONDS = 15000L

        //update too fast
        private const val DELAY_MILLISECONDS = 2000L
    }
}

