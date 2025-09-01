package com.kira.learning.xml.modules.webview

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.MutableContextWrapper
import android.content.res.Configuration
import android.os.Looper
import com.kira.learning.xml.view.BaseWebView
import com.util.lib.log.logger_e
import java.lang.ref.WeakReference
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

    // 使用 ApplicationContext 的弱引用，避免 Lint 对静态 Context 的警告
    private var appContextRef: WeakReference<Context>? = null

    private val recyclable = true

    /**
     * 初始化，必须传入 Application。可选预创建池中首个 WebView。
     */
    @SuppressLint("StaticFieldLeak")
    fun init(application: Application, create: Boolean = true) {
        // 显式预热入口（可选）
        internalInit(application.applicationContext, registerCallbacks = true, prewarm = create)
    }

    @SuppressLint("StaticFieldLeak")
    private fun internalInit(ctx: Context, registerCallbacks: Boolean, prewarm: Boolean) {
        if (isInitialized) return
        synchronized(this) {
            if (isInitialized) return
            val appCtx = ctx.applicationContext
            if (registerCallbacks && appCtx is Application) {
                appCtx.registerComponentCallbacks(object : ComponentCallbacks {
                    override fun onLowMemory() { clear() }
                    override fun onConfigurationChanged(newConfig: Configuration) { }
                })
            }
            appContextRef = WeakReference(appCtx)
            isInitialized = true
            if (prewarm && recyclable && Looper.getMainLooper() == Looper.myLooper()) {
                try { mWebQueue.offer(BaseWebView(MutableContextWrapper(appCtx)).apply { reuse() }) } catch (_: Throwable) {}
            }
        }
    }

    private fun ensureInitialized(context: Context) {
        if (isInitialized) return
        // 惰性初始化，不预热，且若非显式调用无需注册额外回调（避免多次）
        internalInit(context, registerCallbacks = true, prewarm = false)
    }

    private fun requireMainThread() {
        check(Looper.getMainLooper() == Looper.myLooper()) { "Must be called on main thread" }
    }

    private fun appContext(): Context = appContextRef?.get()
        ?: throw IllegalStateException("WebViewPool not initialized or context GC'ed")

    /** 获取可复用 WebView（主线程）。 */
    fun acquire(context: Context): BaseWebView {
        requireMainThread()
        ensureInitialized(context)
        val targetCtx = (context.applicationContext ?: context)
        return (mWebQueue.poll() ?: BaseWebView(MutableContextWrapper(targetCtx))).apply {
            val webContext = this.context
            if (webContext is MutableContextWrapper) {
                webContext.baseContext = targetCtx
            }
            this.reuse()
        }
    }

    /** 回收 WebView（主线程）。 */
    fun recycle(webView: BaseWebView) {
        requireMainThread()
        if (!isInitialized) {
            // 尚未初始化，直接销毁防止引用泄漏
            webView.destroy(); return
        }
        try {
            var success = true
            webView.apply {
                success = reset()
                val webContext = this.context
                if (webContext is MutableContextWrapper) {
                    // 还原为应用上下文，避免持有 Activity
                    webContext.baseContext = appContext()
                }
            }
            if (recyclable && success) {
                if (!mWebQueue.offer(webView)) webView.destroy()
            } else {
                webView.destroy()
            }
        } catch (e: Throwable){
           logger_e(TAG,e.message.orEmpty())
            try { webView.destroy() } catch (_: Throwable) {}
        }
    }

    /** 释放池中所有 WebView（主线程）。 */
    fun clear() {
        if (!isInitialized) return
        requireMainThread()
        do {
            val webView = mWebQueue.poll()
            webView?.destroy()
        } while (webView != null)
    }

    /** 彻底关闭池（测试或退出使用）。 */
    fun shutdown() {
        synchronized(this) {
            if (!isInitialized) return
            clear()
            appContextRef = null
            isInitialized = false
        }
    }

}