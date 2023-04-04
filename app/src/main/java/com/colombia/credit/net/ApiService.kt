package com.colombia.credit.net

import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST

/**
 * Created by weisl on 2019/8/28.
 */
interface ApiService {

    @POST("api/v1/auth/login/sms")
    fun getSmsCode(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("api/v1/auth/login/sms")
    fun login(@Body body: RequestBody): Flowable<BaseResponse<String>>
}