package com.aes.lib

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException

/**
 * Created by weishl on 2022/6/17
 *
 */
object Base64Utils {
    private const val TAG = "Base64Utils"
    private val DEBUG = BuildConfig.DEBUG

    @JvmStatic
    fun encode(str: String?): String {
        var result = ""
        if (str != null) {
            try {
                result = String(
                    Base64.encode(str.toByteArray(charset("utf-8")), Base64.NO_WRAP),
                    Charsets.UTF_8
                )
            } catch (e: UnsupportedEncodingException) {
                if (DEBUG) {
                    Log.e(TAG, "error = $e")
                }
            }
        }
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
    fun encodeBase64(bytes: ByteArray?): String {
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