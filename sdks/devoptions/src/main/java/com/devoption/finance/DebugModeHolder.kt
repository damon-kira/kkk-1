package com.devoption.finance

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference

/**
 * Created by weishl on 2021/7/13
 *
 */
internal object DebugModeHolder {

    private const val TAG = "debug_DebugModeHolder"

    private const val CONFIG_FILE = "apiConfig.json"

    lateinit var mCtx: WeakReference<Context>

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
}