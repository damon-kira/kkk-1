package com.colombia.credit.module.process.personalinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colombia.credit.LoanApplication
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqPersonalInfo
import com.colombia.credit.bean.resp.IRspBaseInfo
import com.colombia.credit.bean.resp.RspPersonalInfo
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.colombia.credit.util.FileUtils
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传基本信息
class PersonalRepository @Inject constructor() : BaseProcessRepository<RspPersonalInfo, ReqPersonalInfo>() {

    override fun uploadInfo(info: IReqBaseInfo) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            val body = createRequestBody(GsonUtil.toJson(info).orEmpty())
            apiService.uploadPersonalInfo(body)
        }

    override fun getInfo(): LiveData<BaseResponse<RspPersonalInfo>> =
        ApiServiceLiveDataProxy.request(RspPersonalInfo::class.java) {
            apiService.getPersonalInfo()
        }

    override fun getCacheClass(): Class<ReqPersonalInfo> {
        return ReqPersonalInfo::class.java
    }

    override fun getCacheKey(): String {
        return SharedPrefKeyManager.KEY_BASE_INFO_INPUT
    }

    fun getAddrInfo(): LiveData<String> {
        val liveData = MutableLiveData<String>()
        FileUtils.readAssets(LoanApplication.getAppContext(), "clbyaddr.txt") {
            liveData.postValue(it)
        }
        return liveData
    }
}