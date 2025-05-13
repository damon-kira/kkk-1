package com.kira.learning.module.process.kyc

import androidx.lifecycle.LiveData
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.req.ReqKycInfo
import com.kira.learning.bean.resp.KycOcrInfo
import com.kira.learning.bean.resp.RspKycInfo
import com.kira.learning.bean.resp.RspResult
import com.kira.learning.di.UploadApiService
import com.kira.learning.manager.SharedPrefKeyManager
import com.kira.learning.module.process.BaseProcessRepository
import com.kira.learning.net.ApiService
import com.kira.learning.util.image.annotations.PicType
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject


// 上传身份证信息
class KycRepository @Inject constructor(@UploadApiService private val uploadApiService: ApiService) :
    BaseProcessRepository<RspKycInfo, ReqKycInfo>() {

    fun uploadImage(path: String, @PicType type: Int) =
        ApiServiceLiveDataProxy.request(KycOcrInfo::class.java) {
            val file = File(path)
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            builder.addFormDataPart("YGqDffupPG", file.name, createFileRequestBody(file))
            val tempType = if (type == PicType.PIC_FRONT) "FRONT" else "BACK"
            builder.addFormDataPart("P6KkaG04zb", tempType)
            uploadApiService.uploadKycImage(builder.build().part(0), tempType)
        }

    override fun getInfo(): LiveData<BaseResponse<RspKycInfo>> =
        ApiServiceLiveDataProxy.request(RspKycInfo::class.java) {
            apiService.getKycInfo()
        }

    override fun uploadInfo(info: IReqBaseInfo) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
            apiService.uploadKycInfo(body)
        }

    override fun getCacheClass(): Class<ReqKycInfo> {
        return ReqKycInfo::class.java
    }

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_IDENTITY_INFO_INPUT
}