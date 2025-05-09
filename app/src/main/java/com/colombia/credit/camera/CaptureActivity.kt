package com.colombia.credit.camera

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.camera.lib.BaseCameraManager
import com.camera.lib.CameraFactory
import com.camera.lib.CameraType
import com.camera.lib.ICamera
import com.colombia.credit.BuildConfig
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityCaptureBinding
import com.colombia.credit.util.image.annotations.PicType
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.image.commonCompressPicIO
import com.util.lib.log.logger_e
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class CaptureActivity : BaseActivity() {

    private val mBinding by binding<ActivityCaptureBinding>()

    companion object {
        const val KEY_CAPTURE_IMAGE_PATH = "keyCaptureImagePath"
        const val KEY_PICTURE_TYPE = "picture_type" //
        const val TYPE_FRONT = 1 // 正面
        const val TYPE_BACK = 2 // 反面

        private const val RECT_WIDTH = 8.6f
        private const val RECT_HEIGHT = 5.4f

        private const val PERSONAL_TO_TOP = 1f
        private const val PERSONAL_TO_BOTTOM = 1.8f
        private const val PERSONAL_TO_LEFT = 6.1f
        private const val PERSONAL_TO_RIGHT = 0.45f

        private const val PERSONAL_WIDTH = RECT_WIDTH - PERSONAL_TO_LEFT - PERSONAL_TO_RIGHT
        private const val PERSONAL_HEIGHT = RECT_HEIGHT - PERSONAL_TO_BOTTOM - PERSONAL_TO_TOP

    }

    private var mCameraManger: ICamera? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filePath = intent.getStringExtra(KEY_CAPTURE_IMAGE_PATH)
        val picType = intent.getIntExtra(KEY_PICTURE_TYPE, TYPE_FRONT)

        if (filePath.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        setContentView(mBinding.root)

        mBinding.aivBack.setBlockingOnClickListener {
            finish()
        }

        if (picType == PicType.PIC_FRONT) {
            mBinding.aivImageHint.setImageResource(R.drawable.image_front_hint)
            mBinding.tvImageHint.setText(R.string.capture_front_hint)
        } else {
            mBinding.aivImageHint.setImageResource(R.drawable.image_back_hint)
            mBinding.tvImageHint.setText(R.string.capture_back_hint)
        }

        mCameraManger = CameraFactory.invoke(
            CameraType.CameraOne,
            this,
            mBinding.cameraview,
            BaseCameraManager.FACING_BACK,
            BaseCameraManager.SCREEN_LANDSCAPE
        )

        mBinding.captureTakePhoto.setBlockingOnClickListener {
            val file = File(filePath)
            showLoading(false)
            mCameraManger?.takePicture(file) { success, f ->
                if (BuildConfig.DEBUG) {
                    Log.i(
                        TAG,
                        "拍照结果是否成功:$success,file:$f length:${if (f.exists()) f.length() else 0}"
                    )
                }
                hideLoading()

                if (success && f.exists()) {
                    val scannerRect = mBinding.scannerview.getScannerRect()
                    val rect = Rect().also {
                        it.left = (mBinding.scannerview.x + scannerRect.left).toInt()
                        it.top = (mBinding.scannerview.y + scannerRect.top).toInt()
                        it.right = scannerRect.right.toInt()
                        it.bottom = scannerRect.bottom.toInt()
                    }
                    val width = mBinding.cameraview.width
                    val height = mBinding.cameraview.height
                    val previewRect = Rect(0,0,width, height)
                    commonCompressPicIO(f.absolutePath, f.absolutePath) { finalPath ->
                        if (filePath != null) {
                            if (BuildConfig.DEBUG) {
                                logger_e(TAG, "success = $finalPath")
                            }
                            val result = intent
                            result.data = Uri.fromFile(File(finalPath))
                            setResult(Activity.RESULT_OK, result)
                            finish()
                        } else {
                            if (BuildConfig.DEBUG) {
                                logger_e(TAG, "error = $it")
                            }
                            finish()
                        }
                    }
//                    BitmapCrop.crop(this, f, rect, previewRect, mCameraManger?.isFront() ?: true) { finalFile ->
//                        if (finalFile != null) {
//                            if (BuildConfig.DEBUG) {
//                                logger_e(TAG, "success = ${finalFile.length()}")
//                            }
//                            val result = intent
//                            result.data = Uri.fromFile(finalFile)
//                            setResult(Activity.RESULT_OK, result)
//                            finish()
//                        } else {
//                            if (BuildConfig.DEBUG) {
//                                logger_e(TAG, "error = $it")
//                            }
//                            finish()
//                        }
//                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        mCameraManger?.close()
        mCameraManger = null
        super.onDestroy()
    }

    override fun finish() {
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: Exception) {
        }
        super.finish()
    }
}