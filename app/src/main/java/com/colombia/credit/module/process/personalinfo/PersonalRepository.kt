package com.colombia.credit.module.process.personalinfo

import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqPersonalInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传基本信息
class PersonalRepository @Inject constructor() : BaseProcessRepository<ReqPersonalInfo>() {

    override fun uploadInfo(info: IReqBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadPersonalInfo(body)
    }

    override fun getCacheClass(): Class<ReqPersonalInfo> {
        return ReqPersonalInfo::class.java
    }

    override fun getCacheKey(): String {
        return SharedPrefKeyManager.KEY_BASE_INFO_INPUT
    }
}