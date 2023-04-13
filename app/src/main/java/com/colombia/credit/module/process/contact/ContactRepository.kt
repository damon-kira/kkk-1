package com.colombia.credit.module.process.contact

import androidx.lifecycle.LiveData
import com.colombia.credit.bean.req.ReqContactInfo
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.resp.RspContactInfo
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传联系人
class ContactRepository @Inject constructor() : BaseProcessRepository<RspContactInfo, ReqContactInfo>() {

    override fun uploadInfo(info: IReqBaseInfo) = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadContactInfo(body)
    }

    override fun getInfo(): LiveData<BaseResponse<RspContactInfo>> =
        ApiServiceLiveDataProxy.request(RspContactInfo::class.java) {
            apiService.getContactInfo()
        }

    override fun getCacheClass(): Class<ReqContactInfo> {
        return ReqContactInfo::class.java
    }

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_CONTACT_INFO_INPUT
}