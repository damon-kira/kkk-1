package com.colombia.credit.module.process.face

import android.os.Bundle
import com.colombia.credit.bean.resp.FaceInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.databinding.ActivityFaceFailedBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FaceFailedActivity : BaseProcessActivity() {

    private val binding by binding<ActivityFaceFailedBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setOnbackListener {
            finish()
        }
        binding.failedTvRetry.setBlockingOnClickListener {
            //重试
        }
    }

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IBaseInfo {
        return FaceInfo()
    }
}