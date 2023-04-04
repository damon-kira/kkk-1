package com.colombia.credit.module.process.personalinfo

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.PersonalInfo
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.dialog.AddressSelectorDialog
import com.colombia.credit.expand.checkEmailFormat
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.util.DictionaryUtil
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityPersonalInfoBinding>()

    private val mEducation by lazy {
        DictionaryUtil.getEducationData()
    }
    private val mMarriage by lazy {
        DictionaryUtil.getMaritalData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setToolbarListener(mBinding.processToolbar)
        mBinding.bivEducation.setBlockingOnClickListener(this)
        mBinding.bivAddress.setBlockingOnClickListener(this)
        mBinding.bivMarriage.setBlockingOnClickListener(this)
        mBinding.tvCommit.setBlockingOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.biv_education -> {
                showProcessSelectorDialog(
                    getString(R.string.education),
                    mEducation,
                    mBinding.bivEducation.tag?.toString().orEmpty()
                ) {
                    mBinding.bivEducation.setViewText(it.value)
                    mBinding.bivEducation.tag = it.key
                }
            }
            R.id.biv_address -> {
                AddressSelectorDialog(this)
                    .setAddressInfo(arrayListOf()).show()
            }
            R.id.biv_marriage -> {
                showProcessSelectorDialog(
                    getString(R.string.education),
                    mMarriage,
                    mBinding.bivMarriage.tag?.toString().orEmpty()
                ) {
                    mBinding.bivMarriage.setViewText(it.value)
                    mBinding.bivMarriage.tag = it.key
                }
            }
            R.id.tv_commit -> {
                uploadInfo()
            }
        }
    }

    override fun getCommitInfo(): IBaseInfo {
        val email = mBinding.bivEmail.getViewText()
        val education = mBinding.bivEducation.tag
        val address = mBinding.bivAddress.getViewText()
        val addrDetail = mBinding.bivAddrDetail.getViewText()
        val marriage = mBinding.bivMarriage.tag
        return PersonalInfo().also {

        }
    }

    override fun checkCommitInfo(): Boolean {
        val checkEmail = checkEmailFormat(mBinding.bivEmail.getViewText())
        if (!checkEmail) {
            mBinding.bivEmail.setError(
                getString(
                    R.string.error_process_hint,
                    mBinding.bivEmail.getTitle()
                )
            )
        }
        return checkEmail.and(checkAndSetErrorHint(mBinding.bivEducation))
            .and(checkAndSetErrorHint(mBinding.bivAddress))
            .and(checkAndSetErrorHint(mBinding.bivAddrDetail, getString(R.string.address_detail_title)))
            .and(checkAndSetErrorHint(mBinding.bivMarriage))
    }

}