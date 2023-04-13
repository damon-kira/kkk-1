package com.colombia.credit.module.upload

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IReqBaseInfo) {
        repository.uploadInfo()
    }

    override fun getInfo() {}

    override fun saveCacheInfo(info: IReqBaseInfo) {

    }

    override fun getCacheInfo(): IReqBaseInfo? = null

    override fun removeCacheInfo() {
    }
}