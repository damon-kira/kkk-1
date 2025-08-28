package com.kira.learning.module.process.personalinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kira.learning.LoanApplication
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.req.ReqPersonalInfo
import com.kira.learning.bean.res.RspPersonalInfo
import com.kira.learning.bean.res.RspResult
import com.kira.learning.manager.SharedPrefKeyManager
import com.kira.learning.module.process.BaseProcessRepository
import com.kira.learning.utils.FileUtils
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