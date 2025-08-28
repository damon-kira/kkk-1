package com.kira.learning.module.process.contact

import androidx.lifecycle.LiveData
import com.kira.learning.bean.req.ReqContactInfo
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.res.RspContactInfo
import com.kira.learning.bean.res.RspResult
import com.kira.learning.manager.SharedPrefKeyManager
import com.kira.learning.module.process.BaseProcessRepository
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