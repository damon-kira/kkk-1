package com.colombia.credit

import android.os.Bundle
import android.os.Handler
import com.bigdata.lib.LocationHelp
import com.colombia.credit.Launch.jumpToAppSettingPage
import com.colombia.credit.databinding.ActivitySplashBinding
import com.colombia.credit.permission.PermissionHelper
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.StatusBarUtil.setStatusBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val mBinding by binding<ActivitySplashBinding>()

    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBar(true, R.color.white, true)
        MainHandler.postDelay({
            reqPermission()
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun reqPermission() {
        PermissionHelper.showDialogIfNeed(this, {
            Launch.skipMainActivity(this)
            finish()
        }, {
            Launch.skipMainActivity(this)
            finish()
//            jumpToAppSettingPage()
        })
    }
}