package com.common.lib.expand

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper

/**
 *@author zhujun
 *@description:
 *@date : 2022/9/15 5:39 下午
 */
fun getActivityFromContext( context: Context?): Activity? {
    if (context == null) {
        return null
    }
    if (context is Activity) {
        return context
    }
    if (context is Application || context is Service) {
        return null
    }
    var c: Context = context
    if (c is ContextWrapper) {
        c = c.baseContext
        if (c is Activity) {
            return c
        }
    } else {
        return null
    }
    return null
}