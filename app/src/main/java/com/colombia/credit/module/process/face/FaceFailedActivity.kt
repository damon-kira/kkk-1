package com.colombia.credit.module.process.face

import android.os.Bundle
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqFaceInfo
import com.colombia.credit.databinding.ActivityFaceFailedBinding
import com.colombia.credit.expand.STEP6
import com.colombia.credit.expand.getStatusBarColor
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import com.util.lib.StatusBarUtil.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaceFailedActivity : BaseProcessActivity() {

    private val binding by binding<ActivityFaceFailedBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(getStatusBarColor(), true)
        binding.toolbar.setOnbackListener {
            finish()
        }
        binding.failedTvRetry.setBlockingOnClickListener {
            Launch.skipFaceActivity(this)
            finish()
        }
    }

    override fun getNextType(): Int = STEP6

    override fun initObserver() {}

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqFaceInfo()
    }

    override fun getViewModel(): BaseProcessViewModel? = null

}