package com.colombia.credit.module.upload

import android.os.Bundle
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqKycInfo
import com.colombia.credit.databinding.ActivityUploadBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.IBaseProcessViewModel
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity : BaseProcessActivity() {

    private val mBinding by binding<ActivityUploadBinding>()

    private val mViewModel by lazyViewModel<UploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqKycInfo()
    }

    override fun getViewModel(): IBaseProcessViewModel = mViewModel
}