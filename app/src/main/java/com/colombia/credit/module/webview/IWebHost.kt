package com.colombia.credit.module.webview

import android.content.Context

interface IWebHost {

    fun getSupportContext(): Context

    fun exit()

//    fun generateShortLink(template: String, inviteCode: String, channel: String, shareType: String)

    fun loading(show: Boolean)

}