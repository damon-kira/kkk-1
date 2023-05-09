package com.colombia.credit.module.process.face

import android.content.Context
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.expand.deleteCameraCache
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.GPInfoUtils
import com.util.lib.ImageInfoUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// 上传活体照片
class FaceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: FaceRepository
) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                GPInfoUtils.saveTag(GPInfoUtils.TAG6)
                (info as ReqFaceInfo).path?.let { path ->
                    val imageExifInfo = ImageInfoUtil.getExifInfo(path).orEmpty()
                    ImageInfoUtil.saveInfo(SharedPrefKeyManager.KEY_IMAGE_FACE, imageExifInfo)
                }
            }
            deleteCameraCache(context)
            mUploadLiveData.postValue(it)
        }
    }

    override fun getInfo() {
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        return null
    }

    override fun removeCacheInfo() {
    }
}