package com.colombia.credit.expand

import com.common.lib.net.bean.BaseResponse


fun BaseResponse<*>.ShowErrorMsg(refresh: (() -> Unit)? = null) {
    val msg = message
    // 如果是网络异常，弹网络异常弹窗
    if (msg.isNullOrEmpty()) {
        // 根据错误码提示
        return
    }
    toast(msg)
}