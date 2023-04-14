package com.colombia.credit.module.login

import android.content.Context
import com.util.lib.net.WifiInfoUtil
import com.colombia.credit.app.BaseRepository
import com.colombia.credit.bean.resp.RspLoginInfo
import com.colombia.credit.bean.resp.RspSmsCode
import com.colombia.credit.util.GPInfoUtils
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import javax.inject.Inject

class LoginRepository @Inject constructor(@ApplicationContext private val context: Context) : BaseRepository() {

    fun loginSms(mobile: String, code: String, uuid: String) = ApiServiceLiveDataProxy.request(RspLoginInfo::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("ska3nXMv1K", "phone")
        jobj.addProperty("LuOZz", "0")
        jobj.addProperty("tSp3", mobile) // 手机号
        jobj.addProperty("lmjyS2Vw", code) // 验证码
        jobj.addProperty("rVcbGK", uuid) // 来自验证码接口返回的uuid 【多个uuid就用,隔开】
        jobj.addProperty("nYc91KC", "") // 邀请码
        jobj.addProperty("bhUyamS",  WifiInfoUtil.getWifiName(context)) // WiFi
        jobj.addProperty("Sbbh", GPInfoUtils.getGdid()) // gaid
        apiService.loginSms(createRequestBody(jobj.toString()))
    }

    fun reqSmsCode(mobile: String) = ApiServiceLiveDataProxy.request(RspSmsCode::class.java) {
        val jobj = JsonObject()
        jobj.addProperty("ZF2FqPB8gZ", "phone") //类型（手机短信=phone,手机语音=phonesounds）
        jobj.addProperty("sbSsC", "1") // 验证码类型 （1=注册 3=忘记密码）
        jobj.addProperty("Y4WsSnBl", mobile)
        apiService.getSmsCode(RequestBody.create(MEDIA_TYPE, jobj.toString()))
    }
}