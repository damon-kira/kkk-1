package com.colombia.credit

import android.os.Bundle
import android.os.Handler
import com.colombia.credit.databinding.ActivitySplashBinding
import com.colombia.credit.expand.showAppUpgradeDialog
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.appupdate.AppUpdateViewModel
import com.colombia.credit.module.upload.UploadViewModel
import com.colombia.credit.permission.PermissionHelper
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.MainHandler
import com.util.lib.StatusBarUtil.setStatusBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val mBinding by binding<ActivitySplashBinding>()

    private val mViewModel by lazyViewModel<AppUpdateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBar(true, R.color.white, true)
        mViewModel.updateLiveData.observerNonSticky(this) {
            showAppUpgradeDialog(it)
        }
        mViewModel.getAppUpdate()

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
            Launch.skipMainActivity(this)
            finish()
        }, {
            Launch.skipMainActivity(this)
            finish()
        })
    }
}