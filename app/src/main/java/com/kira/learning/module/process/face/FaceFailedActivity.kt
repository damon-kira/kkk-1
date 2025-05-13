package com.kira.learning.module.process.face

import android.os.Bundle
import com.kira.learning.bean.req.IReqBaseInfo
import com.kira.learning.bean.req.ReqFaceInfo
import com.kira.learning.databinding.ActivityFaceFailedBinding
import com.kira.learning.expand.STEP6
import com.kira.learning.expand.getStatusBarColor
import com.kira.learning.manager.Launch
import com.kira.learning.module.process.BaseProcessActivity
import com.kira.learning.module.process.BaseProcessViewModel
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