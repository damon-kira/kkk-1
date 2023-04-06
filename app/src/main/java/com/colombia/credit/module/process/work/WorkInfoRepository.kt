package com.colombia.credit.module.process.work

import androidx.lifecycle.LiveData
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.WorkInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.colombia.credit.module.process.BaseProcessRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import javax.inject.Inject

// 上传工作信息
class WorkInfoRepository @Inject constructor() : BaseProcessRepository<WorkInfo>() {

    override fun getCacheClass(): Class<WorkInfo> = WorkInfo::class.java

    override fun getCacheKey(): String = SharedPrefKeyManager.KEY_WORK_INFO_INPUT

    override fun uploadInfo(info: IBaseInfo): LiveData<BaseResponse<String>> =
        ApiServiceLiveDataProxy.request {
            apiService.uploadWorkInfo(createRequestBody(GsonUtil.toJson(info).orEmpty()))
        }

}