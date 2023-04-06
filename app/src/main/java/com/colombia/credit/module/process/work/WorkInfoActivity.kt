package com.colombia.credit.module.process.work

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.DictionaryInfo
import com.colombia.credit.bean.resp.IBaseInfo
import com.colombia.credit.bean.resp.WorkInfo
import com.colombia.credit.databinding.ActivityWorkInfoBinding
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.IBaseProcessViewModel
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.viewbinding.binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkInfoActivity : BaseProcessActivity(), View.OnClickListener {

    private val mJobType by lazy {
        DictionaryUtil.getJobTypeData()
    }
    private val mJobIncomeSource by lazy {
        DictionaryUtil.getIncomeSourceData()
    }
    private val mJobYear by lazy {
        DictionaryUtil.getEntryTimeData()
    }

    private val mPayCycle by lazy {
        DictionaryUtil.getPayCycle()
    }

    private val mViewModel by lazyViewModel<WorkInfoViewModel>()

    private val mBinding by binding<ActivityWorkInfoBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setToolbarListener(mBinding.processToolbar)
        mBinding.bivType.setBlockingOnClickListener(this)
        mBinding.bivIncome.setBlockingOnClickListener(this)
        mBinding.bivPayday.setBlockingOnClickListener(this)
        mBinding.bivJobYear.setBlockingOnClickListener(this)
        mBinding.tvCommit.setBlockingOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v ?: return
        when (v.id) {
            R.id.biv_job_year -> {
                showSelectorDialog(
                    getString(R.string.work_life),
                    mJobYear,
                    mBinding.bivJobYear.tag?.toString(),
                    mBinding.bivJobYear
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
            R.id.biv_payday -> {
                showSelectorDialog(
                    getString(R.string.work_salary),
                    mPayCycle,
                    mBinding.bivPayday.tag?.toString(),
                    mBinding.bivPayday
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

    fun showSelectorDialog(
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
        }
    }

    override fun checkCommitInfo(): Boolean {
        return checkAndSetErrorHint(mBinding.bivType)
            .and(checkAndSetErrorHint(mBinding.bivPayday))
            .and(checkAndSetErrorHint(mBinding.bivIncome))
            .and(checkAndSetErrorHint(mBinding.bivJobYear))
    }

    override fun getCommitInfo(): IBaseInfo {
        val jobType = mBinding.bivType.tag
        val payday = mBinding.bivPayday.tag
        val incomeSource = mBinding.bivIncome.tag
        val jobYear = mBinding.bivJobYear.tag
        return WorkInfo()
    }

    override fun getViewModel(): IBaseProcessViewModel = mViewModel
}