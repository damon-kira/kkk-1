package com.colombia.credit.util

import com.colombia.credit.app.getAppContext
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger

//import com.finance.analysis.FirebaseInfo
//import com.finance.analysis.push.PushManagerFactory

object GPInfoUtils {

    // 获取广告id
    @JvmStatic
    fun getGdid(): String {
//        return PushManagerFactory.getGaid(getAppContext()).orEmpty()
        return ""
    }

    @JvmStatic
    fun getFcmToken(): String {
//        return FirebaseInfo.getFcmToken(getAppContext())
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
        saveFbTag(tag)
    }
    fun saveFbTag(tag: String) {
        when (tag) {
            TAG1 -> AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION
            TAG2 -> AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT
            TAG3 -> AppEventsConstants.EVENT_NAME_PURCHASED
            TAG4 -> AppEventsConstants.EVENT_NAME_ADDED_TO_CART
            TAG5 -> AppEventsConstants.EVENT_NAME_ADDED_PAYMENT_INFO
            TAG6 -> AppEventsConstants.EVENT_NAME_COMPLETED_TUTORIAL
            TAG7 -> AppEventsConstants.EVENT_NAME_ACHIEVED_LEVEL
            TAG8 -> AppEventsConstants.EVENT_NAME_ADDED_TO_WISHLIST
            else -> null
        }?.let {finalTag ->
            AppEventsLogger.newLogger(getAppContext()).logEvent(finalTag)
        }
    }
}