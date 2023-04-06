package com.colombia.credit.module.process.bank

import com.colombia.credit.bean.resp.BankInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传银行卡信息
class BankInfoRepository @Inject constructor() : BaseProcessRepository<BankInfo>() {

    private val CACHE_KEY = SharedPrefKeyManager.KEY_BANKCARD_INFO_INPUT

    override fun getCacheClass(): Class<BankInfo> = BankInfo::class.java

    override fun uploadInfo(info: IBaseInfo) = ApiServiceLiveDataProxy.request {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadBankInfo(body)
    }

    override fun getCacheKey(): String = CACHE_KEY
}