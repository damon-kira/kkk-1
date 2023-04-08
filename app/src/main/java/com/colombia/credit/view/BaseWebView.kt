package com.colombia.credit.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.colombia.credit.BuildConfig
import com.colombia.credit.app.AppEnv
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e

/**
 *Created by Sun Hewei
 *on 2019/11/13
 */
class BaseWebView : WebView {

    companion object {
        private const val TAG = "BaseWebView"
        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                context.createConfigurationContext(Configuration())
            } else {
                context
            }
        }
    }

    constructor(context: Context) : super(getFixedContext(context))
    constructor(context: Context, attrs: AttributeSet?) : super(getFixedContext(context), attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(getFixedContext(context), attrs, defStyleAttr)

    private val javascriptInterfaceNames = mutableListOf<String>()

    private val mProxyWebViewClient = ProxyWebViewClient()

    init {
        initSetting()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(AppEnv.DEBUG)
        }
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false//滑动条隐藏
        settings.javaScriptEnabled = true//启用javascript支持
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false//缩放按钮
        settings.useWideViewPort = true//支持可任意比例缩放
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        settings.textZoom = 100
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        settings.setAppCacheEnabled(false)
        val cachePath = context.filesDir.absolutePath + "/webview"
        settings.setAppCachePath(cachePath)
        settings.setAppCacheMaxSize(50 * 1024 * 1024)//50MB
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true//设置webview支持dom storage API
        settings.allowFileAccess = true//在webView内部是否允许访问文件
        settings.javaScriptCanOpenWindowsAutomatically = true//设置脚本是否允许自动打开弹窗
        settings.blockNetworkImage = false
        settings.setSupportMultipleWindows(false)

        //在webView中加载https的URL中不能加载http资源的bug
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        }
    }


    @SuppressLint("JavascriptInterface")
    override fun addJavascriptInterface(obj: Any, name: String) {
        super.addJavascriptInterface(obj, name)
        javascriptInterfaceNames.add(name)
    }

    override fun removeJavascriptInterface(name: String) {
        super.removeJavascriptInterface(name)
        javascriptInterfaceNames.remove(name)
    }


    override fun setWebViewClient(client: WebViewClient) {
        mProxyWebViewClient.setClient(client)
        super.setWebViewClient(mProxyWebViewClient)
    }

    fun reuse() {
        mProxyWebViewClient.setClearHistory(!url.isNullOrEmpty())
    }

    fun reset():Boolean {
        var result = true
        stopLoading()
        loadUrl("about:blank")
        clearHistory()

        parent?.let {
            if (it is ViewGroup) {
                logger_e(TAG, "从View tree 中移除WebView")
                try {
                    it.removeView(this)
                } catch (e:Throwable){
                    logger_e(TAG, e.message.orEmpty())
                    result = false
                }
            }
        }

        webChromeClient = null
        webViewClient = WebViewClient()

        clearAnimation()

        clearFormData()
        clearSslPreferences()
        setFindListener(null)
        setDownloadListener(null)
        setBackgroundColor(Color.WHITE)
        removeAllViewListener()
        removeAllViews()


        //清除所有 JavascriptInterface
        javascriptInterfaceNames.forEach { super.removeJavascriptInterface(it) }
        javascriptInterfaceNames.clear()

        //重新进行 setting设置
        initSetting()

        //为了复用 WebView, 不在此处调用 destroy()方法
        //destroy()
        return result
    }

    private fun removeAllViewListener() {
        setOnKeyListener(null)
        setOnClickListener(null)
        setOnTouchListener(null)
        setOnLongClickListener(null)
        setOnSystemUiVisibilityChangeListener(null)
        setOnDragListener(null)
        setOnHoverListener(null)
        setOnCreateContextMenuListener(null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setOnContextClickListener(null)
            setOnScrollChangeListener(null)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setOnCapturedPointerListener(null)
        }
        try {
            var tempClass: Class<in Nothing>? = this::class.java
            while (tempClass != null) {
                if (tempClass.isAssignableFrom(View::class.java)) {
                    break
                }
                tempClass = tempClass.superclass
            }
            val viewClass = tempClass!!
            val method = viewClass.getDeclaredMethod("getListenerInfo").apply { isAccessible = true }

            val listenerInfoObj = method.invoke(this)
            val listenerInfoClass = listenerInfoObj::class.java
            val listenerDeclaredFields = listenerInfoClass.declaredFields
            val listenerFields = listenerInfoClass.fields
            val listenerAllFields = listenerFields + listenerDeclaredFields
            val names = listOf("mOnLayoutChangeListeners", "mOnAttachStateChangeListeners", "mUnhandledKeyListeners")
            listenerAllFields.filter {
                names.contains(it.name)
            }.forEach {
                it.isAccessible = true
                val value = it.get(listenerInfoObj)
                if (value != null && value is MutableList<*>) {
                    value.clear()
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}


class ProxyWebViewClient : WebViewClient() {

    private var mClient: WebViewClient? = null

    private var clearHistory = false

    fun setClient(client: WebViewClient?) {
        mClient = client
    }

    fun setClearHistory(clear: Boolean) {
        clearHistory = clear
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        val client = mClient
        if (client == null) {
            super.onPageFinished(view, url)
        } else {
            client.onPageFinished(view, url)
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        val client = mClient
        return if (client == null) {
            super.shouldInterceptRequest(view, url)
        } else {
            client.shouldInterceptRequest(view, url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
        val client = mClient
        return if (client == null) {
            super.shouldInterceptRequest(view, request)
        } else {
            client.shouldInterceptRequest(view, request)
        }
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        return mClient?.shouldOverrideKeyEvent(view, event)
                ?: super.shouldOverrideKeyEvent(view, event)

    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onSafeBrowsingHit(view: WebView?, request: WebResourceRequest?, threatType: Int, callback: SafeBrowsingResponse?) {
        val client = mClient
        if (client == null) {
            super.onSafeBrowsingHit(view, request, threatType, callback)
        } else {
            client.onSafeBrowsingHit(view, request, threatType, callback)
        }

    }

    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        val client = mClient
        if (client == null) {
            super.doUpdateVisitedHistory(view, url, isReload)
        } else {
            client.doUpdateVisitedHistory(view, url, isReload)
        }
        if (clearHistory) {
            logger_e("ProxyWebViewClient", "doUpdateVisitedHistory  clearHistory")
            clearHistory = false
            view?.clearHistory()
        }
    }

    @Suppress("DEPRECATION")
    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        val client = mClient
        if (client == null) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        } else {
            client.onReceivedError(view, errorCode, description, failingUrl)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        val client = mClient
        if (client == null) {
            super.onReceivedError(view, request, error)
        } else {
            client.onReceivedError(view, request, error)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        return mClient?.onRenderProcessGone(view, detail)
                ?: super.onRenderProcessGone(view, detail)
    }

    override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
        val client = mClient
        return if (client == null) {
            super.onReceivedLoginRequest(view, realm, account, args)
        } else {
            client.onReceivedLoginRequest(view, realm, account, args)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
        val client = mClient
        if (client == null) {
            super.onReceivedHttpError(view, request, errorResponse)
        } else {
            client.onReceivedHttpError(view, request, errorResponse)
        }

    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        val client = mClient
        if (client == null) {
            super.onPageStarted(view, url, favicon)
        } else {
            client.onPageStarted(view, url, favicon)
        }
    }

    override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
        val client = mClient
        if (client == null) {
            super.onScaleChanged(view, oldScale, newScale)
        } else {
            client.onScaleChanged(view, oldScale, newScale)
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return mClient?.shouldOverrideUrlLoading(view, url)
                ?: super.shouldOverrideUrlLoading(view, url)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return mClient?.shouldOverrideUrlLoading(view, request)
                ?: super.shouldOverrideUrlLoading(view, request)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onPageCommitVisible(view: WebView?, url: String?) {
        val client = mClient
        if (client == null) {
            super.onPageCommitVisible(view, url)
        } else {
            client.onPageCommitVisible(view, url)
        }
    }

    override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
        val client = mClient
        if (client == null) {
            super.onUnhandledKeyEvent(view, event)
        } else {
            client.onUnhandledKeyEvent(view, event)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        val client = mClient
        if (client == null) {
            super.onReceivedClientCertRequest(view, request)
        } else {
            client.onReceivedClientCertRequest(view, request)
        }
    }

    override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
        val client = mClient
        if (client == null) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm)
        } else {
            client.onReceivedHttpAuthRequest(view, handler, host, realm)
        }
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        val client = mClient
        if (client == null) {
            super.onReceivedSslError(view, handler, error)
        } else {
            client.onReceivedSslError(view, handler, error)
        }
    }

    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        val client = mClient
        if (client == null) {
            super.onTooManyRedirects(view, cancelMsg, continueMsg)
        } else {
            client.onTooManyRedirects(view, cancelMsg, continueMsg)
        }
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        val client = mClient
        if (client == null) {
            super.onFormResubmission(view, dontResend, resend)
        } else {
            client.onFormResubmission(view, dontResend, resend)
        }
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        val client = mClient
        if (client == null) {
            super.onLoadResource(view, url)
        } else {
            client.onLoadResource(view, url)
        }
    }
}