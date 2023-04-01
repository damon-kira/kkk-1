package com.camera.lib

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.hardware.Camera.getCameraInfo
import android.view.SurfaceHolder
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import java.util.*


class CameraOneUtils {

    companion object {
        private const val TAG = "ICamera"

        const val CAMERA_BACK = Camera.CameraInfo.CAMERA_FACING_BACK
        const val CAMERA_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT
        const val CAMERA_NULL = -1

        fun getNumberOfCameras(): Int {
            return Camera.getNumberOfCameras()
        }
    }

    private var mCamera: Camera?= null
    private var mInPreview: Boolean = false
    private var mPreviewCallback: Camera.PreviewCallback? = null

    fun open(openCameraId: Int): Int {
        if(openCameraId != CAMERA_BACK && openCameraId != CAMERA_FRONT){
            return CAMERA_NULL
        }
        var result = if(openCameraId == CAMERA_BACK){
            Camera.CameraInfo.CAMERA_FACING_BACK
        }else{
            Camera.CameraInfo.CAMERA_FACING_FRONT
        }

        var camera = openCameraInternal(result)
        if(camera == null){
            result = if(result == CAMERA_BACK){
                Camera.CameraInfo.CAMERA_FACING_FRONT
            }else{
                Camera.CameraInfo.CAMERA_FACING_BACK
            }
            camera = openCameraInternal(result)
        }

        if(camera == null){
            result = CAMERA_NULL
        }

        logger_i(TAG,"打开摄像头是否成功:${camera != null},cameraId:$result")

        if(camera == null){
            mCamera = null
            return result
        }

        mCamera = camera

        try {
            camera.setPreviewCallback { data, camera ->
                mInPreview = true
                mPreviewCallback?.onPreviewFrame(data, camera)

            }
        }catch (e: Exception){
            logger_i(TAG,"open camera failed,setPreviewCallback Exception:$e")
            result = CAMERA_NULL
        }

        return result
    }

    fun getParameters(): Camera.Parameters? {
        try{
            return mCamera?.parameters
        }catch (e: Exception){
            logger_e(TAG,"获取相机参数失败:$e")
        }

        return null
    }

    fun setParameters(parameters: Camera.Parameters?): Boolean {
        if(parameters == null){
            return false
        }
        try{
            mCamera?.parameters = parameters
            return true
        }catch (e: Exception){
            logger_e(TAG,"获取相机参数失败:$e")
        }
        return false
    }

    fun take(callback: (success: Boolean,data: ByteArray?) -> Unit) {
        if(!isInPreview()){
            callback(false,null)
            return
        }
        try {
            mCamera?.takePicture(null,null,null) { data, camera ->
                stopPreview()
                callback(true,data)
                startPreview()
            }
        }catch (e:Exception){
            logger_i(TAG,"take picture error e = $e")
            callback(false,null)
//            EventAgent.onEvent(ConstantDot.PAGE_CAMERA,ConstantDot.EVENT_TAKE_PHOTO_CRASH)
        }
    }

    fun close(){
        mInPreview = false
        try {
            mCamera?.setPreviewCallback(null)
            mCamera?.stopPreview()
            mCamera?.release()
        }catch (e: Exception){
            logger_e(TAG,"close 相机出错:$e")

        }
        mCamera = null
    }


    fun setPreviewDisplay(holder: SurfaceHolder): Boolean{
        try {
            mCamera?.setPreviewDisplay(holder)
            return true
        }catch (e: Exception){
            logger_e(TAG,"setPreviewTexture出错:$e")
        }
        return false
    }

    fun setPreviewTexture(texture: SurfaceTexture?): Boolean{
        try {
            mCamera?.setPreviewTexture(texture)
            return true
        }catch (e: Exception){
            logger_e(TAG,"setPreviewTexture出错:$e")
        }
        return false
    }

    fun startPreview(): Boolean{
        try {
            mCamera?.startPreview()
            return true
        }catch (e: Exception){
            logger_e(TAG,"startPreview出错:$e")
        }
        return false
    }

    fun stopPreview(): Boolean{
        try {
            mCamera?.stopPreview()
            return true
        }catch (e: Exception){
            logger_e(TAG,"stopPreview出错:$e")
        }
        return false
    }

    private fun isInPreview(): Boolean {
        return mCamera != null && mInPreview
    }

    fun autoFocus() {
        val camera = mCamera ?: return
        try {
            val parameters = camera.parameters
            val focusModes = parameters.supportedFocusModes
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                camera.cancelAutoFocus()
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                camera.parameters = parameters
                camera.autoFocus(null)
            }
        } catch (e: Exception) {
            logger_e(TAG,"自动对焦失败:$e")
        }
    }

    fun switchFlash(): Boolean {
        val camera = mCamera ?: return false
        var flag = false
        try {
            val parameters = camera.parameters

            if (parameters?.flashMode == Camera.Parameters.FLASH_MODE_ON) {
                parameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
                flag = false
            } else {
                parameters?.flashMode = Camera.Parameters.FLASH_MODE_ON
                flag = true
            }
            camera.parameters = parameters
        } catch (e: Exception) {
            logger_e(TAG, "闪光灯失败:$e")
        }
        return flag
    }

    fun setDisplayOrientation(degrees: Int){
        try {
            mCamera?.setDisplayOrientation(degrees)
        }catch (e: Exception){
            logger_e(TAG,"setDisplayOrientation失败:$e")
        }
    }

    /**
     * 打开指定的摄像头
     * @param cameraId cameraId
     */
    private fun openCameraInternal(cameraId: Int): Camera? {
        var camera: Camera? = null
        try {
            val hasBackCamera = hasBackCamera()
            val hasFrontCamera = hasFrontCamera()

            if (cameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                if (hasBackCamera) {
                    camera = Camera.open(cameraId)
                    logger_i(TAG, "打开后置摄像头")
                }
            } else if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                if (hasFrontCamera) {
                    camera = Camera.open(cameraId)
                    logger_i(TAG, "打开前置摄像头")
                }
            }
        }catch (e: Exception){
            logger_i(TAG,"打开相机失败，可能是因为其他应用锁定了camera:$e")
        }

        return camera
    }

    fun getNearestRatioSize(para: Camera.Parameters,
                            screenWidth: Int, screenHeight: Int): Camera.Size? {
        val supportedSize = para.supportedPreviewSizes ?: return null

        val buffer = StringBuffer()
        for(size in supportedSize){
            buffer.append("${size.width},${size.height}\n")
        }
        logger_i(TAG,"supported size :$buffer")
        for (tmp in supportedSize) {
            if(tmp.width == screenWidth && tmp.height == screenHeight){
                return tmp
            }
            if (tmp.width == 1920 && tmp.height == 1080) {
                return tmp
            }
            if (tmp.width == 640 && tmp.height == 480) {
                return tmp
            }
        }
        supportedSize.sortWith(Comparator { lhs, rhs ->
            val diff1 = ((1000 * Math.abs(lhs.width / lhs.height.toFloat() - screenWidth / screenHeight.toFloat())).toInt() shl 16) - lhs.width
            val diff2 = ((1000 * Math.abs(rhs.width / rhs.height.toFloat() - screenWidth / screenHeight.toFloat())).toInt() shl 16) - rhs.width

            diff1 - diff2
        })
        return supportedSize[0]
    }

    /**
     * 是否有后置摄像头
     *
     * @return 是否有后置摄像头
     */
    fun hasBackCamera(): Boolean {
        val numberOfCameras = Camera.getNumberOfCameras()
        if (numberOfCameras <= 0) {
            return false
        }
        try {
            val cameraInfo = Camera.CameraInfo()
            for (i in 0 until numberOfCameras) {
                getCameraInfo(i, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    return true
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, "has back camera :$e")
        }

        return false
    }

    /**
     * 是否有前置摄像头
     *
     * @return 是否有前置摄像头
     */
    fun hasFrontCamera(): Boolean {
        val numberOfCameras = Camera.getNumberOfCameras()
        if (numberOfCameras <= 0) {
            return false
        }
        try {
            val cameraInfo = Camera.CameraInfo()
            for (i in 0 until numberOfCameras) {
                getCameraInfo(i, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    return true
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, "has front camera :$e")
        }

        return false
    }

    fun getBestPictureSize(parameters: Camera.Parameters): Camera.Size? {
        try {
            val list = parameters.supportedPictureSizes

            val buffer = StringBuilder()
            for (size in list) {
                buffer.append("${size.width}X${size.height}、")
            }
            logger_i(TAG, "相机支持的图片大小尺寸排序前$buffer")

            list.sortWith(Comparator<Camera.Size> { o1, o2 ->
                val widthDelta = o2.width - o1.width
                val heightDelta = o2.height - o1.height

                return@Comparator if (widthDelta == 0) heightDelta else widthDelta

            })

            val sbuffer = StringBuilder()
            for (size in list) {
                sbuffer.append("${size.width}X${size.height}、")
            }
            logger_i(TAG, "相机支持的图片大小尺寸排序后$sbuffer")

            val previewSize = parameters.previewSize ?: return null

            val ratio = previewSize.width * 1.0f / previewSize.height
            for(size in list){
                if(ratio == size.width * 1.0f / size.height){
                    return size
                }
            }

            return list[0]
        }catch (e: Exception){
            logger_e(TAG,"获取支持的图片大小失败:$e")
        }
        return null
    }
}