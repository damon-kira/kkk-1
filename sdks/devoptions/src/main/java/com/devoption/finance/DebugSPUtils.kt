package com.devoption.finance

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by weishl on 2021/7/13
 *
 */
internal object DebugSPUtils {

    private lateinit var sp: SharedPreferences

    fun init(context: Context) {
        sp = context.getSharedPreferences("debug_mode", Context.MODE_PRIVATE)
    }

    fun setBoolean(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean = false) = sp.getBoolean(key, defValue)

    fun setString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String) = sp.getString(key, defValue)

}