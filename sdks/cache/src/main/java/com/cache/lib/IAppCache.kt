package com.cache.lib

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by weishl on 2022/7/13
 *
 */
interface IAppCache: SharedPreferences, SharedPreferences.Editor {

    fun init(context: Context)

    fun init(context: Context, filePath: String)
}
