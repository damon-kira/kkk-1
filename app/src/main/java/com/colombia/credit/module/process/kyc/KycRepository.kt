package com.colombia.credit.module.process.kyc

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.di.UploadApiService
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.colombia.credit.net.ApiService
import com.colombia.credit.util.image.annotations.PicType
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject


// 上传身份证信息
class KycRepository @Inject constructor(@UploadApiService private val uploadApiService: ApiService) :
    BaseProcessRepository<ReqKycInfo>() {

    fun uploadImage(path: String,@PicType type: Int) =
        ApiServiceLiveDataProxy.request {
            val file = File(path)
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("gdkvsSDfvOrfds", file.name, createFileRequestBody(file))
            builder.addFormDataPart("asfvVdsainKsfv", if (type == PicType.PIC_FRONT) "FRONT" else "BACK")
            uploadApiService.uploadKycImage(builder.build())
        }

    override fun uploadInfo(info: IReqBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadKycInfo(body)
    }

    override fun getCacheClass(): Class<ReqKycInfo> {
        return ReqKycInfo::class.java
    }

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_IDENTITY_INFO_INPUT
}