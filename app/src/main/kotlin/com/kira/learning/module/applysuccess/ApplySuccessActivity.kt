package com.kira.learning.module.applysuccess

import android.graphics.Color
import android.os.Bundle
import com.kira.learning.R
import com.kira.learning.databinding.ActivityApplySuccessBinding
import com.kira.learning.expand.isGpAccount
import com.kira.learning.expand.isRepeat
import com.kira.learning.manager.Launch
import com.kira.learning.module.config.ConfigViewModel
import com.kira.learning.permission.HintDialog
import com.common.lib.base.BaseActivity
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApplySuccessActivity : BaseActivity() {

    private val mBinding by binding<ActivityApplySuccessBinding>()

    private val mViewModel by lazyViewModel<ConfigViewModel>()

    private val mHintDialog by lazy {
        HintDialog(this).also {
            it.setBtnText(getString(R.string.gp_btn))
            it.setTitleText(getString(R.string.gp_title))
            it.setMessage(getString(R.string.gp_msg))
            it.setIcon(R.drawable.ic_zan)
            it.updateBtnPadding(left = 16f.dp(), right = 16f.dp())
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setStatusBarColor(Color.WHITE, true)

        setViewModelLoading(mViewModel)
        mViewModel.configLiveData.observerNonSticky(this) {
            if (it.isOpen()) {
                showDialog()
            }
        }
        mViewModel.getConfig(ConfigViewModel.KEY_GP_PJ)
    }

    private fun showDialog() {
        if (!isRepeat && !isGpAccount()) {
            mHintDialog.setOnClickListener {
                Launch.skipAppStore(null)
            }
            mHintDialog.show()
        }
    }

    override fun onBackPressed() {
        Launch.skipXMLMainActivity(this)
        super.onBackPressed()
    }
}