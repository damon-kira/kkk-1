package com.kira.learning.xml.modules.webview

import android.os.Bundle
import com.kira.learning.databinding.ActivityWebviewBinding
import com.common.lib.base.BaseActivity
import com.common.lib.helper.FragmentHelper
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebViewActivity : BaseActivity() {

    companion object {
        const val EXTRA_URL = "url"
        const val EXTRA_INTERCEPT_WEBVIEW_BACK_KEY = "intercept_webview_back_key"
    }

    private var mCurrTag: String? = null

    private val mBinding by binding<ActivityWebviewBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        val webViewFragment = WebViewFragment()
        val bundle = Bundle()
        bundle.putString(EXTRA_URL, intent.getStringExtra(EXTRA_URL))
        bundle.putBoolean(
            EXTRA_INTERCEPT_WEBVIEW_BACK_KEY,
            intent.getBooleanExtra(EXTRA_INTERCEPT_WEBVIEW_BACK_KEY, false)
        )

        webViewFragment.arguments = bundle
        mCurrTag = FragmentHelper.switchFragment(
            supportFragmentManager,
            mBinding.flWebviewContainer.id,
            webViewFragment,
            mCurrTag
        )
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = FragmentHelper.getCurrFragment(
            supportFragmentManager,
            mBinding.flWebviewContainer.id,
            mCurrTag
        )
        if (fragment?.onFragmentBackPressed() == false) {
            finish()
        }
    }
}