package com.kira.learning.module.process.bank

import androidx.lifecycle.LiveData
import com.kira.learning.bean.req.ReqBankInfo
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.resp.RspBankInfo
import com.kira.learning.bean.resp.RspResult
import com.kira.learning.manager.SharedPrefKeyManager
import com.kira.learning.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传银行卡信息
class BankInfoRepository @Inject constructor() : BaseProcessRepository<RspBankInfo, ReqBankInfo>() {

    private val CACHE_KEY = SharedPrefKeyManager.KEY_BANKCARD_INFO_INPUT

    override fun getCacheClass(): Class<ReqBankInfo> = ReqBankInfo::class.java

    override fun uploadInfo(info: IReqBaseInfo) = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
        apiService.uploadBankInfo(body)
    }

    override fun getCacheKey(): String = CACHE_KEY

    override fun getInfo(): LiveData<BaseResponse<RspBankInfo>> =
        ApiServiceLiveDataProxy.request(RspBankInfo::class.java) {
            apiService.getBankInfo()
        }
}