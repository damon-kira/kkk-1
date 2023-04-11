package com.colombia.credit.module.process.work

import androidx.lifecycle.LiveData
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqWorkInfo
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传工作信息
class WorkInfoRepository @Inject constructor() : BaseProcessRepository<ReqWorkInfo>() {

    override fun getCacheClass(): Class<ReqWorkInfo> = ReqWorkInfo::class.java

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_WORK_INFO_INPUT

    override fun uploadInfo(info: IReqBaseInfo) =
        ApiServiceLiveDataProxy.request(RspResult::class.java) {
            apiService.uploadWorkInfo(createRequestBody(GsonUtil.toJson(info).orEmpty()))
        }

}