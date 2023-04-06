package com.colombia.credit.net

import com.colombia.credit.bean.resp.KycOcrInfo
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by weisl on 2019/8/28.
 */
interface ApiService {

    @POST("api/v1/auth/login/sms")
    fun getSmsCode(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("api/v1/auth/login/sms")
    fun login(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @Multipart
    @POST
    fun uploadKycImage(@Part img: MultipartBody): Flowable<BaseResponse<KycOcrInfo>>


    @POST("/upload/image")
    fun uploadFaceImage(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("")
    fun uploadPersonalInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("")
    fun uploadWorkInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("")
    fun uploadContactInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("")
    fun uploadBankInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    @POST("")
    fun uploadKycInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>
}