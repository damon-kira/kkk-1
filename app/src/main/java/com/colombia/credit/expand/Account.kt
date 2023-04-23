package com.colombia.credit.expand

import android.content.Context
import com.cache.lib.SharedPrefUser
import com.colombia.credit.app.getAppContext
import com.colombia.credit.bean.resp.RspLoginInfo
import com.colombia.credit.manager.SharedPrefKeyManager
import com.util.lib.GsonUtil
import com.util.lib.ThreadPoolUtil
import com.util.lib.expand.deleteDir
import com.util.lib.expand.getCameraCache


fun saveUserInfo(info: RspLoginInfo) {
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_INFO, GsonUtil.toJson(info))
    setUserId(info.jsa2Dfw3.orEmpty())
    saveUserToken(info.token.orEmpty())
}

fun getUserToken(): String {
    return SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_TOKEN, null)
}

fun saveUserToken(token: String) {
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

var isRepeat: Boolean
    get() = SharedPrefUser.getBoolean(SharedPrefKeyManager.KEY_IS_REPEAT, false)
    set(value) = SharedPrefUser.setBoolean(SharedPrefKeyManager.KEY_IS_REPEAT, value)

var isGp: Boolean = false

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

fun setLogout() {
    saveUserToken("")
    SharedPrefUser.clear()
    deleteCameraCache(getAppContext())
}


/**
 * 删除应用内拍照图片
 */
fun deleteCameraCache(context: Context) {
    ThreadPoolUtil.executor("删除证件信息图片缓存") {
        deleteDir(getCameraCache(context))
    }
}

fun getMobile(): String {
    return SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_MOBILE, null)
}

fun saveMobile(mobile: String) {
    var temp = mobile
    if (mobile.startsWith("57") && mobile.length == 12) {
        temp = mobile.substring(2)
    }
    SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_MOBILE, temp)
}

var mUserName: String
    get() {
        val cache = SharedPrefUser.getString(SharedPrefKeyManager.KEY_USER_NAME, "")
        return if (cache.isNullOrEmpty()) {
            "user"
        } else cache
    }
    set(value) = SharedPrefUser.setString(SharedPrefKeyManager.KEY_USER_NAME, value)
