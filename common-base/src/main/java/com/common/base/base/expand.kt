package com.common.base.base

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.content.ContextWrapper

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