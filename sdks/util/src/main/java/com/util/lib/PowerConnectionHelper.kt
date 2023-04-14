package com.util.lib

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

object PowerConnectionHelper {

    fun getStatus(context: Context): Int {
        val intent = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let {
            context.registerReceiver(null, it)
        }
        return intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
    }

    // 是否在充电
    fun isCharging(context: Context): Boolean {
        val status = getStatus(context)
        return status == BatteryManager.BATTERY_PLUGGED_AC || status == BatteryManager.BATTERY_PLUGGED_USB || status == BatteryManager.BATTERY_PLUGGED_WIRELESS
    }

    fun isUsb(context: Context): Int {
        return if (getStatus(context) == BatteryManager.BATTERY_PLUGGED_USB) {
            1
        } else 0
    }

    fun isAc(context: Context) =
        if (getStatus(context) == BatteryManager.BATTERY_PLUGGED_AC) 1 else 0

}