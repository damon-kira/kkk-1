package com.colombia.credit.module.process.face

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.bean.resp.RspFace
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.di.UploadApiService
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.colombia.credit.net.ApiService
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class FaceRepository @Inject constructor(@UploadApiService private val uploadApiService: ApiService) :
    BaseProcessRepository<RspFace, ReqFaceInfo>() {

    override fun getCacheClass(): Class<ReqFaceInfo> = ReqFaceInfo::class.java

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_FACE_INFO

    override fun getInfo(): LiveData<BaseResponse<RspFace>> = MutableLiveData()

    //{{app_url}}/userInfo/submitUserCheckFaceV2?headingCodeId=4fdas41&durationStay=2311&eventOrigin=06
    override fun uploadInfo(info: IReqBaseInfo) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            val path = (info as ReqFaceInfo).path.orEmpty()
            val file = File(path)
            val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("dSgQ8Tt", file.name, createFileRequestBody(file))
            val map = mutableMapOf<String, String>()
            map["TOCkGDeL"] = ""
            map["DGCb"] = "0"
            map["pVGq3"] = "02"
            uploadApiService.uploadFaceImage(builder.build().part(0), map)
        }
}