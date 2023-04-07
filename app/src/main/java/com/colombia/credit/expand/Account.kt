package com.colombia.credit.expand

import com.cache.lib.SharedPrefUser
import com.colombia.credit.bean.resp.RspLoginInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.util.lib.GsonUtil


fun saveUserInfo(info: RspLoginInfo) {
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_INFO, GsonUtil.toJson(info))
    setUserToken(info.token.orEmpty())
    setUserId(info.jsa2Dfw3.orEmpty())
}

fun getUserToken(): String {
    return SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_TOKEN, null)
}

fun setUserToken(token: String) {
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_TOKEN, token)
}

fun getUserId(): String {
    return SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_ID, null)
}

fun setUserId(id: String) {
    if (id.isEmpty()) return
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_TOKEN, id)
}

fun inValidToken(): Boolean {
    return getUserToken().isEmpty()
}

// 是否是gp审核账号
fun isGpAccount(): Boolean {
    val json = SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_INFO, null)
    return ((GsonUtil.fromJson(json, RspLoginInfo::class.java) as? RspLoginInfo)?.vhFsD8cK
        ?: 0) == 1
}

// 是否是首次注册
fun isFirstRegister(): Boolean {
    val json = SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_INFO, null)
    return ((GsonUtil.fromJson(json, RspLoginInfo::class.java) as? RspLoginInfo)?.roiM2eg8uM
        ?: 0) == 1
}

fun getMobile(): String {
    return SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_MOBILE, null)
}

fun saveMobile(mobile: String) {
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_MOBILE, mobile)
}

fun getUserName(): String {
    return ""
}
