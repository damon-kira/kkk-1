package com.colombia.credit.module.process.kyc

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.KycInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject


// 上传身份证信息
class KycRepository @Inject constructor() : BaseProcessRepository<KycInfo>() {

    fun uploadImage(path: String, type: Int) =
        ApiServiceLiveDataProxy.request {
            val part = MultipartBody.Builder().addPart(createFileRequestBody(File(path))).build()
            apiService.uploadKycImage(part)
        }

    override fun uploadInfo(info: IBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadKycInfo(body)
    }

    override fun getCacheClass(): Class<KycInfo> {
        return KycInfo::class.java
    }

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_IDENTITY_INFO_INPUT
}