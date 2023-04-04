package com.colombia.credit.module.process.kyc

import android.os.Bundle
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.IdentityInfo
import com.colombia.credit.databinding.ActivityKycInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding

class KycInfoActivity: BaseProcessActivity() {

    private val mBinding by binding<ActivityKycInfoBinding>()

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