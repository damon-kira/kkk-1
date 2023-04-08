package com.colombia.credit.module.process.face

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.module.process.BaseProcessViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 上传活体照片
@HiltViewModel
class FaceViewModel @Inject constructor(private val repository: FaceRepository) :
    BaseProcessViewModel() {

    override fun uploadInfo(info: IReqBaseInfo) {
        showloading()
        mUploadLiveData.addSourceLiveData(repository.uploadInfo(info)) {

        }
    }

    override fun saveCacheInfo(info: IReqBaseInfo) {
    }

    override fun getCacheInfo(): IReqBaseInfo? {
        return null
    }

    override fun removeCacheInfo() {
    }
}