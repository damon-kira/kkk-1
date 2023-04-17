package com.common.lib.net

/**
 * Created by weisl on 2019/8/13.
 */
object ResponseCode {

    const val TAG = "okhttp"
    const val SUCCESS_CODE = 200//接口请求成功码
    const val OTHER_ERROR_CODE = -111//其他未定义错误
    const val SSL_ERROR_CODE = 120 // CA证书失效
    const val REQUEST_ERROR_CODE = -2//请求失败
    const val PAYTM_ERROR_CODE = -3//paytm支付错误
    const val INVALIDTOKEN = 401//token过期和无效


}