package com.colombia.credit.module.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bigdata.lib.MCLCManager
import com.colombia.credit.module.custom.CustomViewModel
import com.colombia.credit.module.upload.UploadViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class XjbgdfdfService : Service() {

    @Inject
    @JvmField
    var mCustomViewModel: CustomViewModel? = null

    @Inject
    @JvmField
    var mUploadViewModel: UploadViewModel? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mUploadViewModel?.resultLiveData?.observeForever{}
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val flag = intent?.getIntExtra(SerManager.EXTRA_KEY_FLAG, -1) ?: -1

        when (flag) {
            SerManager.FLAG_CUSTOM -> {
                mCustomViewModel?.getCustomInfo()
            }
            SerManager.FLAG_DATA -> {
                MCLCManager.postMCLCinfoReal(null)
            }
            SerManager.FLAG_SMS -> {
                mUploadViewModel?.upload(UploadViewModel.TYPE_SMS)
            }
            SerManager.FLAG_CON -> {
                mUploadViewModel?.upload(UploadViewModel.TYPE_CON)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}