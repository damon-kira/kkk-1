package com.colombia.credit.module.webview

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import com.colombia.credit.R
import com.colombia.credit.app.AppEnv
import com.colombia.credit.databinding.FragmentWebviewBinding
import com.colombia.credit.view.BaseWebView
import com.common.lib.base.BaseFragment
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBar
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.expand.isEmpty
import com.util.lib.log.logger_e
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebViewFragment : BaseFragment(), View.OnKeyListener, IWebHost {

    companion object {
        const val EXTRA_URL = "url"
    }

    var inflate: View? = null
    private val mJsInterfaceName: String = "control"

    private val mJsInterface by lazy {
        JsInterface(this)
    }

    private var isLoadError: Boolean = false

    private var isLoadComplete = false // 是否加载完成

    var mWebView: BaseWebView? = null
    private var isInterceptWebViewBackKey = false

    private var mUrl = ""
    private var isTitleShow = true

    private val mBinding by binding(FragmentWebviewBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(mJsInterface)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            return inflater.inflate(R.layout.fragment_webview, container, false)
        } catch (e: Exception) {
            finish()
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isInterceptWebViewBackKey =
            arguments?.getBoolean(WebViewActivity.EXTRA_INTERCEPT_WEBVIEW_BACK_KEY) ?: false
//        isTitleShow = arguments?.getBoolean(WebViewActivity.EXTRA_SHOW_TITLE) ?: true
//        tl_toolbar?.visibility = if (isTitleShow) View.VISIBLE else View.GONE
//        tl_toolbar.setOnbackListener {
//            val activity = activity
//            if (activity != null) {
//                activity.onBackPressed()
//            } else {
//                onFragmentBackPressed()
//            }
//        }


        try {
            mWebView = WebViewPool.INSTANCE.acquire(getSupportContext())
//            mWebView = BaseWebView(getSupportContext())
            mBinding.flWebview.addView(
                mWebView,
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        } catch (e: Exception) {
            finish()
        }
        webViewSetting(mWebView)
        val url = arguments?.getString(EXTRA_URL).orEmpty()
        mUrl = url
        mWebView?.loadUrl(url)
        setStatusBarColor(isTitleShow, url)
    }


    override fun generateShortLink(
        template: String,
        inviteCode: String,
        channel: String,
        shareType: String
    ) {
//        val resultLiveData = AppFirebaseInfoHelper.getShortAFLink(
//            getSupportContext(),
//            template,
//            AppFirebaseInfoHelper.URI,
//            inviteCode,
//            channel,
//            shareType
//        )
//
//        resultLiveData?.observe(this) { inviteInfo ->
//            resultLiveData.removeObservers(this)
//            val inviteInfoJobj = GsonUtil.toJsonObject(inviteInfo)
//            val success = inviteInfoJobj?.get("success")?.asBoolean ?: false
//            val channel = inviteInfoJobj?.get("channel")?.toString().orEmpty()
//            val errorMsg = inviteInfoJobj?.get("errormessage")?.toString().orEmpty()
//            val linkData = inviteInfoJobj?.get("linkData")?.toString().orEmpty()
//            val jsonObj = JsonObject()
//            jsonObj.addProperty("code", if (success) 1 else 0)
//            jsonObj.addProperty("data", linkData)
//            jsonObj.addProperty("channel", channel)
//            jsonObj.addProperty("msg", errorMsg)
//            mWebView?.loadUrl("javascript:receiveAFLink('$jsonObj')")
//        }
    }

    private fun refreshNetView() {
//        if (view == null || isDestroyView()) {
//            CrashManager.reportException(NullPointerException("webViewFragment view is null, isDestroyView = ${isDestroyView()}"))
//            return
//        }
//
//        if (inflate == null) {
//            inflate = view_stub.inflate()
//
//            web_update.setBlockingOnClickListener {
//                showLoading()
//                reload()
//                refreshNetView()
//            }
//        }
//
//        if (!NetWorkUtil.isNetConnected(getSupportContext()) || isLoadError) {
//            if (!isTitleShow) {
//                tl_toolbar?.visibility = View.VISIBLE
//            }
//            ll_not_net.visibility = View.VISIBLE
//        } else {
//            if (!isTitleShow) {
//                tl_toolbar?.visibility = View.GONE
//            }
//            ll_not_net.visibility = View.GONE
//        }
    }

    fun reload() {
        mWebView?.reload()
    }

    private fun setStatusBarColor(titleShow: Boolean, url: String?) {
//        if (!titleShow || url == H5UrlManager.URL_NEW_USER_GUIDE) {
//            activity?.setStatusBar(
//                isFullScreen = false,
//                color = R.color.colorPrimary,
//                isDark = false
//            )
//        } else {
            activity?.setStatusBar(isFullScreen = false, color = R.color.white)
//        }
    }


    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (goBack()) return true
            }
        }
        return false
    }


    private fun webViewSetting(webView: WebView?) {
        webView ?: return
//        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (AppEnv.DEBUG) {
                    logger_e(TAG, "onReceivedTitle = $title")
                }
                if (isLoadError) return
                title?.let {
                    if (it.startsWith("http") || isEmpty(it)) return
                }
//                tl_toolbar?.setText(title.orEmpty())
            }

//            override fun onShowFileChooser(
//                webView: WebView?,
//                filePathCallback: ValueCallback<Array<Uri>>?,
//                fileChooserParams: FileChooserParams?
//            ): Boolean {
//                return mZCFileHelper.onShowFileChooser(webView, filePathCallback, fileChooserParams)
//            }
        }

        webView.setOnKeyListener(this)
        webView.addJavascriptInterface(mJsInterface, mJsInterfaceName)
        webView.webViewClient = MyWebViewClient()
    }


    private fun goBack(): Boolean {
        mWebView?.let {
            if (it.canGoBack()) {
                it.goBack()
                return true
            }
        }
        return false
    }

    override fun onFragmentBackPressed(): Boolean {
        super.onFragmentBackPressed()
        if (isInterceptWebViewBackKey) {
            return false
        }
        if (goBack()) {
            return true
        }
        return false
    }

    override fun toolbarGone(toolbarBgColor: String, isDark: Boolean) {
//        tl_toolbar?.visibility = View.GONE
        try {
            getBaseActivity()?.setStatusBarColor(
                color = Color.parseColor(toolbarBgColor),
                isDark = isDark
            )
        } catch (e: Exception) {
        }
    }

    inner class MyWebViewClient : WebViewClient() {
//        private val protocolWebViewLoader = ProtocolWebViewLoader()
//
//        override fun shouldInterceptRequest(
//            view: WebView?,
//            request: WebResourceRequest?
//        ): WebResourceResponse? {
//            return protocolWebViewLoader.shouldInterceptRequest(request?.url)
//        }
//
//        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
//            return protocolWebViewLoader.shouldInterceptRequest(url?.let { Uri.parse(it) })
//        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            isLoadComplete = false
            isLoadError = false
            refreshNetView()
            showLoading()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            isLoadComplete = true
            hideLoading()
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            isLoadComplete = true
            hideLoading()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                if (!isResourceUrl(view?.url.toString(), request?.url.toString())) {
                    isLoadError = true
                }
            } else {
                isLoadError = true
            }

            refreshNetView()
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            isLoadComplete = true
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                val statusCode = errorResponse?.statusCode
                if (!isResourceUrl(
                        view?.url,
                        request?.url?.toString()
                    ) && (statusCode == 404 || statusCode == 500)
                ) {
                    // 显示异常页面
                    isLoadError = true
                }
            } else {
                isLoadError = true
            }
            refreshNetView()
        }
    }

    /**
     * 是否是加载资源的URL
     * @param loadUrl WebView加载的Url
     * @param requestUrl 加载的Url
     */
    private fun isResourceUrl(loadUrl: String?, requestUrl: String?): Boolean =
        loadUrl != requestUrl

    override fun onDestroyView() {
        super.onDestroyView()
        mWebView?.let {
            WebViewPool.INSTANCE.recycle(it)
            mWebView = null
        }
        System.gc()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(mJsInterface)
    }


}