package com.colombia.credit.module.process.face

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传活体照片
@HiltViewModel
class FaceViewModel @Inject constructor(private val repository: FaceRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {

        }
    }

    override fun saveCacheInfo(info: IBaseInfo) {
    }

    override fun getCacheInfo(): IBaseInfo? {
        return null
    }

    override fun removeCacheInfo() {
    }
}