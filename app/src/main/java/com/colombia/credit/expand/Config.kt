package com.colombia.credit.expand

import com.cache.lib.SharedPrefGlobal
import com.colombia.credit.bean.resp.RspCustom
import com.util.lib.GsonUtil


var mCustom: RspCustom?
    set(value) {
        if (value != null)
            SharedPrefGlobal.setString("key_custom", GsonUtil.toJson(value))
    }
    get() {
        val json = SharedPrefGlobal.getString("key_custom", "")
        return GsonUtil.fromJson(json, RspCustom::class.java) as? RspCustom
    }


fun getServiceTel(): String {
    return mCustom?.getServiceTel().orEmpty()
}

fun getWhatsAppTel(): String {
    return mCustom?.QwtC9lYGT7.orEmpty()
}

fun getEmail(): String {
    return mCustom?.GCdnsj.orEmpty()
}