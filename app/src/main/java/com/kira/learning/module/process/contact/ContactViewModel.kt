package com.kira.learning.module.process.contact

import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.module.process.BaseProcessViewModel
import com.kira.learning.utils.GPInfoUtils
import javax.inject.Inject

// 上传联系人信息
class ContactViewModel @Inject constructor(private val repository: ContactRepository) :
    BaseProcessViewModel() {


    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {
            hideLoading()
            isUploadSuccess = it.isSuccess()
            if (isUploadSuccess) {
                GPInfoUtils.saveTag(GPInfoUtils.TAG3)
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

    override fun getInfo() {
        mInfoLiveData.addSourceLiveData(repository.getInfo()) {
            if (it.isSuccess()) {
                val data = it.getData() ?: return@addSourceLiveData
                val Rwfbhdu1 = data.Rwfbhdu1
                if (Rwfbhdu1 == null || Rwfbhdu1.isEmpty()) return@addSourceLiveData
                mInfoLiveData.postValue(data)
            }
        }
    }

    override fun removeCacheInfo() = run { repository.removeCacheInfo() }

    override fun getCacheInfo(): IReqBaseInfo? {
        return repository.getCacheInfo()
    }
}