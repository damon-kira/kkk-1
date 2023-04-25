package com.colombia.credit.module.process.contact

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.module.service.SerManager
import javax.inject.Inject

// 上传联系人信息
class ContactViewModel @Inject constructor(private val repository: ContactRepository) :
    BaseProcessViewModel() {


    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        SerManager.startCon()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            mUploadLiveData.postValue(it)
        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
        if (isUploadSuccess){
            removeCacheInfo()
            return
        }
        repository.saveCacheInfo(info)
    }

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                it.getData()?.Rwfbhdu1 ?: return@addSourceLiveData
                mInfoLiveData.postValue(it.getData())
            }
        }
    }

    override fun removeCacheInfo() = run { repository.removeCacheInfo() }

    override fun getCacheInfo(): IReqBaseInfo? {
        return repository.getCacheInfo()
    }
}