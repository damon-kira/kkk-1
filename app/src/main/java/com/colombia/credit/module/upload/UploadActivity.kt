package com.colombia.credit.module.upload

import android.os.Bundle
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.databinding.ActivityUploadBinding
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.common.lib.livedata.LiveDataBus
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityUploadBinding>()

    private val mViewModel by lazyViewModel<UploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqKycInfo()
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun uploadSuccess() {
        LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
        Launch.skipMainActivity(this)
    }

    override fun initObserver() {}

    override fun getNextType(): Int = 0
}