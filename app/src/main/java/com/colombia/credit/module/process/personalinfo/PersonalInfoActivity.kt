package com.colombia.credit.module.process.personalinfo

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqPersonalInfo
import com.colombia.credit.bean.resp.RspPersonalInfo
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

    private val mEducation by lazy(LazyThreadSafetyMode.NONE) {
        DictionaryUtil.getEducationData()
    }
    private val mMarriage by lazy(LazyThreadSafetyMode.NONE) {
        DictionaryUtil.getMaritalData()
    }

    private val mAddrDialog by lazy(LazyThreadSafetyMode.NONE) {
        AddressSelectorDialog(this).also {
            it.setSelectorListener { address ->
                address?.let {
                    mBinding.bivAddress.setViewText("${address.cingorium},${address.trophful}")
                }
            }
        }
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
        mViewModel.getCacheInfo()?.also { info ->
            info as ReqPersonalInfo
            mBinding.bivEmail.setViewText(info.unH4I2vHXG.orEmpty())
            if (!info.QlCvCLnNx.isNullOrEmpty() && !info.woTVOe.isNullOrEmpty()) {
                mBinding.bivAddress.setViewText(info.QlCvCLnNx.orEmpty() + "," + info.woTVOe)
            }
            mBinding.bivAddrDetail.setViewText(info.lh3bJ.orEmpty())
            val education = info.zgGtVHl9N2
            if (mEducation.containsKey(education)) {
                setBaseInfo(mBinding.bivEducation, mEducation[info.zgGtVHl9N2], info.zgGtVHl9N2)
            }
            val marriage = info.m7pyaSk
            if (mMarriage.containsKey(marriage)) {
                setBaseInfo(mBinding.bivMarriage, mMarriage[marriage], marriage)
            }
        }

        mViewModel.mAddrLiveData.observerNonSticky(this) { list ->
            if (mAddrDialog.isShowing || list.isNullOrEmpty()) return@observerNonSticky
            mAddrDialog.setAddressInfo(list).show()
        }

        mViewModel.mInfoLiveData.observerNonSticky(this) {info ->
            if (info !is RspPersonalInfo) return@observerNonSticky

            mBinding.bivEmail.setViewText(info.OloW.orEmpty())
            if (!info.tKgYzqB7yP.isNullOrEmpty() && !info.ZzBVPho.isNullOrEmpty()) {
                mBinding.bivAddress.setViewText(info.tKgYzqB7yP.orEmpty() + "," + info.ZzBVPho)
            }
            mBinding.bivAddrDetail.setViewText(info.fomX9KPzpi.orEmpty())
            val education = info.rtA8s2HSB
            if (mEducation.containsKey(education)) {
                setBaseInfo(mBinding.bivEducation, mEducation[info.rtA8s2HSB], info.rtA8s2HSB)
            }
            val marriage = info.wXlWnOHPzK
            if (mMarriage.containsKey(marriage)) {
                setBaseInfo(mBinding.bivMarriage, mMarriage[marriage], marriage)
            }
        }
        mViewModel.getInfo()
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
                mViewModel.getAddrInfo()
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