package com.colombia.credit.expand

import android.accounts.NetworkErrorException
import com.colombia.credit.app.AppInjector
import com.common.lib.net.ResponseCode
import com.common.lib.net.bean.BaseResponse
import java.net.ConnectException
import java.util.concurrent.TimeoutException


fun BaseResponse<*>.ShowErrorMsg(refresh: (() -> Unit)? = null) {
    val msg = msg
    if (e is ConnectException || e is TimeoutException || e is NetworkErrorException) {
        AppInjector.getTopActivity()?.showNetErrorDialog {
            refresh?.invoke()
        }
        return
    }
    if (code == ResponseCode.INVALIDTOKEN) {
        return
    }

    // 如果是网络异常，弹网络异常弹窗
    if (msg.isNullOrEmpty()) {
        // 根据错误码提示
        return
    }
    toast(msg)
}