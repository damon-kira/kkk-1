package com.camera.lib

import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import com.google.common.util.concurrent.ListenableFuture
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.io.File
import java.io.FileInputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 *@author zhujun
 *@description:
 *@date : 2022/7/27 5:03 下午
 */
class CameraXHelper(val activity: AppCompatActivity) {


    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    //摄像头限制器
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var preview: Preview? = null

    //拍照相关
    private var imageCapture: ImageCapture? = null

    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()


    fun buildImageCapture(screenAspectRatio: Int, rotation: Int) {
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            // 传入宽高比 CameraX 针对最适合我们特定分辨率进行优化
            .setTargetAspectRatio(screenAspectRatio)
            // 设置 rotation
            .setTargetRotation(rotation)
            .build()

        // Preview
        preview = Preview.Builder()
            // 传入宽高比 无需传尺寸
            .setTargetAspectRatio(screenAspectRatio)
            // 设置 rotation
            .setTargetRotation(rotation)
            .build()
    }


    fun bindCamera(surfaceProvider: Preview.SurfaceProvider) {
        // 绑定前必须先解绑
        cameraProvider?.unbindAll()

        val cameraProvider = cameraProvider
            ?: throw IllegalStateException("Camera initialization failed.")

        // 设置前后置摄像头
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        try {
            camera = cameraProvider.bindToLifecycle(
                activity, cameraSelector, preview, imageCapture
            )
            preview?.setSurfaceProvider(surfaceProvider)

            camera?.cameraInfo?.let { observeCameraState(it) }
        } catch (exc: Exception) {
            logger_e(BaseCameraManager.TAG, "Use case binding failed ${exc.message}")
        }
    }

    /**
     * 监听相机状态
     */
    private fun observeCameraState(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.observe(activity) { cameraState ->
            run {
                when (cameraState.type) {
                    CameraState.Type.PENDING_OPEN -> {
                        logger_i(BaseCameraManager.TAG, "CameraState: Pending Open")
                    }
                    CameraState.Type.OPENING -> {
                        logger_i(BaseCameraManager.TAG, "CameraState: Opening")
                    }
                    CameraState.Type.OPEN -> {
                        logger_i(BaseCameraManager.TAG, "CameraState: Open")
                    }
                    CameraState.Type.CLOSING -> {
                        logger_i(BaseCameraManager.TAG, "CameraState: Closing")
                    }
                    CameraState.Type.CLOSED -> {
                        logger_i(BaseCameraManager.TAG, "CameraState: Closed")
                    }
                }
            }

            cameraState.error?.let { error ->
                when (error.code) {
                    // Open errors
                    CameraState.ERROR_STREAM_CONFIG -> {
                        logger_e(BaseCameraManager.TAG, "Stream config error")
                    }
                    // Opening errors
                    CameraState.ERROR_CAMERA_IN_USE -> {
                        logger_e(BaseCameraManager.TAG, "Camera in use")
                    }
                    CameraState.ERROR_MAX_CAMERAS_IN_USE -> {
                        logger_e(BaseCameraManager.TAG, "Max cameras in use")
                    }
                    CameraState.ERROR_OTHER_RECOVERABLE_ERROR -> {
                        logger_e(BaseCameraManager.TAG, "Other recoverable error")
                    }
                    // Closing errors
                    CameraState.ERROR_CAMERA_DISABLED -> {
                        logger_e(BaseCameraManager.TAG, "Camera disabled")
                    }
                    CameraState.ERROR_CAMERA_FATAL_ERROR -> {
                        // 重启设备以恢复摄像头功能
                        logger_e(BaseCameraManager.TAG, "Fatal error")
                    }
                    // Closed errors
                    CameraState.ERROR_DO_NOT_DISTURB_MODE_ENABLED -> {
                        // 禁用“请勿打扰”模式，然后重新打开相机
                        logger_e(BaseCameraManager.TAG, "Do not disturb mode enabled")
                    }
                }
            }
        }
    }

    fun autoFocus(previewView: PreviewView) {
        previewView.setOnTouchListener { _, event ->
            val action = FocusMeteringAction.Builder(
                previewView.meteringPointFactory
                    .createPoint(event.x, event.y)
            ).build()
            camera?.cameraControl?.startFocusAndMetering(action)
            true
        }
    }

    fun openLight() {
        if (camera?.cameraInfo?.torchState?.value == TorchState.ON) {
            camera?.cameraControl?.enableTorch(false)
        } else {
            camera?.cameraControl?.enableTorch(true)
        }
    }

    fun takePicture(file: File, callback: (success: Boolean, filePath: File) -> Unit) {
        imageCapture?.let { imageCapture ->
            if (cameraProvider?.isBound(imageCapture) != true) {
                logger_e(BaseCameraManager.TAG, "相机已关闭，请先打开相机")
                return
            }

            val metadata = ImageCapture.Metadata().apply {
                //使用前置摄像头时镜像
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }

            val outputOptions = ImageCapture.OutputFileOptions.Builder(file)
                .setMetadata(metadata)
                .build()

            // 拍照后监听
            imageCapture.takePicture(
                outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        logger_e(BaseCameraManager.TAG, "Photo capture failed: ${exc.message}")
                        callback(false, file)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: Uri.fromFile(file)
                        logger_d(BaseCameraManager.TAG, "Photo capture succeeded: $savedUri")

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            try {
                                val outputStream =
                                    activity.contentResolver.openOutputStream(savedUri)
                                val fileInputStream = FileInputStream(file)
                                if (outputStream != null) {
                                    FileUtils.copy(fileInputStream, outputStream)
                                }
                                fileInputStream.close()
                                outputStream?.close()
                                logger_i(BaseCameraManager.TAG,"拍摄成功 path:${file.absolutePath}")
                            } catch (e: Exception) {

                            }
                        } else {
                            MediaScannerConnection.scanFile(
                                activity,
                                arrayOf(file.absolutePath),
                                arrayOf("image/jpeg")
                            ) { path: String?, uri: Uri? ->
                                logger_i(BaseCameraManager.TAG,"拍摄成功 path:${file.absolutePath}")
                            }
                        }
                        callback(true, file)
                    }
                })
        }
    }

    fun switchFlash(): Boolean {
        return if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_ON) {
            imageCapture?.flashMode = ImageCapture.FLASH_MODE_OFF
            false
        } else {
            imageCapture?.flashMode = ImageCapture.FLASH_MODE_ON
            true
        }

    }

    fun close() {
        cameraProvider?.unbindAll()// 解绑相机提供器
        cameraExecutor.shutdown()// 关闭线程池
    }

    fun setCameraProviderFuture(cameraProviderFuture: ListenableFuture<ProcessCameraProvider>) {
        cameraProvider = cameraProviderFuture.get()
    }

    fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    fun getNumberOfCameras(): Int {
        val number = cameraProvider?.availableCameraInfos?.size ?: 0
        logger_i(BaseCameraManager.TAG, "摄像头数：$number")
        return number
    }

    fun setLensFacing(defaultFacing: Int) {
        lensFacing = when {
            defaultFacing == BaseCameraManager.FACING_BACK && hasBackCamera() -> CameraSelector.LENS_FACING_BACK
            defaultFacing == BaseCameraManager.FACING_FRONT && hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
            else -> throw IllegalStateException("Back and front camera are unavailable")
        }
    }

    fun getLensFacing(): Int {
        return lensFacing
    }

}