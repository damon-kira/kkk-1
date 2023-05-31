package com.camera.lib

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity


abstract class BaseCameraManager(val activity: AppCompatActivity) : ICamera {

    companion object {
        const val TAG = "CameraView"
        const val RATIO_4_3_VALUE = 4.0 / 3.0
        const val RATIO_16_9_VALUE = 16.0 / 9.0
        const val FACING_BACK = 1
        const val FACING_FRONT = 2

        //camera1用
        val SCREEN_PORTRAIT: Int = 0x10 // 竖屏
        val SCREEN_LANDSCAPE: Int = 0x11 // 横屏
    }

    //默认启动后置摄像头
    var defaultFacing = FACING_BACK


    override fun hasFlash(): Boolean {
        return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

}