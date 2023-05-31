package com.camera.lib

import java.io.File


interface ICamera {

    /**
     * 打开相机
     */
    fun openCamera(targetCameraId: Int): Int


    /**
     * 开始拍照
     */
    fun takePicture(file: File, callback: (success: Boolean, filePath: File) -> Unit = { success: Boolean, filePath: File -> })

    /**
     * 关闭相机 释放资源
     */
    fun close()

    /**
     * 自动对焦
     */
    fun autoFocus()

    /**
     * 闪光灯
     */
    fun switchFlash(): Boolean

    /**
     * 是否有闪光灯
     */
    fun hasFlash(): Boolean

    /**
     * 打开手电筒
     */
    fun openLight()

    /**
     * 切换摄像头
     * curCameraId:切换后的摄像头  前 or 后
     */
    fun switchCamera(callback: (curCameraId: Int) -> Unit = { curCameraId -> })
    /**
     * 是否有后置摄像头
     */
    fun hasBackCamera(): Boolean

    /**
     * 是否有前置摄像头
     */
    fun hasFrontCamera(): Boolean

    /**
     * 获取可用摄像头数
     */
    fun getNumberOfCameras(): Int

    /**
     * 是否为前置摄像头
     */
    fun isFront(): Boolean
}