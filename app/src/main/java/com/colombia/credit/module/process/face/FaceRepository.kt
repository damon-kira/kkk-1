package com.colombia.credit.module.process.face

import com.colombia.credit.bean.resp.FaceInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class FaceRepository @Inject constructor() : BaseProcessRepository<FaceInfo>() {

    override fun getCacheClass(): Class<FaceInfo> = FaceInfo::class.java

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_FACE_INFO

    override fun uploadInfo(info: IBaseInfo) = ApiServiceLiveDataProxy.request {
        val path = (info as FaceInfo).path.orEmpty()
        val file = File(path)
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("image", file.name, createFileRequestBody(file))
        apiService.uploadFaceImage(builder.build())
    }
}