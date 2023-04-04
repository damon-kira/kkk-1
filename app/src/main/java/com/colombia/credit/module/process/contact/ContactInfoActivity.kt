package com.colombia.credit.module.process.contact

import android.os.Bundle
import com.colombia.credit.bean.resp.ContactInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.IdentityInfo
import com.colombia.credit.databinding.ActivityContactInfoBinding
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactInfoActivity: BaseProcessActivity() {

    private val mBinding by binding<ActivityContactInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setToolbarListener(mBinding.processToolbar)
    }


    override fun checkCommitInfo(): Boolean {
        return false
    }

    override fun getCommitInfo(): IBaseInfo {
        return ContactInfo()
    }
}