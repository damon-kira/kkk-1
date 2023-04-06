package com.colombia.credit.module.process.bank

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.livedata.observerNonSticky
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传银行卡信息
@HiltViewModel
class BankInfoViewModel @Inject constructor(private val repository: BankInfoRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            mUploadLiveData.postValue(it)
        }
    }

    override fun saveCacheInfo(info: IBaseInfo) {
        if (isUploadSuccess) return
        repository.saveCacheInfo(info)
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }

    override fun getCacheInfo(): IBaseInfo? {
        return repository.getCacheInfo()
    }
}