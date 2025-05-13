package com.kira.learning.manager

import com.kira.learning.Constant
import com.kira.learning.app.AppEnv
import com.kira.learning.app.getAppContext
import com.kira.learning.expand.formatCommon
import com.kira.learning.expand.getUserToken
import com.kira.learning.util.GPInfoUtils
import com.util.lib.SysUtils

object H5UrlManager {

    private val base_url = Constant.BASE_H5_URL
    val URL_PRIVACY = "$base_url/ggreazda" // 隐私协议

    val URL_FEEDBACK = "$base_url/wfefgewf" // 反馈
        get() = urlAppend(field)

    val URL_ABOUT = "$base_url/gfewffa" // 关于我们
        get() = "$field?ver=${AppEnv.version}"

    //    aw1513few=金额值 cns23win1=订单ID kowi12xkis=订单类型
    val URL_PAY = "$base_url/gregzhhtrw" // 支付
        get() = urlAppend(field)

    /**
     * @param type 1:展期 2正常订单
     *
     */
    fun getPayUrl(id: String, amount: String, type: String): String {
        return StringBuilder(URL_PAY)
            .append("&aw1513few=").append(formatCommon(amount))
            .append("&gPV7c5=").append(id)
            .append("&NVLXhwe=").append(type)
            .toString()
    }

    fun urlAppend(url: String): String {
        val sr = StringBuilder(url).append("?")
        sr.append("vMRdV0dUmj=").append(AppEnv.version)
            .append("&tk=").append(getUserToken())
            .append("&dnpiIILLEI=android")
            .append("&NbBH4GIwmz=").append(SysUtils.getImei(getAppContext()))
            .append("&pg77Foy4PL=").append(GPInfoUtils.getGdid())
        return sr.toString()
    }
}