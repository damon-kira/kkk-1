package com.cache.lib

import android.content.Context
import android.content.SharedPreferences

interface IAppCache: SharedPreferences, SharedPreferences.Editor {

    fun init(context: Context)

    fun init(context: Context, filePath: String)
}
