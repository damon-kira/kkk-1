package com.camera.lib

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.util.lib.DisplayUtils
import com.util.lib.log.logger_d
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class CameraXManager(
    activity: AppCompatActivity, mCameraView: PreviewView,
    cameraOrientation: Int
) : BaseCameraManager(activity) {

    private var previewView: PreviewView? = null

    private var mCamera = CameraXHelper(activity)

    init {
        defaultFacing = cameraOrientation
        previewView = mCameraView
        previewView?.post {
            setUpCamera()
        }
    }

    /**
     * 初始化CameraX
     */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener({
            mCamera.setCameraProviderFuture(cameraProviderFuture)
            //选择摄像头
            mCamera.setLensFacing(defaultFacing)
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(activity))
    }

    /**
     * 绑定相机并打开
     */
    private fun bindCameraUseCases() {

        val screenWidth = DisplayUtils.getRealScreenWidth(activity)
        val screenHeight = DisplayUtils.getRealScreenHeight(activity)
        val screenAspectRatio = aspectRatio(screenWidth, screenHeight)
        logger_d(TAG, "Preview aspect ratio: $screenAspectRatio")
        val rotation = previewView?.display?.rotation ?: 0
        //创建ImageCapture
        mCamera.buildImageCapture(screenAspectRatio, rotation)
        //打开相机
        previewView?.surfaceProvider?.let { mCamera.bindCamera(it) }
    }

    override fun openCamera(targetCameraId: Int): Int {
        setUpCamera()
        return -1
    }

    override fun takePicture(file: File, callback: (success: Boolean, filePath: File) -> Unit) {
        mCamera.takePicture(file, callback)
    }

    override fun autoFocus() {
        previewView?.let { mCamera.autoFocus(it) }

    }

    override fun switchFlash(): Boolean {
        return mCamera.switchFlash()
    }

    override fun openLight() {
        mCamera.openLight()
    }

    override fun hasBackCamera(): Boolean {
        return mCamera.hasBackCamera()
    }

    override fun hasFrontCamera(): Boolean {
        return mCamera.hasFrontCamera()
    }

    override fun switchCamera(callback: (curCameraId: Int) -> Unit) {
        val cameraId = if (CameraSelector.LENS_FACING_FRONT == mCamera.getLensFacing()) {
            FACING_BACK
        } else {
            FACING_FRONT
        }
        mCamera.setLensFacing(cameraId)
        bindCameraUseCases()
        activity.runOnUiThread {
            callback(cameraId)
        }
    }

    override fun getNumberOfCameras(): Int {
        return mCamera.getNumberOfCameras()
    }

    override fun isFront(): Boolean = CameraSelector.LENS_FACING_FRONT == mCamera.getLensFacing()

    override fun close() {
        mCamera.close()
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

}