package com.colombia.credit.net

import com.common.lib.net.bean.BaseResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.POST

/**
 * Created by weisl on 2019/8/28.
 */
interface ApiService {

    @POST("api/v1/auth/login/sms")
    suspend fun login(@Field("data") data: String): Call<BaseResponse<String>>
}