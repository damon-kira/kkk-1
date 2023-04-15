package com.colombia.credit.module.webview

import androidx.lifecycle.*



class JsInterface(webHost: IWebHost) : LifecycleEventObserver {
    private var fragment: IWebHost? = webHost

    companion object {
        const val DEBUG = false
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            source.lifecycle.removeObserver(this)
            fragment = null
        }
    }
}