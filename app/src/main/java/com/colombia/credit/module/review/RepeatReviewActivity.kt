package com.colombia.credit.module.review

import android.graphics.Color
import android.os.Bundle
import com.colombia.credit.databinding.ActivityReviewBinding
import com.colombia.credit.expand.showCustomDialog
import com.common.lib.base.BaseActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

// 复贷审核中
@AndroidEntryPoint
class RepeatReviewActivity: BaseActivity() {

    private val mBinding by binding<ActivityReviewBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(Color.WHITE, true)
        mBinding.toolbar.setOnbackListener {
            finish()
        }

        mBinding.etvBtn.setBlockingOnClickListener {
            showCustomDialog()
        }
    }
}