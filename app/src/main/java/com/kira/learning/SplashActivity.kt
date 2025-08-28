package com.kira.learning

import android.os.Bundle
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.kira.learning.databinding.ActivitySplashBinding
import com.kira.learning.manager.Launch
import com.kira.learning.permission.PermissionHelper
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

//        SerManager.getCustom() // 数据上传

//        LanguageUtils.applyLanguage(this, Locale.CHINESE.language)
//        recreate()
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