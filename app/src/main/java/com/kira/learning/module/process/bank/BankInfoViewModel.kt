package com.kira.learning.module.process.bank

import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.module.process.BaseProcessViewModel
import com.kira.learning.utils.GPInfoUtils
import javax.inject.Inject

// 上传银行卡信息
class BankInfoViewModel @Inject constructor(private val repository: BankInfoRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                GPInfoUtils.saveTag(GPInfoUtils.TAG4)
            }
            mUploadLiveData.postValue(it)
        }
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@addSourceLiveData
                val hQYeCtjtJh = data.hQYeCtjtJh
                if (hQYeCtjtJh == null || hQYeCtjtJh.isEmpty()) return@addSourceLiveData
                mInfoLiveData.postValue(data)
            }
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
        return repository.getCacheInfo()
    }
}