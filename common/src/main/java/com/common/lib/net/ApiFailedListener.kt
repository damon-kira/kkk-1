package com.common.lib.net

import com.common.lib.net.bean.BaseResponse


interface ApiFailedListener {

    fun onFailed(result: BaseResponse<*>, gotoLogin:Boolean)
}