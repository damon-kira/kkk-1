package com.bigdata.lib

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

object BatteryManager {
    /**
     * 获取电量 intent
     */
    @JvmStatic
    private fun getBatteryIntent(): Intent? {
        val config = BigDataManager.get().getNetDataListener() ?: return null
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        var intent: Intent? = null
        try {
            intent = config.getContext().registerReceiver(null, intentFilter)
        } catch (e: Exception) {

        }
        return intent
    }

    /**
     * 获取最大电量
     */
    @JvmStatic
    fun getMaxBattery(): Int {
        var intent = getBatteryIntent()
        return intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    }

    /**
     * 获取剩余电量
     */
    @JvmStatic
    fun getLevelBattery(): Int {
        val intent = getBatteryIntent()
        return intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    }
}