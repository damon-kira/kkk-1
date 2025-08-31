package com.kira.learning.xml.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kira.learning.xml.modules.custom.CustomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class XjbgdfdfService : Service() {

    @Inject
    @JvmField
    var mCustomViewModel: CustomViewModel? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val flag = intent?.getIntExtra(SerManager.EXTRA_KEY_FLAG, -1) ?: -1

        when (flag) {
            SerManager.FLAG_CUSTOM -> {
                mCustomViewModel?.getCustomInfo()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}