package com.colombia.credit.net

import com.colombia.credit.bean.resp.RspCheckData
import com.colombia.credit.bean.resp.RspResult
import com.common.lib.net.bean.BaseResponse
import io.reactivex.Flowable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface DataApiService {

    // 风控数据检查
    @POST("XRmN4gV/pv1eW")
    @Headers("kio8YGhwe6:lM01Yybjt8")
    fun checkData(): Flowable<BaseResponse<RspCheckData>>

    // 上传短信
    @POST("XRmN4gV/fGLRm")
    @Headers("kio8YGhwe6:Zvk5RbsUmG")
    fun uploadSms(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 上传app list
    @POST("XRmN4gV/joGno1gz8")
    @Headers("kio8YGhwe6:SQhLOmOKfo")
    fun uploadAppList(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 上传app list
    @POST("/XRmN4gV/fGLRm")
    @Headers("kio8YGhwe6:Zvk5RbsUmG")
    fun uploadCo(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>
}