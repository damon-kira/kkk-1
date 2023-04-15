package com.colombia.credit

import android.os.Bundle
import com.colombia.credit.databinding.ActivitySplashBinding
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.service.SerManager
import com.colombia.credit.permission.PermissionHelper
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.StatusBarUtil.setStatusBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity() {


    private val mBinding by binding<ActivitySplashBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBar(true, R.color.white, true)

        SerManager.getCustom()

        MainHandler.postDelay({
            reqPermission()
        }, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainHandler.removeAll()
    }

    private fun reqPermission() {
        PermissionHelper.showDialogIfNeed(this, {
            next()
        }, {
            next()
        })
    }

    private fun next() {
        Launch.skipMainActivity(this)
        finish()
    }
}