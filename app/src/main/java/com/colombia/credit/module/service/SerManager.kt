package com.colombia.credit.module.service

import android.content.Intent
import com.colombia.credit.app.getAppContext
import com.util.lib.log.logger_e

object SerManager {

    private const val TAG = "SerManager"

    const val EXTRA_KEY_FLAG = "key_flag"
    const val FLAG_CUSTOM = 0x11

    private fun startService(flag: Int) {
        try {
            val ctx = getAppContext()
            val intent = Intent(ctx, XjbgdfdfService::class.java).also {
                it.putExtra(EXTRA_KEY_FLAG, flag)
            }
            ctx.startService(intent)
        } catch (e: Exception) {
            logger_e(TAG, "error = $e")
        }
    }

    fun getCustom(){
        startService(FLAG_CUSTOM)
    }
}