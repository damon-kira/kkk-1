package com.colombia.credit.module.process.personalinfo

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqPersonalInfo
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.dialog.AddressSelectorDialog
import com.colombia.credit.expand.ShowErrorMsg
import com.colombia.credit.expand.TYPE_WORK
import com.colombia.credit.expand.checkEmailFormat
import com.colombia.credit.expand.jumpProcess
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.DictionaryUtil
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mBinding by binding<ActivityPersonalInfoBinding>()

    private val mViewModel by lazyViewModel<PersonalViewModel>()

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
        mViewModel.mUploadLiveData.observerNonSticky(this) {
            if (it.isSuccess()) {
                Launch.skipWorkInfoActivity(this)
            } else {
                it.ShowErrorMsg()
            }
        }

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
                    .setSelectorListener { address ->
                        mBinding.bivAddress.setViewText(address)
                    }.setAddressInfo(arrayListOf()).show()
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

    override fun getCommitInfo(): IReqBaseInfo {
        val address = mBinding.bivAddress.getViewText()
        val addressArray = address.split(",")
        return ReqPersonalInfo().also {
            it.unH4I2vHXG = mBinding.bivEmail.getViewText() // email
            it.m7pyaSk = mBinding.bivMarriage.tag.toString() // 婚姻
            it.zgGtVHl9N2 = mBinding.bivEducation.tag.toString() // 教育
            if (addressArray.size > 1) {
                it.QlCvCLnNx = addressArray[0] // 省份
                it.woTVOe = addressArray[1]// 市区
            }
            it.lh3bJ = mBinding.bivAddrDetail.getViewText() // 详细地址
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
            .and(mBinding.bivAddress.getViewText().contains(","))
            .and(
                checkAndSetErrorHint(
                    mBinding.bivAddrDetail,
                    getString(R.string.address_detail_title)
                )
            )
            .and(checkAndSetErrorHint(mBinding.bivMarriage))
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel

    override fun uploadSuccess() {
        jumpProcess(this, TYPE_WORK)
    }
}