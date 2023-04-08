package com.colombia.credit.module.process.face

import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.di.UploadApiService
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.colombia.credit.net.ApiService
import com.common.lib.net.ApiServiceLiveDataProxy
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class FaceRepository @Inject constructor(@UploadApiService private val uploadApiService: ApiService) : BaseProcessRepository<ReqFaceInfo>() {

    override fun getCacheClass(): Class<ReqFaceInfo> = ReqFaceInfo::class.java

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_FACE_INFO

    override fun uploadInfo(info: IReqBaseInfo) = ApiServiceLiveDataProxy.request {
        val path = (info as ReqFaceInfo).path.orEmpty()
        val file = File(path)
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("image", file.name, createFileRequestBody(file))
        uploadApiService.uploadFaceImage(builder.build())
    }
}