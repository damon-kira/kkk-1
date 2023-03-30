package com.cache.lib

import android.content.SharedPreferences
import android.util.Log
import com.cache.lib.impl.SpCacheImpl

/**
 * Created by weisl on 2019/10/14.
 * 系统缓存的一下数据，退出登录无需清除
 */
object SharedPrefGlobal{

    const val TAG = "SharedPrefSystem"

    private val mCache: IAppCache by lazy {
        object : SpCacheImpl() {
            override fun getFileName(): String = "global.cache"
        }.also {
            it.init(getContext())
        }
    }

    fun setString(key: String, value: String?) {
        mCache.putString(key, value ?: "")
    }

    fun setSynchString(key: String, value: String?) {
        mCache.putString(key, value ?: "")
    }

    fun getString(key: String, defVal: String?): String {
        val spUserValue = SharedPrefUser.getString(key, defVal)
        if (spUserValue != defVal){
            setString(key,spUserValue)
        }
        return mCache.getString(key, defVal ?: "").orEmpty()
    }

    fun setBoolean(key: String, value: Boolean) {
        mCache.putBoolean(key, value)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val spUserValue = SharedPrefUser.getBoolean(key, defValue)
        if (spUserValue != defValue){
            setBoolean(key,spUserValue)
        }
        return mCache.getBoolean(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        mCache.putInt(key, value)
    }

    fun getInt(key: String, defValue: Int): Int {
        val spUserValue = SharedPrefUser.getInt(key, defValue)
        if (spUserValue != defValue){
            setInt(key,spUserValue)
        }
        return mCache.getInt(key, defValue)
    }

    fun setLong(key: String, value: Long) {

        mCache.putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        val spUserValue = SharedPrefUser.getLong(key, defValue)
        if (spUserValue != defValue){
            getLong(key,spUserValue)
        }
        return mCache.getLong(key, defValue)
    }

    fun setStringNoApply(key: String, value: String?): SharedPreferences.Editor {
        return mCache.putString(key, value ?: "")
    }

    fun setBooleanNoApply(key: String, value: Boolean): SharedPreferences.Editor {
        return mCache.putBoolean(key, value)
    }

    fun setIntNoApply(key: String, value: Int): SharedPreferences.Editor {
        return mCache.putInt(key, value)
    }

    fun setLongNoApply(key: String, value: Long): SharedPreferences.Editor {
        return mCache.putLong(key, value)
    }

    fun getAll(): Map<String, Any?> {
        return mCache.all
    }

    fun removeKey(key: String) {
        try {
            if (mCache.contains(key)) {
                mCache.remove(key).apply()
            }
        } catch (e: Exception) {
            if (CacheInit.get().isDebug()) {
                Log.e(TAG, "remove key e.toString$e")
            }
        }
    }

    fun clear() {
        try {
            mCache.clear().apply()
        } catch (e: Exception) {
        }
    }
}