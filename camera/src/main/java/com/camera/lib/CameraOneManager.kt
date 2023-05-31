package com.camera.lib

import android.app.Activity
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.view.Surface
import android.view.TextureView
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.util.lib.DisplayUtils
import com.util.lib.MainHandler
import com.util.lib.ThreadPoolUtil
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.io.File
import java.io.FileOutputStream


class CameraOneManager(
    activity: AppCompatActivity,
    cameraView: TextureView,
    private val cameraOrientation: Int,
    private val screenOrientation: Int
) : BaseCameraManager(activity), TextureView.SurfaceTextureListener {

    private val CAMERA_BACK = Camera.CameraInfo.CAMERA_FACING_BACK
    private val CAMERA_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT
    private val CAMERA_NULL = -1

    private var mCamera = CameraOneUtils()
    private var textureView: TextureView? = null
    private var mSurface: SurfaceTexture? = null
    private var mWidth = 0
    private var mHeight = 0
    private var mCameraId = CAMERA_NULL

    init {
        textureView = cameraView
        textureView?.surfaceTextureListener = this@CameraOneManager
    }


    override fun openCamera(targetCameraId: Int): Int {
        val width = mWidth
        val height = mHeight
        val surface = mSurface
        if (width == 0 || height == 0 || surface == null) {
            activity.finish()
            return CAMERA_NULL
        }
        val screenWidth = DisplayUtils.getRealScreenWidth(activity)
        val screenHeight = DisplayUtils.getRealScreenHeight(activity)

        val cameraId = mCamera.open(targetCameraId)

        if (cameraId == CAMERA_NULL) {
            Toast.makeText(
                activity,
                activity.getString(R.string.capture_open_camera_failure),
                Toast.LENGTH_SHORT
            ).show()
//            toast(R.string.capture_open_camera_failure)
            activity.finish()
            return CAMERA_NULL
        }
        setCurrentCameraId(cameraId)
        val degrees = getCameraDegrees(activity, cameraId)
        mCamera.setDisplayOrientation(degrees)
        val parameters = mCamera.getParameters()

        if (parameters != null) {
            val size = mCamera.getNearestRatioSize(parameters, screenWidth, screenHeight)
            if (size != null) {
                logger_i(TAG, "104 set preview size:${size.width},${size.height}")
                // 预览大小
                parameters.setPreviewSize(size.width, size.height)
            }

            val pictureSize = mCamera.getBestPictureSize(parameters)
            if (pictureSize != null) {
                parameters.setPictureSize(pictureSize.width, pictureSize.height)
            }

            logger_i(
                TAG, "set picture size:${pictureSize?.width ?: 0},${
                    pictureSize?.height
                        ?: 0
                }"
            )

            // 聚焦模式
            val focusModes = parameters.supportedFocusModes
            if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            }

            // 图片&预览方向 rotation – 相对于相机方向的旋转角度（以度为单位）。旋转只能是 0、90、180 或 270。
            if (degrees == 0 || degrees == 90 || degrees == 180 || degrees == 270) {
                parameters.setRotation(degrees)
            } else {
                logger_e(TAG, "Rotation can only be 0, 90, 180 or 270.  rotation= $degrees")
//                CrashManager.reportException(IllegalArgumentException("Rotation can only be 0, 90, 180 or 270.  rotation= $degrees"))
            }

            mCamera.setParameters(parameters)
        }

        val info = Camera.CameraInfo()
        logger_i(
            TAG,
            "default display orientation:${
                Camera.getCameraInfo(
                    Camera.CameraInfo.CAMERA_FACING_BACK,
                    info
                )
            }"
        )

        val size = parameters?.previewSize
        if (size != null) {
            val previewWidth = size.width
            val previewHeight = size.height

            val widthRatio = screenWidth * 1.0f / previewWidth
//            var heightRatio = screenHeight * 1.0f / previewHeight

            var lastWidth: Int = screenWidth
            var lastHeight: Int = (previewHeight * widthRatio + 0.5f).toInt()
            if (lastHeight < screenHeight) {

                val previewRatio = if (previewHeight < previewWidth) {
                    previewHeight * 1.0f / previewWidth
                } else {
                    previewWidth * 1.0f / previewHeight
                }
                val screenRatio = screenWidth * 1.0f / screenHeight
                logger_e(TAG, "previewRatio = $previewRatio ,screenRatio = $screenRatio")
                when {
                    previewRatio != screenRatio -> {
                        lastWidth = screenWidth
                        lastHeight = (screenWidth / previewRatio).toInt()

                    }
                    else -> {
                        lastHeight = screenHeight
                        lastWidth = screenWidth
                    }
                }
            }

            logger_i(TAG, "surface size - width:$width,height:$height")
            logger_i(TAG, "preview size - width:$previewWidth,height:$previewHeight")
            logger_i(TAG, "screen size - width:$screenWidth,height:$screenHeight")
            logger_i(TAG, "last size - width:$lastWidth,height:$lastHeight")

            if (lastWidth > screenWidth && lastHeight > screenHeight) {
                val widthPercent = screenWidth * 1.0f / lastWidth
                val heightPercent = screenHeight * 1.0f / lastHeight
                val maxPercent = Math.max(widthPercent, heightPercent)
                lastHeight = (lastHeight * maxPercent).toInt()
                lastWidth = (lastWidth * maxPercent).toInt()
            } else if (lastWidth < screenWidth) {
                val widthPercent = screenWidth * 1.0f / lastWidth
                lastHeight = (lastHeight * widthPercent).toInt()
                lastWidth = (lastWidth * widthPercent).toInt()
            } else if (lastHeight < screenHeight) {
                val heightPercent = screenHeight * 1.0f / lastHeight
                lastHeight = (lastHeight * heightPercent).toInt()
                lastWidth = (lastWidth * heightPercent).toInt()
            }

            activity.runOnUiThread {
                val params = FrameLayout.LayoutParams(lastWidth, lastHeight)
                if (screenOrientation == SCREEN_PORTRAIT && cameraId == CAMERA_FRONT) {
                    params.leftMargin = (screenWidth - lastWidth) / 2
                } else {
                    params.rightMargin = (screenWidth - lastWidth) / 2
                }
                params.bottomMargin = screenHeight - lastHeight
                textureView?.layoutParams = params

                if (screenOrientation == SCREEN_PORTRAIT) {
                    if (cameraId == CAMERA_FRONT) {
                        textureView?.rotation = 180f
                    } else if (textureView?.rotation != 0f) {
                        textureView?.rotation = 360f
                    }
                }
            }
        }

        mCamera.setPreviewTexture(surface)
        mCamera.startPreview()

        return cameraId
    }


    override fun takePicture(file: File, callback: (success: Boolean, filePath: File) -> Unit) {
        mCamera.take { success: Boolean, data: ByteArray? ->
            if (!success || data == null) {
                callback(false, file)
                return@take
            }
            ThreadPoolUtil.executor("picture") {
                try {
                    file.createNewFile()
                    val fos = FileOutputStream(file)
                    fos.use {
                        it.write(data)
                    }
                } catch (e: Exception) {
                    logger_i(
                        TAG, "拍照结果 - 失败:${file.absolutePath}," +
                                "file length:${if (file.exists()) file.length() else 0}"
                    )
                    callback(false, file)
                    return@executor
                }
                logger_i(
                    TAG, "拍照结果 - 成功:${file.absolutePath}," +
                            "file length:${if (file.exists()) file.length() else 0}"
                )
                callback(false, file)
            }
        }
    }

    private fun callback(isSuccess: Boolean, file: File) {
        MainHandler.post {
            callback(isSuccess, file)
        }
    }

    override fun close() {
        mCamera.close()
        textureView = null
        mSurface = null
    }

    override fun autoFocus() {
        mCamera.autoFocus()
    }

    override fun switchFlash(): Boolean {
        return mCamera.switchFlash()
    }

    override fun openLight() {

    }

    override fun switchCamera(callback: (curCameraId: Int) -> Unit) {
        ThreadPoolUtil.executor("switchCamera") {
            val cameraId = mCameraId
            mCamera.close()

            val result = if (cameraId == CAMERA_BACK) {
                openCamera(CAMERA_FRONT)
            } else {
                openCamera(CAMERA_BACK)
            }

            setCurrentCameraId(result)
            activity.runOnUiThread {
                val cameraId = if (result == CAMERA_BACK) FACING_BACK else FACING_FRONT
                callback(cameraId)
            }
        }
    }

    override fun hasBackCamera(): Boolean {
        return mCamera.hasBackCamera()
    }

    override fun hasFrontCamera(): Boolean {
        return mCamera.hasFrontCamera()
    }

    override fun getNumberOfCameras(): Int {
        return CameraOneUtils.getNumberOfCameras()
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mSurface = surface
        mWidth = width
        mHeight = height
        defaultFacing = cameraOrientation
        val targetCameraId = when {
            defaultFacing == FACING_BACK && hasBackCamera() -> CAMERA_BACK
            defaultFacing == FACING_FRONT && hasFrontCamera() -> CAMERA_FRONT
            else -> throw IllegalStateException("Back and front camera are unavailable")
        }
        mCameraId = openCamera(targetCameraId)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        mSurface = surface
        mWidth = width
        mHeight = height
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        close()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        mSurface = surface
    }

    override fun isFront(): Boolean {
        return mCameraId == CAMERA_FRONT
    }

    @Synchronized
    private fun setCurrentCameraId(cameraId: Int) {
        mCameraId = cameraId
    }

    /**
     * 获取照相机旋转角度
     */
    private fun getCameraDegrees(activity: Activity, cameraId: Int): Int {
        var rotateAngle = 0
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotateAngle = (info.orientation + degrees) % 360
            rotateAngle = (360 - rotateAngle) % 360 // compensate the mirror
        } else { // back-facing
            rotateAngle = (info.orientation - degrees + 360) % 360
        }

        if (screenOrientation == SCREEN_PORTRAIT) {
            if (cameraId == CAMERA_FRONT) {
                rotateAngle = (rotateAngle + 180) % 360
            }
        }
        return rotateAngle
    }
}