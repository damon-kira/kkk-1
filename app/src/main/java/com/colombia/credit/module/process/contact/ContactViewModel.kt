package com.colombia.credit.module.process.contact

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传联系人信息
@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ContactRepository) :
    BaseProcessViewModel() {


    override fun uploadInfo(info: IBaseInfo) {
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            mUploadLiveData.postValue(it)
        }
    }

    override fun saveCacheInfo(info: IBaseInfo) {
        repository.saveCacheInfo(info)
    }

    override fun removeCacheInfo() = run { repository.removeCacheInfo() }

    override fun getCacheInfo(): IBaseInfo? {
        return repository.getCacheInfo()
    }
}