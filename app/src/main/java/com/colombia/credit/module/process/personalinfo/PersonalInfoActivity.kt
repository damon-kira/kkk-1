package com.colombia.credit.module.process.personalinfo

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqPersonalInfo
import com.colombia.credit.bean.resp.RspPersonalInfo
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.dialog.AddressSelectorDialog
import com.colombia.credit.dialog.FirstLoanHintDialog
import com.colombia.credit.expand.STEP3
import com.colombia.credit.expand.isNewUser
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.view.baseinfo.BaseInfoView
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
    private val mJobType by lazy {
        DictionaryUtil.getJobTypeData()
    }
    private val mJobIncomeSource by lazy {
        DictionaryUtil.getIncomeSourceData()
    }

    private val mAddrDialog by lazy(LazyThreadSafetyMode.NONE) {
        AddressSelectorDialog(this).also {
            it.setSelectorListener { address ->
                address?.let {
                    mBinding.bivAddress.setViewText("${address.cingorium},${address.trophful}")
                }
                mAutoHelper.startCheckNext()
            }
        }
    }

    private val mFirstDialog: FirstLoanHintDialog by lazy {
        FirstLoanHintDialog(this)
            .also {
                it.setOnClickListener { }
                it.setOnDismissListener {
                    mAutoHelper.startCheckNext()
                }
            }
    }

    private val mAutoHelper by lazy {
        object : PersonalAutoHelper(mBinding, !isNewUser) {
            override fun showItemDialog(index: Int) {
                when (index) {
                    ITEM_JOB_TYPE -> onClick(mBinding.bivType)
                    ITEM_EDUCATION -> onClick(mBinding.bivEducation)
                    ITEM_MARRIAGE -> onClick(mBinding.bivMarriage)
                    ITEM_INCOME -> onClick(mBinding.bivIncome)
                    ITEM_ADDR -> onClick(mBinding.bivAddress)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbarListener(mBinding.processToolbar)
        initView()
        initCache()
        mViewModel.getInfo()

        if (isNewUser) {
            mFirstDialog.show()
        }
    }

    private fun initView() {
        mBinding.bivType.setBlockingOnClickListener(this)
        mBinding.bivEducation.setBlockingOnClickListener(this)
        mBinding.bivMarriage.setBlockingOnClickListener(this)
        mBinding.bivIncome.setBlockingOnClickListener(this)
        mBinding.bivAddress.setBlockingOnClickListener(this)
        mBinding.tvCommit.setBlockingOnClickListener(this)
    }

    private fun initCache() {
        mViewModel.getCacheInfo()?.also { info ->
            info as ReqPersonalInfo
            val jobType = info.kBHCAbhdwbh
            if (mJobType.containsKey(jobType)) {
                setBaseInfo(mBinding.bivType, mJobType[jobType], jobType)
            }
            val education = info.TEAVvgsvgqs
            if (mEducation.containsKey(education)) {
                setBaseInfo(mBinding.bivEducation, mEducation[info.TEAVvgsvgqs], info.TEAVvgsvgqs)
            }
            val marriage = info.oiwnusx
            if (mMarriage.containsKey(marriage)) {
                setBaseInfo(mBinding.bivMarriage, mMarriage[marriage], marriage)
            }
            val income = info.dGCAVvsgw23ds
            if (mJobIncomeSource.containsKey(income)) {
                setBaseInfo(mBinding.bivIncome, mJobIncomeSource[income], income)
            }
            if (!info.uwahBHDbws.isNullOrEmpty() && !info.pwonBHSWASA.isNullOrEmpty()) {
                mBinding.bivAddress.setViewText(info.uwahBHDbws.orEmpty() + "," + info.pwonBHSWASA)
            }
        }
    }

    override fun initObserver() {
        mViewModel.mAddrLiveData.observerNonSticky(this) { list ->
            if (mAddrDialog.isShowing || list.isNullOrEmpty()) return@observerNonSticky
            mAddrDialog.setAddressInfo(list).show()
        }

        mViewModel.mInfoLiveData.observerNonSticky(this) { rspInfo ->
            if (rspInfo !is RspPersonalInfo) return@observerNonSticky
            rspInfo.ovabhwbahsSBHs?.let { info ->
                val jobType = info.tavwgVGSVnsdj
                if (mJobType.containsKey(jobType)) {
                    setBaseInfo(mBinding.bivType, mJobType[jobType], jobType)
                }
                val education = info.BVbhbhaBHDas
                if (mEducation.containsKey(education)) {
                    setBaseInfo(mBinding.bivEducation, mEducation[info.BVbhbhaBHDas], info.BVbhbhaBHDas)
                }
                val marriage = info.twavgVGDEWE2HBS
                if (mMarriage.containsKey(marriage)) {
                    setBaseInfo(mBinding.bivMarriage, mMarriage[marriage], marriage)
                }
                val income = info.vVGVAgxvsa
                if (mJobIncomeSource.containsKey(income)) {
                    setBaseInfo(mBinding.bivIncome, mJobIncomeSource[income], income)
                }
                if (!info.mbchaBHDE2DSsj.isNullOrEmpty() && !info.oiawasVSV.isNullOrEmpty()) {
                    mBinding.bivAddress.setViewText(info.mbchaBHDE2DSsj.orEmpty() + "," + info.oiawasVSV)
                }
            }
        }
    }


    override fun onClick(v: View?) {
        v ?: return
        mAutoHelper.clearFocus()
        when (v.id) {
            R.id.biv_education -> {
                showSelectorDialog(
                    getString(R.string.education),
                    mEducation,
                    mBinding.bivEducation.tag?.toString().orEmpty(),
                    mBinding.bivEducation
                )
            }
            R.id.biv_address -> {
                mViewModel.getAddrInfo()
            }
            R.id.biv_marriage -> {
                showSelectorDialog(
                    getString(R.string.marriage),
                    mMarriage,
                    mBinding.bivMarriage.tag?.toString().orEmpty(),
                    mBinding.bivMarriage
                )
            }
            R.id.biv_type -> {
                showSelectorDialog(
                    getString(R.string.work_type),
                    mJobType,
                    mBinding.bivType.tag?.toString(),
                    mBinding.bivType
                )
            }
            R.id.biv_income -> {
                showSelectorDialog(
                    getString(R.string.work_month_income),
                    mJobIncomeSource,
                    mBinding.bivIncome.tag?.toString(),
                    mBinding.bivIncome
                )
            }
            R.id.tv_commit -> {
                uploadInfo()
            }
        }
    }

    private fun showSelectorDialog(
        title: String,
        data: MutableMap<String, String>,
        selectorTag: String? = null,
        baseInfoView: BaseInfoView
    ) {
        showProcessSelectorDialog(
            title,
            data,
            selectorTag
        ) {
            baseInfoView.setViewText(it.value)
            baseInfoView.tag = it.key
            mAutoHelper.startCheckNext()
        }
    }

    override fun getCommitInfo(): IReqBaseInfo {
        val address = mBinding.bivAddress.getViewText()
        val addressArray = address.split(",")
        return ReqPersonalInfo().also {
            it.kBHCAbhdwbh = mBinding.bivType.tag?.toString()
            it.dGCAVvsgw23ds = mBinding.bivIncome.tag?.toString()
            it.oiwnusx = mBinding.bivMarriage.tag?.toString().orEmpty() // 婚姻
            it.TEAVvgsvgqs = mBinding.bivEducation.tag?.toString().orEmpty() // 教育
            if (addressArray.size > 1) {
                it.uwahBHDbws = addressArray[0] // 省份
                it.pwonBHSWASA = addressArray[1]// 市区
            }
        }
    }

    override fun checkCommitInfo(): Boolean {
        return checkAndSetErrorHint(mBinding.bivType)
            .and(checkAndSetErrorHint(mBinding.bivEducation))
            .and(checkAndSetErrorHint(mBinding.bivMarriage))
            .and(checkAndSetErrorHint(mBinding.bivIncome))
            .and(checkAndSetErrorHint(mBinding.bivAddress))
            .and(mBinding.bivAddress.getViewText().contains(","))
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = STEP3
}