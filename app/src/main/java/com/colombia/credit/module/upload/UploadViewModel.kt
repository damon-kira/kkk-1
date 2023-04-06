package com.colombia.credit.module.upload

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(private val repository: UploadRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IBaseInfo) {
        repository.uploadInfo()
    }

    override fun saveCacheInfo(info: IBaseInfo) {

    }

    override fun getCacheInfo(): IBaseInfo? = null

    override fun removeCacheInfo() {
    }
}