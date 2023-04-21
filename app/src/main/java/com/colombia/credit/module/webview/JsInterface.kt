package com.colombia.credit.module.webview

import android.webkit.JavascriptInterface
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.expand.toast


class JsInterface(webHost: IWebHost) : LifecycleEventObserver {
    private var fragment: IWebHost? = webHost

    companion object {
        const val DEBUG = false
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            fragment = null
        } else if (event == Lifecycle.Event.ON_STOP) {
            fragment?.loading(false)
        }
    }


    private val FLAG_LOADING = "0x10" // params : 0:showloading 1:hideloading
    private val FLAG_TOAST = "0x11" //显示toast
    private val FLAG_TO_APP_TOAST = "0x12"// 显示toast 并退出H5页面

    @JavascriptInterface
    fun withApp(flag: String, params: String? = null) {
        when (flag) {
            FLAG_LOADING -> {
                fragment?.loading(params == "0")
            }
            FLAG_TOAST -> {
                params?.toast()
            }
            FLAG_TO_APP_TOAST -> {
                params?.toast()
                fragment?.exit()
            }
            else -> {}
        }
    }
}