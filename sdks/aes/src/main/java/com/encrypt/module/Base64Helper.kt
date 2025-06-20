package com.encrypt.module

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException

object Base64Helper {
    private const val TAG = "debug_Base64helper"
    private val DEBUG = BuildConfig.DEBUG

    @JvmStatic
    fun encode(str: String?): String {
        var result = ""
        if (str.isNullOrEmpty()) return result
        result = encode(str.toByteArray(Charsets.UTF_8))
        return result
    }

    @JvmStatic
    fun encode(bytes: ByteArray?): String {
        var result = ""
        if (bytes != null) {
            try {
                result = String(Base64.encode(bytes, Base64.NO_WRAP), Charsets.UTF_8)
            } catch (e: UnsupportedEncodingException) {
                if (DEBUG) {
                    Log.e(TAG, "error = $e")
                }
            }
        }
        return result
    }


    @JvmStatic
    fun decode(str: String?): ByteArray? {
        return Base64.decode(str, Base64.NO_WRAP)
    }
}