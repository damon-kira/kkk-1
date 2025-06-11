package com.datepicker.lib

import android.util.Log

object Log {
    private const val TAG = "MDatePicker"
    private const val debug = false

    @JvmStatic
    fun i(tag: String?, msg: String?) {
        if (debug) {
            Log.i(TAG, "$tag > $msg")
        }
    }

    fun e(tag: String?, msg: String?) {
        Log.e(TAG, "$tag > $msg")
    }

    @JvmStatic
    fun w(tag: String?, msg: String?) {
        if (debug) {
            Log.w(TAG, "$tag > $msg")
        }
    }
}
