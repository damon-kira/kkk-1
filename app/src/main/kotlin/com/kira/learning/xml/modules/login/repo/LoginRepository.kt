package com.kira.learning.xml.modules.login.repo

import android.content.Context
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import com.kira.learning.di.BaseRepository
import com.kira.learning.bean.res.RspLoginInfo
import com.kira.learning.bean.res.RspSmsCode
import com.util.lib.net.WifiInfoUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import javax.inject.Inject

class LoginRepository @Inject constructor(@ApplicationContext private val context: Context) : BaseRepository() {

    fun loginSms(mobile: String, code: String, uuid: String) = ApiServiceLiveDataProxy.request(
        RspLoginInfo::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("ska3nXMv1K", "phone")
        jobj.addProperty("LuOZz", "0")
        jobj.addProperty("tSp3", "57|$mobile") // 手机号
        jobj.addProperty("lmjyS2Vw", code) // 验证码
        jobj.addProperty("gGvE", uuid) // 来自验证码接口返回的uuid 【多个uuid就用,隔开】
        jobj.addProperty("nYc91KC", "") // 邀请码
        jobj.addProperty("bhUyamS",  WifiInfoUtil.getWifiName(context)) // WiFi
        apiService.loginSms(createRequestBody(jobj.toString()))
    }

    fun reqSmsCode(type: String, mobile: String) = ApiServiceLiveDataProxy.request(RspSmsCode::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("ZF2FqPB8gZ", type) //类型（手机短信=phone,手机语音=phonesounds）
        jobj.addProperty("sbSsC", "1") // 验证码类型 （1=注册 3=忘记密码）
        jobj.addProperty("Y4WsSnBl", "57|$mobile")
        apiService.getSmsCode(RequestBody.create(MEDIA_TYPE, jobj.toString()))
    }
}