package com.colombia.credit.module.process.kyc

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.bean.resp.KycOcrInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.ImageInfoUtil
import com.colombia.credit.util.image.annotations.PicType
import com.common.lib.net.bean.BaseResponse
import com.util.lib.ThreadPoolUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传身份证信息
@HiltViewModel
class KycViewModel @Inject constructor(private val repository: KycRepository) :
    BaseProcessViewModel() {
    private val _imageLiveData = generatorLiveData<BaseResponse<KycOcrInfo>>()
    val imageLivedata = _imageLiveData

    var mImageType = -1

    fun uploadImage(path: String, type: Int) {
        showloading()
        mImageType = type
        _imageLiveData.addSourceLiveData(repository.uploadImage(path, type)) {
            hideLoading()
            if (it.isSuccess()) {
                val imageInfo = ImageInfoUtil.getExifInfo(path).orEmpty()
                val key = if (type == PicType.PIC_FRONT) SharedPrefKeyManager.KEY_IMAGE_FRONT else SharedPrefKeyManager.KEY_IMAGE_BACK
                ImageInfoUtil.saveInfo(key, imageInfo)
            }
            _imageLiveData.postValue(it)
        }
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                it.getData()?.jmWujylO6j ?: return@addSourceLiveData
                mInfoLiveData.postValue(it.getData())
            }
        }
    }

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                com.colombia.credit.expand.mUserName = (info as ReqKycInfo).y6hQBtv.orEmpty()
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

    override fun getCacheInfo(): IReqBaseInfo? = repository.getCacheInfo()
}