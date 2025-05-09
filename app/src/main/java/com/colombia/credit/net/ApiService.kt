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

    // 获取配置
    @POST("OASJDnjka/vaknNJNAifjos")
    @Headers("kio8YGhwe6:0jjayBHSDygfsHI")
    fun getConfig(@Body body: RequestBody): Flowable<BaseResponse<RspConfig>>

    // 获取客服信息
    @POST("XRmN4gV/3YiUwQ")
    @Headers("kio8YGhwe6:bW41VrzLl3")
    fun getCustom(): Flowable<BaseResponse<RspCustom>>

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
    @Multipart
    @POST("/fiR/3PA2Q")
    @Headers("kio8YGhwe6:Vf9hLq0GjT","temp:multipart/form-data;charset=utf-8")
    fun uploadKycImage(@Part img: MultipartBody.Part, @Query("P6KkaG04zb") type: String): Flowable<BaseResponse<KycOcrInfo>>

    // 上传活体照片
    @Multipart
    @POST("/QVPWLl/whWhm")
    @Headers("kio8YGhwe6:Qc5nJm2ZuL","temp:multipart/form-data;charset=utf-8")
    fun uploadFaceImage(@Part body: MultipartBody.Part, @QueryMap map: MutableMap<String, String>): Flowable<BaseResponse<RspResult>>

    // 上传个人信息
    @POST("vgBHJE3Ssj/VOnj2njsans")
    @Headers("kio8YGhwe6:yvgaVGDEswjsd")
    fun uploadPersonalInfo(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 获取个人信息
    @POST("ryabehaHBHwbsh/pNJFN2SJNnxj")
    @Headers("kio8YGhwe6:twavgsnja2nja")
    fun getPersonalInfo(): Flowable<BaseResponse<RspPersonalInfo>>

    // 上传联系人信息
    @POST("fxqYSZI11H/aDGVjE")
    @Headers("kio8YGhwe6:Uw2vMl5ZtP")
    fun uploadContactInfo(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 获取联系人信息
    @POST("B4GHMias/N9qiifls")
    @Headers("kio8YGhwe6:Gk1nFj8TzS")
    fun getContactInfo(): Flowable<BaseResponse<RspContactInfo>>

    // 上传银行信息
    @POST("nThpJKyVhe/xCwc5dop")
    @Headers("kio8YGhwe6:Vc9rKf1GhT")
    fun uploadBankInfo(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 获取银行信息 待修改接口
    @POST("msdkU7H6Gswe/Lowen8gsdYWE")
    @Headers("kio8YGhwe6:skwueYEU7SDGWE")
    fun getBankInfo(): Flowable<BaseResponse<RspBankInfo>>

    // 上传身份证信息
    @POST("5Vw4Bz/sx4Yn6V")
    @Headers("kio8YGhwe6:Zt1xQb2VuL")
    fun uploadKycInfo(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 获取身份证信息
    @POST("NgX06XLb9a/jzPHk")
    @Headers("kio8YGhwe6:Ys3xMl6DpB")
    fun getKycInfo(): Flowable<BaseResponse<RspKycInfo>>

    // 获取认证进度
    @POST("mPmB1n2Nh/fxC")
    @Headers("kio8YGhwe6:Bw8rKf7HtN")
    fun getCertProcess():Flowable<BaseResponse<RspCertProcessInfo>>

    @Headers("kio8YGhwe6:eGkB4uwv0U")
    @POST("/jUOhsd7UY/ksjdYR6Jn")
    fun firstCacul(): Flowable<BaseResponse<RspResult>>

    // 获取银行名称列表
    @POST("y6zwn6HiW/BRRsOpkNaM")
    @Headers("kio8YGhwe6:Sd7gJm0TfN")
    fun getBankNameList(): Flowable<BaseResponse<RspBankNameInfo>>

    // 获取银行账户
    @POST("Ih8Ke/IzDMW1s8")
    @Headers("kio8YGhwe6:Xp6nHk4YsB")
    fun getBankAccountList(): Flowable<BaseResponse<RspBankAccount>>

    @POST("aKzLLSv/F2Y")
    @Headers("kio8YGhwe6:Zg2tPm4KlN")
    fun confirmLoan(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 更改放款银行
    @POST("N6qNa/fH98vRUuB")
    @Headers("kio8YGhwe6:Pf9hKl3GmT")
    fun updateLoanBank(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    @POST("tZyFyDwU/wD9X")
    @Headers("kio8YGhwe6:UzB5rkIgVd")
    fun logout(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>
    // 复借客户勾选产品进入确认金额期限页面
    @POST("6SgT4/N1WQmiq")
    @Headers("kio8YGhwe6:agNcUkPUCu")
    fun getRepeatCalcul(@Body body: RequestBody): Flowable<BaseResponse<RspRepeatCalcul>>

    // 历史订单
    @POST("S56sI1etMP/bkz")
    @Headers("kio8YGhwe6:32rbrKpP3I")
    fun getHistoryInfo(): Flowable<BaseResponse<RspHistoryInfo>>

    //获取还款订单
    @POST("Fzr/DnfbJqlp")
    @Headers("kio8YGhwe6:Iv7mKp0TcE")
    fun getRepayOrders(): Flowable<BaseResponse<RspRepayOrders>>

    // 获取还款详情
    @POST("sWatUY/AaHOW")
    @Headers("kio8YGhwe6:mekK1MgIDG")
    fun getRepayDetail(@Body body: RequestBody): Flowable<BaseResponse<RspRepayDetail>>

    // 获取还款详情
    @POST("YGEVMl5jL/tvCwKuE")
    @Headers("kio8YGhwe6:Xy6rDs1FkB")
    fun getCountdownTime(@Body body: RequestBody): Flowable<BaseResponse<Long>>

    // 获取还款详情
    @POST("QFnzPxjvvn/qLLgs7QX7")
    @Headers("kio8YGhwe6:Tj8nHs5RqN")
    fun cancelAuto(@Body body: RequestBody): Flowable<BaseResponse<RspResult>>

    // 查询订单是否已还
    @POST("visajndASj9NS/BVBHA9dsNCS")
    @Headers("kio8YGhwe6:ojusajVDSVyhbBHCDA")
    fun checkRepayStatus(@Body body: RequestBody): Flowable<BaseResponse<RspCheckOrder>>
}