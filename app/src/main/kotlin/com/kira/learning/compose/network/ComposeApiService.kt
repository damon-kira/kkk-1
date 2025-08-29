package com.kira.learning.compose.network

import com.common.lib.net.bean.Document
import com.common.lib.net.bean.BaseResponse
import com.kira.learning.bean.QuestionProcessInfo
import com.kira.learning.bean.dao.AIResponseInfo
import com.kira.learning.bean.res.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/** Compose 侧 Retrofit API: 将原 ApiService 所有接口迁移为挂起函数 */
interface ComposeApiService {
    @POST("/ktje3N/ysE")
    @Headers("kio8YGhwe6:Rf4hTm8JkN")
    suspend fun getAppUpdate(): BaseResponse<AppUpgradeInfo>

    @POST("OASJDnjka/vaknNJNAifjos")
    @Headers("kio8YGhwe6:0jjayBHSDygfsHI")
    suspend fun getConfig(@Body body: RequestBody): BaseResponse<RspConfig>

    @POST("XRmN4gV/3YiUwQ")
    @Headers("kio8YGhwe6:bW41VrzLl3")
    suspend fun getCustom(): BaseResponse<RspCustom>

    @POST("jasdi78wd/dfj7senYS")
    @Headers("kio8YGhwe6:Dc7vJh2TrP")
    suspend fun getSmsCode(@Body body: RequestBody): BaseResponse<RspSmsCode>

    @POST("/wFvw7/6bKwy3")
    @Headers("kio8YGhwe6:Ew6rXs7FzC")
    suspend fun loginSms(@Body body: RequestBody): BaseResponse<RspLoginInfo>

    @POST("/YmJbnXu2/40Jfc")
    @Headers("kio8YGhwe6:Afh93km1Kd")
    suspend fun getHomeInfo(): BaseResponse<RspProductInfo>

    @Multipart
    @POST("/fiR/3PA2Q")
    @Headers("kio8YGhwe6:Vf9hLq0GjT", "temp:multipart/form-data;charset=utf-8")
    suspend fun uploadKycImage(@Part img: MultipartBody.Part, @Query("P6KkaG04zb") type: String): BaseResponse<KycOcrInfo>

    @Multipart
    @POST("/QVPWLl/whWhm")
    @Headers("kio8YGhwe6:Qc5nJm2ZuL", "temp:multipart/form-data;charset=utf-8")
    suspend fun uploadFaceImage(@Part body: MultipartBody.Part, @QueryMap map: MutableMap<String, String>): BaseResponse<RspResult>

    @POST("vgBHJE3Ssj/VOnj2njsans")
    @Headers("kio8YGhwe6:yvgaVGDEswjsd")
    suspend fun uploadPersonalInfo(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("ryabehaHBHwbsh/pNJFN2SJNnxj")
    @Headers("kio8YGhwe6:twavgsnja2nja")
    suspend fun getPersonalInfo(): BaseResponse<RspPersonalInfo>

    @POST("fxqYSZI11H/aDGVjE")
    @Headers("kio8YGhwe6:Uw2vMl5ZtP")
    suspend fun uploadContactInfo(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("B4GHMias/N9qiifls")
    @Headers("kio8YGhwe6:Gk1nFj8TzS")
    suspend fun getContactInfo(): BaseResponse<RspContactInfo>

    @POST("nThpJKyVhe/xCwc5dop")
    @Headers("kio8YGhwe6:Vc9rKf1GhT")
    suspend fun uploadBankInfo(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("msdkU7H6Gswe/Lowen8gsdYWE")
    @Headers("kio8YGhwe6:skwueYEU7SDGWE")
    suspend fun getBankInfo(): BaseResponse<RspBankInfo>

    @POST("5Vw4Bz/sx4Yn6V")
    @Headers("kio8YGhwe6:Zt1xQb2VuL")
    suspend fun uploadKycInfo(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("NgX06XLb9a/jzPHk")
    @Headers("kio8YGhwe6:Ys3xMl6DpB")
    suspend fun getKycInfo(): BaseResponse<RspKycInfo>

    @POST("mPmB1n2Nh/fxC")
    @Headers("kio8YGhwe6:Bw8rKf7HtN")
    suspend fun getCertProcess(): BaseResponse<RspCertProcessInfo>

    @Headers("kio8YGhwe6:eGkB4uwv0U")
    @POST("/jUOhsd7UY/ksjdYR6Jn")
    suspend fun firstCacul(): BaseResponse<RspResult>

    @POST("y6zwn6HiW/BRRsOpkNaM")
    @Headers("kio8YGhwe6:Sd7gJm0TfN")
    suspend fun getBankNameList(): BaseResponse<RspBankNameInfo>

    @POST("Ih8Ke/IzDMW1s8")
    @Headers("kio8YGhwe6:Xp6nHk4YsB")
    suspend fun getBankAccountList(): BaseResponse<RspBankAccount>

    @POST("aKzLLSv/F2Y")
    @Headers("kio8YGhwe6:Zg2tPm4KlN")
    suspend fun confirmLoan(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("N6qNa/fH98vRUuB")
    @Headers("kio8YGhwe6:Pf9hKl3GmT")
    suspend fun updateLoanBank(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("tZyFyDwU/wD9X")
    @Headers("kio8YGhwe6:UzB5rkIgVd")
    suspend fun logout(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("6SgT4/N1WQmiq")
    @Headers("kio8YGhwe6:agNcUkPUCu")
    suspend fun getRepeatCalcul(@Body body: RequestBody): BaseResponse<RspRepeatCalcul>

    @POST("S56sI1etMP/bkz")
    @Headers("kio8YGhwe6:32rbrKpP3I")
    suspend fun getHistoryInfo(): BaseResponse<RspHistoryInfo>

    @POST("Fzr/DnfbJqlp")
    @Headers("kio8YGhwe6:Iv7mKp0TcE")
    suspend fun getRepayOrders(): BaseResponse<RspRepayOrders>

    @POST("sWatUY/AaHOW")
    @Headers("kio8YGhwe6:mekK1MgIDG")
    suspend fun getRepayDetail(@Body body: RequestBody): BaseResponse<RspRepayDetail>

    @POST("YGEVMl5jL/tvCwKuE")
    @Headers("kio8YGhwe6:Xy6rDs1FkB")
    suspend fun getCountdownTime(@Body body: RequestBody): BaseResponse<Long>

    @POST("QFnzPxjvvn/qLLgs7QX7")
    @Headers("kio8YGhwe6:Tj8nHs5RqN")
    suspend fun cancelAuto(@Body body: RequestBody): BaseResponse<RspResult>

    @POST("visajndASj9NS/BVBHA9dsNCS")
    @Headers("kio8YGhwe6:ojusajVDSVyhbBHCDA")
    suspend fun checkRepayStatus(@Body body: RequestBody): BaseResponse<RspCheckOrder>

    @POST("/v1/chat/completions123456")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun aiSendRequest(@Body body: RequestBody): BaseResponse<AIResponseInfo>

    @POST("/v1/chat/searchQuestion")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun searchQuestion(@Body body: RequestBody): BaseResponse<QuestionProcessInfo>

    @POST("/v1/kira/activities")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun getQuizInfo(@Body body: RequestBody): BaseResponse<Document>

    // ===== Compose Module Unified APIs =====
    // Sample photos (use full url so we don't need second Retrofit)
    @GET
    suspend fun fetchPhotos(@Url url: String = "https://jsonplaceholder.typicode.com/photos?_limit=20"): List<PhotoDTO>

    // Profile
    @GET("/compose/profile")
    suspend fun getProfile(): UserProfileDTO

    @PUT("/compose/profile")
    suspend fun updateProfile(@Body req: UpdateProfileRequest): UserProfileDTO

    @PUT("/compose/profile/avatar")
    suspend fun updateAvatar(@Body req: UpdateAvatarRequest): UserProfileDTO

    @PUT("/compose/profile/settings")
    suspend fun updateSettings(@Body settings: AppSettingsDTO): AppSettingsDTO
}
