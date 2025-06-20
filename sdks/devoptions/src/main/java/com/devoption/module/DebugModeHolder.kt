package com.devoption.module

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.ref.WeakReference

internal object DebugModeHolder {

    private const val TAG = "debug_DebugModeHolder"

    private const val CONFIG_FILE = "apiConfig.json"

    lateinit var mCtx: WeakReference<Context>

    internal var mIsApiChanged: Boolean = false

    internal var mCurrApiModeDebug: Boolean = true

    @JvmStatic
    fun getContext() = mCtx.get()

    @JvmStatic
    fun init(context: Context) {
        mCtx = WeakReference(context)
        DebugSPUtils.init(context)
        initApiInfo(context)
    }

    @JvmStatic
    fun initApiInfo(context: Context?) {
        context ?: return
        val result = StringBuilder()
        val inputStream = try {
            context.resources.assets.open(CONFIG_FILE)
        } catch (e: Exception) {
            Log.e(
                TAG,
                "initApiInfo: ********** assets dir have not '$CONFIG_FILE' file ***************"
            )
            return
        }
        val br = BufferedReader(InputStreamReader(inputStream))
        br.use {
            val line = br.readText()
            result.append(line)
            inputStream.close()
        }
        Log.e(TAG, "initApiInfo: result = $result")
        if (result.isNotEmpty()) {
            val jsonStr = result.toString()
            DebugSPUtils.setString(DebugSPKey.KEY_API_JSON, jsonStr)
            DebugGsonUtil.fromJsonNew(jsonStr, ApiConfig::class.java)?.let { apiConfig ->
                Log.e(TAG, "initApiInfo: apiConfig = $apiConfig")
                DebugModeConfig.setApiConfig(apiConfig)
            }
        }
    }

    fun isApiChanged(): Boolean {
        return mCurrApiModeDebug == DebugApiConfigInfo.isApiDebug
    }

    fun deleteCache() {
        DebugModeHolder.getContext()?.cacheDir?.let {
            deleteDir(it)
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir != null && dir.isDirectory) {
            val children: Array<String> = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            return dir.delete()
        }
        return dir.delete()
    }
}