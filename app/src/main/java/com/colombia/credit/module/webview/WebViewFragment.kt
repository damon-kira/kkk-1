package com.colombia.credit.module.webview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import com.bigdata.lib.readPrivacyCount
import com.bigdata.lib.readPrivacyTime
import com.colombia.credit.R
import com.colombia.credit.app.AppEnv
import com.colombia.credit.databinding.FragmentWebviewBinding
import com.colombia.credit.databinding.LayoutNetErrorBinding
import com.colombia.credit.manager.H5UrlManager
import com.colombia.credit.module.defer.PayEvent
import com.colombia.credit.view.BaseWebView
import com.common.lib.base.BaseFragment
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.NetWorkUtils
import com.util.lib.StatusBarUtil.setStatusBar
import com.util.lib.expand.isEmpty
import com.util.lib.hide
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebViewFragment : BaseFragment(), View.OnKeyListener, IWebHost {

    companion object {
        const val EXTRA_URL = "url"
    }

    var inflate: View? = null
    private val mJsInterfaceName: String = "app"

    private val mJsInterface by lazy {
        JsInterface(this)
    }

    private var isLoadError: Boolean = false

    private var isLoadComplete = false // 是否加载完成

    var mWebView: BaseWebView? = null
    private var isInterceptWebViewBackKey = false

    private var mUrl = ""
    private var isTitleShow = true

    private var mEnterTime: Long = 0

    private val mBinding by binding(FragmentWebviewBinding::inflate)

    private val mFileHelper by lazy(LazyThreadSafetyMode.NONE) {
        FileHelper(this)
    }

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
            return mBinding.root
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
        mBinding.toolbar.setOnbackListener {
            val activity = activity
            if (activity != null) {
                activity.onBackPressed()
            } else {
                onFragmentBackPressed()
            }
        }


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
        var url = arguments?.getString(EXTRA_URL).orEmpty()
        logger_d(TAG, "webview url =$url")
        mUrl = url
        mWebView?.loadUrl(url)
        setStatusBarColor(isTitleShow, url)

        if (url == H5UrlManager.URL_PRIVACY) {
            readPrivacyCount++
            mEnterTime = System.currentTimeMillis()
        }
    }


    private fun refreshNetView() {
//        if (view == null || isDestroyView()) {
//            CrashManager.reportException(NullPointerException("webViewFragment view is null, isDestroyView = ${isDestroyView()}"))
//            return
//        }

        mBinding.layoutError.etvUpdate.setBlockingOnClickListener {
            showLoading()
            reload()
            refreshNetView()
        }

        if (!NetWorkUtils.isNetConnected(getSupportContext()) || isLoadError) {
            mBinding.layoutError.llError.show()
        } else {
            mBinding.layoutError.llError.hide()
        }
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


    @SuppressLint("JavascriptInterface")
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
                mBinding.toolbar.setText(title.orEmpty())
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                return mFileHelper.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }
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

    inner class MyWebViewClient : WebViewClient() {
//        private val protocolWebViewLoader = ProtocolWebViewLoader()

//        override fun shouldInterceptRequest(
//            view: WebView?,
//            request: WebResourceRequest?
//        ): WebResourceResponse? {
//            return super.shouldInterceptRequest(view, request)
//        }

//        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
//            return  super.shouldInterceptRequest(view, url)
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
            logger_e(TAG, "onReceivedError = $error")
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
            logger_e(TAG, "onReceivedHttpError = ${errorResponse.toString()}")
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

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)

            getBaseActivity()?.let { activy ->
                val builder = AlertDialog.Builder(activy)
                    .setMessage(getString(R.string.webview_ssl_hint))
                    .setPositiveButton(
                        getString(R.string.confirm)
                    ) { _, _ -> handler?.proceed() }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        handler?.cancel()
                    }.setOnKeyListener { dialog, keyCode, event ->
                        return@setOnKeyListener if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                            handler?.cancel()
                            dialog.dismiss()
                            true
                        } else false
                    }
                try {
                    builder.create().show()
                } catch (e: Exception) {
                }
            }
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
        if (mUrl == H5UrlManager.URL_PRIVACY) {
            readPrivacyTime = System.currentTimeMillis() - mEnterTime
        }
        if (this.mUrl.contains(H5UrlManager.URL_PAY)) {
            LiveDataBus.post(PayEvent(PayEvent.EVENT_EXIT))
        }
        mWebView?.let {
            WebViewPool.INSTANCE.recycle(it)
            mWebView = null
        }
        System.gc()
    }


    override fun loading(show: Boolean) {
        MainHandler.post {
            if (show) showLoading(true) else hideLoading()
        }
    }

    override fun exit() {
        finish()
    }
}