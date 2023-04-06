package com.colombia.credit.module.process.contact

import com.colombia.credit.bean.resp.ContactInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.KycInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传联系人
class ContactRepository @Inject constructor() : BaseProcessRepository<ContactInfo>() {

    private val CACHE_KEY = SharedPrefKeyManager.KEY_CONTACT_INFO_INPUT

    override fun uploadInfo(info: IBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadContactInfo(body)
    }


    override fun getCacheClass(): Class<ContactInfo> {
        return ContactInfo::class.java
    }

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_CONTACT_INFO_INPUT
}