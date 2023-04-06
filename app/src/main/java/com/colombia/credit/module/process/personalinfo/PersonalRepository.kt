package com.colombia.credit.module.process.personalinfo

import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.PersonalInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传基本信息
class PersonalRepository @Inject constructor() : BaseProcessRepository<PersonalInfo>() {

    override fun uploadInfo(info: IBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadPersonalInfo(body)
    }

    override fun getCacheClass(): Class<PersonalInfo> {
        return PersonalInfo::class.java
    }

    override fun getCacheKey(): String {
        return SharedPrefKeyManager.KEY_BASE_INFO_INPUT
    }
}