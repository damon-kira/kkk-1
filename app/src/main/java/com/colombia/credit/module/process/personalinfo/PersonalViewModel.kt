package com.colombia.credit.module.process.personalinfo

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传基本信息
@HiltViewModel
class PersonalViewModel @Inject constructor(private val repository: PersonalRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
        }
    }

    override fun saveCacheInfo(info: IBaseInfo) {
        if (isUploadSuccess) return
        repository.saveCacheInfo(info)
    }

    override fun getCacheInfo(): IBaseInfo? {
        return repository.getCacheInfo()
    }

    override fun removeCacheInfo() {
        repository.removeCacheInfo()
    }
}