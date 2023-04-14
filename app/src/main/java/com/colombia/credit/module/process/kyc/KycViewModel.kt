package com.colombia.credit.module.process.kyc

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.resp.KycOcrInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.ocrExif
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传身份证信息
@HiltViewModel
class KycViewModel @Inject constructor(private val repository: KycRepository) :
    BaseProcessViewModel() {
    private val _imageLiveData = generatorLiveData<BaseResponse<KycOcrInfo>>()
    val imageLivedata = _imageLiveData
    fun uploadImage(path: String, type: Int) {
        val json = GsonUtil.toJson(KycOcrInfo())
        _imageLiveData.postValue(BaseResponse(0, json.orEmpty(), ""))
        _imageLiveData.addSourceLiveData(repository.uploadImage(path, type)) {
            _imageLiveData.postValue(it)
        }
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                mInfoLiveData.postValue(it.getData())
            }
        }
    }

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            isUploadSuccess = it.isSuccess()
            hideLoading()
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
        if (isUploadSuccess){
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