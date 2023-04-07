package com.colombia.credit.expand

import com.common.lib.net.bean.BaseResponse


fun BaseResponse<*>.ShowErrorMsg() {
    val msg = message
    if (msg.isNullOrEmpty()) {
        // 根据错误码提示
        return
    }
    toast(msg)
}