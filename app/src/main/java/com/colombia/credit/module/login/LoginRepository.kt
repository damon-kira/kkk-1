package com.colombia.credit.module.login

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

class LoginRepository @Inject constructor() : BaseRepository() {

    fun login(mobile: String, code: String) = ApiServiceLiveDataProxy.request {
        val jobj = JsonObject()
        jobj.addProperty("mobile", mobile)
        jobj.addProperty("code", code)
        apiService.login(RequestBody.create(MEDIA_TYPE, jobj.toString()))
    }

    fun reqSmsCode(mobile: String) = ApiServiceLiveDataProxy.request {
        val jobj = JsonObject()
        jobj.addProperty("mobile", mobile)
        apiService.getSmsCode(RequestBody.create(MEDIA_TYPE, jobj.toString()))
    }
}