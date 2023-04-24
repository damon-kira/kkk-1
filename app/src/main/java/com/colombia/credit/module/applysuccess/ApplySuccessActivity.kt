package com.colombia.credit.module.applysuccess

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.R
import com.colombia.credit.databinding.ActivityApplySuccessBinding
import com.colombia.credit.expand.isGpAccount
import com.colombia.credit.expand.isRepeat
import com.colombia.credit.manager.Launch
import com.colombia.credit.permission.HintDialog
import com.common.lib.base.BaseActivity
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import com.util.lib.dp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApplySuccessActivity : BaseActivity() {

    private val mBinding by binding<ActivityApplySuccessBinding>()

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

        if (!isRepeat && !isGpAccount()) {
            mHintDialog.setOnClickListener {
                Launch.skipAppStore(null)
            }
            mHintDialog.show()
        }
    }

    override fun onBackPressed() {
        Launch.skipMainActivity(this)
        super.onBackPressed()
    }
}