package com.colombia.credit.module.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.colombia.credit.module.custom.CustomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class XjbgdfdfService : Service() {

    @Inject
    lateinit var mCustomViewModel: CustomViewModel

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val flag = intent?.getIntExtra(SerManager.EXTRA_KEY_FLAG, -1) ?: -1

        if (flag == SerManager.FLAG_CUSTOM) {
            mCustomViewModel.getCustomInfo()
        }

        return super.onStartCommand(intent, flags, startId)
    }
}