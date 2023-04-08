package com.colombia.credit.module.process.work

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传工作信息
@HiltViewModel
class WorkInfoViewModel @Inject constructor(private val repository: WorkInfoRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
        if (isUploadSuccess) return
        repository.saveCacheInfo(info)
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        return repository.getCacheInfo()
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }
}