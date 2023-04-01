package com.colombia.credit.module.webview

import android.content.Context

interface IWebHost {

    fun getSupportContext(): Context

    fun <T> getCurrentActivity(): T?

    fun toolbarGone(toolbarBgColor: String, isDark: Boolean)

    fun finish()

    fun generateShortLink(template: String, inviteCode: String, channel: String, shareType: String)

}