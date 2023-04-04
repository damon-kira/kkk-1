package com.colombia.credit.module.upload

import android.os.Bundle
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.IdentityInfo
import com.colombia.credit.databinding.ActivityUploadBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadActivity: BaseProcessActivity() {

    private val mBinding by binding<ActivityUploadBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IBaseInfo {
        return IdentityInfo()
    }
}