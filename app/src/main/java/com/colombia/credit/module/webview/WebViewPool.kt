package com.colombia.credit.module.webview

import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.MutableContextWrapper
import android.content.res.Configuration
import com.colombia.credit.view.BaseWebView
import com.util.lib.log.logger_e
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

class WebViewPool private constructor() {
    companion object {
        private const val TAG = "WebViewPool"
        @JvmField
        val INSTANCE = WebViewPool()
    }

    private val mWebQueue: Queue<BaseWebView> = LinkedBlockingQueue<BaseWebView>(1)

    @Volatile
    private var isInitialized = false

    private lateinit var mContext: Context;

    private val recyclable = true

    fun init(application: Application, create: Boolean = true) {
        synchronized(this@WebViewPool) {
            if (!isInitialized) {
                application.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onLowMemory() {
                        clear()
                    }

                    override fun onConfigurationChanged(newConfig: Configuration) {
                    }
                })
                mContext = application.applicationContext
                isInitialized = true
//                if (create && recyclable) {
////                    mWebQueue.offer(acquire(mContext))
//                }
            }
        }
    }

    fun acquire(context: Context): BaseWebView {
        if (!isInitialized) throw IllegalStateException("not initialized yet")
        return (mWebQueue.poll() ?: BaseWebView(MutableContextWrapper(context))).apply {
            val webContext = this.context
            if (webContext is MutableContextWrapper) {
                webContext.baseContext = context
            }
            this.reuse()
        }
    }

    fun recycle(webView: BaseWebView) {
        try {
            var success = true
            webView.apply {
                success = reset()
                val webContext = this.context
                if (webContext is MutableContextWrapper) {
                    webContext.baseContext = mContext
                }
            }
            if (recyclable && success) {
                val added = mWebQueue.offer(webView)
                if (!added) {
                    webView.destroy()
                }
            } else {
                webView.destroy()
            }
        } catch (e: Throwable){
           logger_e(TAG,e.message.orEmpty())
        }
    }

    fun clear() {
        do {
            val webView = mWebQueue.poll()
            webView?.destroy()
        } while (webView != null) // y 在此处可见

    }

}