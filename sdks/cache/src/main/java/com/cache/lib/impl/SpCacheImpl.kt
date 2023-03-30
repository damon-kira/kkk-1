package com.cache.lib.impl

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.cache.lib.IAppCache
import com.cache.lib.SharedPrefGlobal

/**
 * Created by weishl on 2022/7/13
 *
 */
abstract class SpCacheImpl : IAppCache {

    lateinit var mSP: SharedPreferences

    abstract fun getFileName(): String

    override fun init(context: Context) {
        init(context, getFileName())
    }

    override fun init(context: Context, name: String) {
        mSP = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun getAll(): MutableMap<String, *> {
        return mSP.all
    }

    override fun getString(key: String?, defValue: String?): String? {
        return mSP.getString(key, defValue)
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        return mSP.getStringSet(key, defValues)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return mSP.getInt(key, defValue)
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return mSP.getLong(key, defValue)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return mSP.getFloat(key, defValue)
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return mSP.getBoolean(key, defValue)
    }

    override fun contains(key: String?): Boolean {
        return mSP.contains(key)
    }

    override fun edit(): SharedPreferences.Editor {
        return mSP.edit()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        return mSP.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        return mSP.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun putString(key: String?, value: String?): SharedPreferences.Editor {
        return mSP.edit().putString(key, value).also {
            it.apply()
        }
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?): SharedPreferences.Editor {
        return mSP.edit().putStringSet(key, values).also {
            it.apply()
        }
    }

    override fun putInt(key: String?, value: Int): SharedPreferences.Editor {
        return mSP.edit().putInt(key, value).also {
            it.apply()
        }
    }

    override fun putLong(key: String?, value: Long): SharedPreferences.Editor {
        return mSP.edit().putLong(key, value).also {
            it.apply()
        }
    }

    override fun putFloat(key: String?, value: Float): SharedPreferences.Editor {
        return mSP.edit().putFloat(key, value).also {
            it.apply()
        }
    }

    override fun putBoolean(key: String?, value: Boolean): SharedPreferences.Editor {
        return mSP.edit().putBoolean(key, value).also {
            it.apply()
        }
    }

    override fun remove(key: String?): SharedPreferences.Editor {
        try {
            if (mSP.contains(key)) {
                mSP.edit().remove(key).apply()
            }
        } catch (e: Exception) {
            Log.e(SharedPrefGlobal.TAG, "remove key e.toString$e")
        }
        return mSP.edit()
    }

    override fun clear(): SharedPreferences.Editor {
        mSP.edit().clear().apply()
        return mSP.edit()
    }

    override fun commit(): Boolean {
        mSP.edit().commit()
        return true
    }

    override fun apply() {
        mSP.edit().apply()
    }
}