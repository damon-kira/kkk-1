package com.cache.lib

import android.util.Log
import com.cache.lib.impl.SpCacheImpl

/**
 * Created by weisl on 2019/10/14.
 */
object SharedPrefUser {
    const val TAG = "SharedPref"

    /**
     * 记录应用的版本versionCode
     */
    const val KEY_APP_VERSION = "key_app_version"

    private val mCache: IAppCache by lazy {
        object: SpCacheImpl() {
            override fun getFileName(): String = "user.cache"
        }.also {
            it.init(getContext())
        }
    }

    fun setString(key: String, value: String?) {
        mCache.putString(key, value.orEmpty())
    }

    fun setSynchString(key: String, value: String?) {
        mCache.putString(key, value.orEmpty())
    }

    fun getString(key: String, defVal: String?): String {
        return mCache.getString(key, defVal).orEmpty()
    }

    fun setBoolean(key: String, value: Boolean) {
        mCache.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mCache.getBoolean(key, defValue)
    }

    fun setInt(key: String, value: Int) {
        mCache.putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return mCache.getInt(key, defValue)
    }

    fun setLong(key: String, value: Long) {
        mCache.putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return mCache.getLong(key, defValue)
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

    fun containsKey(key: String): Boolean {
        return mCache.contains(key)
    }

    fun clear() {
        try {
            mCache.clear().apply()
        } catch (e: Exception) {
        }
    }
}