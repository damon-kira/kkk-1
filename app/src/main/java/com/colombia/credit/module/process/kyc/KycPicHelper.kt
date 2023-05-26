package com.colombia.credit.module.process.kyc

import com.camera.lib.CameraOneUtils
import com.colombia.credit.dialog.PicImageDialog
import com.colombia.credit.util.image.ImageObtainHelper
import com.colombia.credit.util.image.annotations.ImageType
import com.colombia.credit.util.image.annotations.PicType
import com.colombia.credit.util.image.annotations.PickRange
import com.colombia.credit.util.image.callback.AdapterCallback
import com.colombia.credit.util.image.data.CompressResult
import com.common.lib.base.BaseActivity
import com.util.lib.expand.deleteFiles
import java.io.File

class KycPicHelper {

    companion object {
        private const val TYPE_NULL = 0X10
        private const val TYPE_CAMERA = 0X11
        private const val TYPE_ALBUM = 0X12
    }

    var mCurrPicType = 0 // 当前选择的照片，PicType.PIC_FRONT=2 PicType.PIC_BACK=1

    var mSelectType = TYPE_NULL // 相机 or 相册

    private val mKycInfo by lazy {
        KycTempInfo()
    }

    var mResultListener: ((filePath: String?, picType: Int) -> Unit)? = null

    fun showPicImageModeDialog(
        activity: BaseActivity,
        @PicType picType: Int
    ) {
        mSelectType = TYPE_NULL
        PicImageDialog(activity)
            .setOnImageClick({
                openCamera(activity, picType)
            }, {
                pickPicture(activity, picType)
            })
            .show()
    }

    fun openCamera(activity: BaseActivity, @PicType picType: Int) {
        mCurrPicType = picType
        mSelectType = TYPE_CAMERA
        val isFront = picType == PicType.PIC_FRONT
        val targetPath = if (isFront) mKycInfo.pathFront else mKycInfo.pathBack
        val captureFile = File(mKycInfo.getTempPath())
        ImageObtainHelper
            .createAgent(activity)
            .idPicture()
            .type(CameraOneUtils.CAMERA_BACK)
            .picType(picType)
            .fileToSave(captureFile)
            .then()
            .compress()
            .fileToSave(File(targetPath))
            .start(object : AdapterCallback<CompressResult>() {
                override fun onSuccess(result: CompressResult) {
                    deleteFiles(captureFile)
                    val filePath = result.uri?.path ?: return
                    mResultListener?.invoke(filePath, picType)
                }
            })
    }

    fun pickPicture(activity: BaseActivity, @PicType picType: Int) {
        mCurrPicType = picType
        mSelectType = TYPE_ALBUM
        val isFront = picType == PicType.PIC_FRONT
        val targetPath = if (isFront) mKycInfo.pathFront else mKycInfo.pathBack
        ImageObtainHelper
            .createAgent(activity)
            .pick()
            .range(PickRange.PICK_DICM)
            .type(ImageType.ALL)
            .then()
            .compress()
            .fileToSave(File(targetPath))
            .start(object : AdapterCallback<CompressResult>() {
                override fun onSuccess(result: CompressResult) {
                    val filePath = result.uri?.path ?: return
                    mResultListener?.invoke(filePath, picType)
                }
            })
    }

    fun autoShow(activity: BaseActivity, picType: Int){
        if (mSelectType == TYPE_CAMERA) {
            openCamera(activity, picType)
        } else if (mSelectType == TYPE_ALBUM) {
            showPicImageModeDialog(activity, picType)
        }
    }

    fun destroy() {
        mKycInfo.deleteImage()
    }
}