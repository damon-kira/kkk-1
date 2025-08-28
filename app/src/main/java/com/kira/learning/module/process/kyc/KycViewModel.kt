package com.kira.learning.module.process.kyc

import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.req.ReqKycInfo
import com.kira.learning.bean.res.KycOcrInfo
import com.kira.learning.bean.res.RspKycInfo
import com.kira.learning.manager.SharedPrefKeyManager
import com.kira.learning.module.process.BaseProcessViewModel
import com.kira.learning.module.service.SerManager
import com.kira.learning.utils.GPInfoUtils
import com.util.lib.ImageInfoUtil
import com.kira.learning.utils.image.annotations.PicType
import com.common.lib.net.bean.BaseResponse
import javax.inject.Inject

// 上传身份证信息
class KycViewModel @Inject constructor(private val repository: KycRepository) :
    BaseProcessViewModel() {
    private val _imageLiveData = generatorLiveData<BaseResponse<KycOcrInfo>>()
    val imageLivedata = _imageLiveData

    var mImageType = -1

    var mFrontException = false
    var mBackException = false

    fun uploadImage(path: String, type: Int) {
        showloading()
        mImageType = type
        _imageLiveData.addSourceLiveData(repository.uploadImage(path, type)) {
            hideLoading()
            if (it.isSuccess()) {
                updateResult(type, false)
                val imageInfo = ImageInfoUtil.getExifInfo(path).orEmpty()
                val key =
                    if (type == PicType.PIC_FRONT) SharedPrefKeyManager.KEY_IMAGE_FRONT else SharedPrefKeyManager.KEY_IMAGE_BACK
                ImageInfoUtil.saveInfo(key, imageInfo)
            } else {
                updateResult(type, true)
            }
            _imageLiveData.postValue(it)
        }
    }

    private fun updateResult(type: Int, result: Boolean) {
        if (type == PicType.PIC_FRONT) {
            mFrontException = result
        } else if (type == PicType.PIC_BACK) {
            mBackException = result
        }
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                it.getData()?.let {data ->
                    mInfoLiveData.postValue(data)
                }
            } else {
                mInfoLiveData.postValue(RspKycInfo())
            }
        }
    }

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        SerManager.uploadData()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                GPInfoUtils.saveTag(GPInfoUtils.TAG5)
                com.kira.learning.expand.mUserName = (info as ReqKycInfo).y6hQBtv.orEmpty()
            }
            mUploadLiveData.postValue(it)
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
        if (isUploadSuccess) {
            removeCacheInfo()
            return
        }
        repository.saveCacheInfo(info)
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        val cache = repository.getCacheInfo()
        if (cache?.isEmpty() == true || cache == null) return null
        return cache
    }
}