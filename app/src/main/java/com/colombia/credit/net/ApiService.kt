package com.colombia.credit.net

import com.colombia.credit.bean.resp.*
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    // 获取App版本更新
    @POST("/ktje3N/ysE")
    @Headers("kio8YGhwe6:sdFENAdj8as2DAS")
    fun getAppUpdate(): Flowable<BaseResponse<AppUpgradeInfo>>

    @POST("jasdi78wd/dfj7senYS")
    @Headers("kio8YGhwe6:casdBAS8ASDcbas2LCJS")
    fun getSmsCode(@Body body: RequestBody): Flowable<BaseResponse<RspSmsCode>>

    // 登录
    @POST("/wFvw7/6bKwy3")
    @Headers("kio8YGhwe6:dsfEIFSadssda3KDAlos")
    fun loginSms( @Body body: RequestBody): Flowable<BaseResponse<RspLoginInfo>>

    // 获取首页信息
    @POST("/YmJbnXu2/40Jfc")
    @Headers("kio8YGhwe6:oajsdAsadza")
    fun getHomeInfo(): Flowable<BaseResponse<RspProductInfo>>

    @Multipart
    @POST
    fun uploadKycImage(@Part img: MultipartBody): Flowable<BaseResponse<KycOcrInfo>>

    @POST("/upload/image")
    fun uploadFaceImage(@Body body: MultipartBody): Flowable<BaseResponse<String>>

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

    @POST("tZyFyDwU/wD9X")
    @Headers("kio8YGhwe6:pushMessageLogOut")
    fun logout(@Body body: RequestBody): Flowable<BaseResponse<String>>
}