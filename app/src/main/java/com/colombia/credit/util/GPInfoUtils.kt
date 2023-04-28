package com.colombia.credit.util

import com.colombia.credit.app.getAppContext

object GPInfoUtils {

    // 获取广告id
    @JvmStatic
    fun getGdid(): String {
        return ""
    }

    @JvmStatic
    fun getFcmToken(): String {
        return ""
    }

    const val TAG1 = "Ex_zc" // 完成注册
    const val TAG2 = "Ex_first"// 基本信息提交
    const val TAG3 = "Ex_con"// 联系人提交
    const val TAG4 = "Ex_Amou"// 银行卡提交
    const val TAG5 = "Ex_cc"// OCR提交
    const val TAG6 = "Ex_peo"// 人脸通过
    const val TAG7 = "Ex_suess"// 人脸后数据上传成功
    const val TAG8 = "Ex_con"// 首贷确认成功

    fun saveTag(tag: String){
//        FirebaseAnalytics.getInstance(getAppContext()).logEvent(tag, null)
    }
}