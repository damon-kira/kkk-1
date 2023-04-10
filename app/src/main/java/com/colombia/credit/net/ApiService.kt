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
    @Headers("kio8YGhwe6:Rf4hTm8JkN")
    fun getAppUpdate(): Flowable<BaseResponse<AppUpgradeInfo>>

    // 获取验证码
    @POST("jasdi78wd/dfj7senYS")
    @Headers("kio8YGhwe6:Dc7vJh2TrP")
    fun getSmsCode(@Body body: RequestBody): Flowable<BaseResponse<RspSmsCode>>

    // 登录
    @POST("/wFvw7/6bKwy3")
    @Headers("kio8YGhwe6:Ew6rXs7FzC")
    fun loginSms(@Body body: RequestBody): Flowable<BaseResponse<RspLoginInfo>>

    // 获取首页信息
    @POST("/YmJbnXu2/40Jfc")
    @Headers("kio8YGhwe6:Afh93km1Kd")
    fun getHomeInfo(): Flowable<BaseResponse<RspProductInfo>>

    //上传身份证照片
    @POST("/fiR/3PA2Q")
    @Headers("kio8YGhwe6:Vf9hLq0GjT")
    fun uploadKycImage(@Part img: MultipartBody): Flowable<BaseResponse<KycOcrInfo>>

    // 上传活体照片
    @POST("QVPWLl/whWhm")
    @Headers("kio8YGhwe6:Qc5nJm2ZuL")
    fun uploadFaceImage(@Body body: MultipartBody,@QueryMap map: MutableMap<String, String>): Flowable<BaseResponse<String>>

    // 上传个人信息
    @POST("WY8x6gV/GWE9")
    @Headers("kio8YGhwe6:Qb5rDc2PnE")
    fun uploadPersonalInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    // 上传工作信息
    @POST("zJWiAtubkn/PX9")
    @Headers("kio8YGhwe6:Nf8xLg7JkY")
    fun uploadWorkInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    // 上传联系人信息
    @POST("fxqYSZI11H/aDGVjE")
    @Headers("kio8YGhwe6:Uw2vMl5ZtP")
    fun uploadContactInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    // 上传银行信息
    @POST("nThpJKyVhe/xCwc5dop")
    @Headers("kio8YGhwe6:Vc9rKf1GhT")
    fun uploadBankInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    // 上传身份证信息
    @POST("5Vw4Bz/sx4Yn6V")
    @Headers("kio8YGhwe6:Zt1xQb2VuL")
    fun uploadKycInfo(@Body body: RequestBody): Flowable<BaseResponse<String>>

    // 获取认证进度
    @POST("mPmB1n2Nh/fxC")
    @Headers("kio8YGhwe6:Bw8rKf7HtN")
    fun getCertProcess():Flowable<BaseResponse<RspCertProcessInfo>>

    @POST("tZyFyDwU/wD9X")
    @Headers("kio8YGhwe6:UzB5rkIgVd")
    fun logout(@Body body: RequestBody): Flowable<BaseResponse<String>>
}