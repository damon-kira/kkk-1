package com.colombia.credit.module.login

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.cache.lib.SharedPrefGlobal
import com.colombia.credit.manager.SharedPrefKeyManager
import com.google.gson.reflect.TypeToken
import com.util.lib.GsonUtil
import com.util.lib.TimerUtil
import com.util.lib.time2Str

class LoginHelper : LifecycleEventObserver {

    private val KEY_TIME = "time"

    private var mCache: HashMap<String, Long>? = null
        get() {
            if (field == null) {
                val map = getCache()
                field = map
            }
            return field
        }

    // 如果是0或者是get返回为空，则是第一次，返回true
    // 如果是 >0 则不是第一次
    fun isFirst(mobile: String): Boolean {
        val count = mCache?.get(mobile) ?: 0
        mCache?.set(mobile, (count + 1))
        return (count < 1)
    }

    //@return  true:第一次触发自动获取验证码
    fun isFirstAuto(mobile: String): Boolean {
        val count = mCache?.get(mobile) ?: 0
        return count == 1L
    }

    fun save() {
        mCache?.let {
            SharedPrefGlobal.setString(SharedPrefKeyManager.KEY_LOGIN_MOBILE, GsonUtil.toJson(it))
        }
    }

    private fun getCache(): HashMap<String, Long> {
        val cacheJson = SharedPrefGlobal.getString(SharedPrefKeyManager.KEY_LOGIN_MOBILE, "")
        if (cacheJson.isNullOrEmpty()) {
            return getMap()
        }
        val cache = GsonUtil.fromJson(cacheJson, object : TypeToken<HashMap<String, Long>>() {})
            ?: return getMap()
        if (cache.containsKey(KEY_TIME)) {
            val time = cache[KEY_TIME] ?: System.currentTimeMillis()
            val cacheTime = time2Str(time, TimerUtil.REGEX_DDMMYYYY)
            val currTime = time2Str(System.currentTimeMillis(), TimerUtil.REGEX_DDMMYYYY)
            if (cacheTime != currTime) {
                cache.clear()
                cache[KEY_TIME] = System.currentTimeMillis()
            }
        }
        return cache
    }

    private fun getMap(): HashMap<String, Long> {
        return HashMap<String, Long>().also {
            it[KEY_TIME] = System.currentTimeMillis()
        }
    }

    fun clearCache() {
        SharedPrefGlobal.removeKey(SharedPrefKeyManager.KEY_LOGIN_MOBILE)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            save()
        }
    }
}