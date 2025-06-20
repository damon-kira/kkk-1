package com.devoption.module

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

internal object DebugSPUtils {

    private lateinit var sp: SharedPreferences

    fun init(context: Context) {
        sp = context.getSharedPreferences("debug_mode", Context.MODE_PRIVATE)
    }

    fun setBoolean(key: String, value: Boolean) {
        sp.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String, defValue: Boolean = false) = sp.getBoolean(key, defValue)

    fun setString(key: String, value: String) {
        sp.edit { putString(key, value) }
    }

    fun getString(key: String, defValue: String) = sp.getString(key, defValue)

}