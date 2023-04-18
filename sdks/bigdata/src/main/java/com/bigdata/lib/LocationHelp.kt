package com.bigdata.lib

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import com.bigdata.lib.SpKeyManager.CASH_KEY_LOCATION_INFO
import com.bigdata.lib.SpKeyManager.CASH_KEY_REQUEST_CICY_TIMER
import com.bigdata.lib.SpKeyManager.CASH_KEY_REQUEST_LOCATION_TIMER
import com.cache.lib.SharedPrefGlobal
import com.location.lib.LocationInfo
import com.location.lib.OLocationManager
import com.util.lib.GsonUtil
import com.util.lib.checkCanRequest
import com.util.lib.log.logger_d
import com.util.lib.log.logger_i


class LocationHelp {
    companion object {
        val TAG = "LocationHelp"
        val config = BigDataManager.get().getNetDataListener()
        fun saveLocationInfo(location: LocationInfo) {
            val json = GsonUtil.toJson(location)
            SharedPrefGlobal.setString(CASH_KEY_LOCATION_INFO, json)
        }

        /**
         *
         * @return first param:longitude  second param:latitude
         * */
        fun getLocationInfo(): Pair<String, String>? {
            try {
                val json = SharedPrefGlobal.getString(CASH_KEY_LOCATION_INFO, "")
                logger_d(TAG, "31:getLocationInfo: json = $json")
                if (!TextUtils.isEmpty(json)) {
                    val info = GsonUtil.fromJsonNew(json, LocationInfo::class.java)
                    return Pair(
                        info?.longitude?.toString().orEmpty(),
                        info?.latitude?.toString().orEmpty()
                    )
                }
            } catch (e: Exception) {

            }
            return null
        }

        /**
         * 请求定位和获取经纬度城市
         */
        @SuppressLint("MissingPermission")
        fun requestLocation(listener: ((LocationInfo?) -> Unit)? = null) {
            //app 在后台不请求位置信息
            if (config == null) {
                logger_i(TAG, "please init BigDataManager first")
                return
            }
            OLocationManager.stop()
            val isAppFront = config.isAppFront()
            if (checkRequestLocationSuccessTimer() && isAppFront) {
                logger_i(TAG, "request location")
                OLocationManager.create(config.getContext())
                OLocationManager.getOnceLocationCallbackByAsync { location ->
                    Log.i(TAG, "requestLocation: location = $location")
                    location?.let {
                        saveLocationInfo(it)
                    }
                    listener?.invoke(location)
                }
            }
        }

        /**
         * 判断location定位成功的时间是否满足条件
         */
        fun checkRequestLocationSuccessTimer(): Boolean {
            return checkCanRequestLocation()
        }

        /**
         * 判断city请求成功的时间是否满足条件
         */
        fun checkRequestCitySuccessTimer(): Boolean {
            return checkCanRequestCity()
        }

        /**
         * 判断 经纬度多少天之内是否可以再次请求
         */
        private fun checkCanRequestLocation(): Boolean {
            return checkCanRequest(
                CASH_KEY_REQUEST_LOCATION_TIMER,
                MCLCManager.getIntervalTimer(),
                "Location"
            )
        }

        /**
         * 判断 城市信息多少天之内是否可以再次请求
         */
        private fun checkCanRequestCity(): Boolean {
            return checkCanRequest(
                CASH_KEY_REQUEST_CICY_TIMER,
                MCLCManager.getIntervalTimer(),
                "city"
            )
        }
    }
}