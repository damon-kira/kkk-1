package com.colombia.credit.module.login

import android.content.Context
import com.util.lib.net.WifiInfoUtil
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.util.GPInfoUtils
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import javax.inject.Inject

class LoginRepository @Inject constructor(@ApplicationContext private val context: Context) : BaseRepository() {

    fun loginSms(mobile: String, code: String, uuid: String) = ApiServiceLiveDataProxy.request {
        val jobj = JsonObject()
        jobj.addProperty("ykFmcq", mobile) // 手机号
        jobj.addProperty("AUMKWbddJp", code) // 验证码
        jobj.addProperty("swEXCj9g", "phone")
        jobj.addProperty("rVcbGK", uuid) // 来自验证码接口返回的uuid 【多个uuid就用,隔开】
        jobj.addProperty("YLFuyy", "0")
        jobj.addProperty("KP6X4M", "") // 邀请码
        jobj.addProperty("UvnKsvKDu",  WifiInfoUtil.getWifiName(context)) // WiFi
        jobj.addProperty("yzkPN4Z", GPInfoUtils.getAdid()) // gaid
        apiService.loginSms(createRequestBody(jobj.toString()))
    }

    fun reqSmsCode(mobile: String) = ApiServiceLiveDataProxy.request {
        val jobj = JsonObject()
        jobj.addProperty("ZF2FqPB8gZ", "phone") //类型（手机短信=phone,手机语音=phonesounds）
        jobj.addProperty("sbSsC", "1") // 验证码类型 （1=注册 3=忘记密码）
        jobj.addProperty("Y4WsSnBl", mobile)
        apiService.getSmsCode(RequestBody.create(MEDIA_TYPE, jobj.toString()))
    }
}