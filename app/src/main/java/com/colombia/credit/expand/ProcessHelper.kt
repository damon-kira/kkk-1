package com.colombia.credit.expand

import android.content.Context
import com.cache.lib.SharedPrefUser
import com.colombia.credit.manager.Launch
import com.colombia.credit.manager.SharedPrefKeyManager

val TYPE_PERSONAL = 1
val TYPE_WORK = 2
val TYPE_CONTACT = 3
val TYPE_BANK = 4
val TYPE_IDENTITY = 5
val TYPE_FACE = 6
val TYPE_SUCCESS = 100

fun jumpProcess(context: Context, type: Int) {
    saveShowBackDialog(true)
    when (type) {
        TYPE_PERSONAL -> {
            Launch.skipPersonalInfoActivity(context)
        }
        TYPE_WORK -> {
            Launch.skipWorkInfoActivity(context)
        }
        TYPE_CONTACT -> {
            Launch.skipContactInfoActivity(context)
        }
        TYPE_BANK -> {
            Launch.skipBankInfoActivity(context)
        }
        TYPE_IDENTITY -> {
            Launch.skipKycInfoActivity(context)
        }
        TYPE_FACE -> {
            Launch.skipFaceActivity(context)
        }
        TYPE_SUCCESS -> {
            Launch.skipUploadActivity(context)
        }
    }
}

var mFirstPageLoanAmount = ""

fun isShowBackDialog() = SharedPrefUser.getBoolean(SharedPrefKeyManager.KEY_SHOW_BACK_DIALOG, true)

fun saveShowBackDialog(isShow: Boolean) {
    SharedPrefUser.setBoolean(SharedPrefKeyManager.KEY_SHOW_BACK_DIALOG, isShow)
}