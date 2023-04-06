package com.colombia.credit.module.process.kyc

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.KycOcrInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传身份证信息
@HiltViewModel
class KycViewModel @Inject constructor(private val repository: KycRepository) :
    BaseProcessViewModel() {

    val imageLivedata = generatorLiveData<BaseResponse<KycOcrInfo>>()
    fun uploadImage(path: String, type: Int) {
        val json = GsonUtil.toJson(KycOcrInfo())
        imageLivedata.postValue(BaseResponse(0, json.orEmpty(), ""))
        imageLivedata.addSourceLiveData(repository.uploadImage(path, type)) {
            imageLivedata.postValue(it)
        }
    }

    override fun uploadInfo(info: IBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
        }
    }

    override fun saveCacheInfo(info: IBaseInfo) {
        repository.saveCacheInfo(info)
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }

    override fun getCacheInfo(): IBaseInfo? = repository.getCacheInfo()
}