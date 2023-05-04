package com.colombia.credit.expand

import android.content.Context
import com.cache.lib.SharedPrefUser
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.SharedPrefKeyManager

val STEP1 = 1// 基本信息
val STEP2 = 2// 工作信息
val STEP3 = 3// 联系人信息
val STEP4 = 4// 银行卡信息
val STEP5 = 5 // 身份证
val STEP6 = 6// 活体
val STEP_OK = 100

fun jumpProcess(context: Context, type: Int) {
    saveShowBackDialog(true)
    when (type) {
        STEP1 -> {
            Launch.skipPersonalInfoActivity(context)
        }
        STEP2 -> {
            Launch.skipWorkInfoActivity(context)
        }
        STEP3 -> {
            Launch.skipContactInfoActivity(context)
        }
        STEP4 -> {
            Launch.skipBankInfoActivity(context)
        }
        STEP5 -> {
            Launch.skipKycInfoActivity(context)
        }
        STEP6 -> {
            Launch.skipFaceActivity(context)
        }
        STEP_OK -> {
            Launch.skipUploadActivity(context)
        }
    }
}

fun isShowBackDialog() = SharedPrefUser.getBoolean(SharedPrefKeyManager.KEY_SHOW_BACK_DIALOG, true)

fun saveShowBackDialog(isShow: Boolean) {
    SharedPrefUser.setBoolean(SharedPrefKeyManager.KEY_SHOW_BACK_DIALOG, isShow)
}