package com.colombia.credit.module.process.work

import android.os.Bundle
import android.view.View
import com.colombia.credit.R
import com.colombia.credit.bean.req.IReqBaseInfo
import com.colombia.credit.bean.req.ReqWorkInfo
import com.colombia.credit.bean.resp.RspWorkInfo
import com.colombia.credit.databinding.ActivityWorkInfoBinding
import com.colombia.credit.expand.STEP3
import com.colombia.credit.module.process.BaseProcessActivity
import com.colombia.credit.module.process.BaseProcessViewModel
import com.colombia.credit.util.DictionaryUtil
import com.colombia.credit.view.baseinfo.BaseInfoView
import com.common.lib.expand.setBlockingOnClickListener
import com.common.lib.livedata.observerNonSticky
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
        setToolbarListener(mBinding.processToolbar)
        mBinding.bivType.setBlockingOnClickListener(this)
        mBinding.bivIncome.setBlockingOnClickListener(this)
        mBinding.bivPayday.setBlockingOnClickListener(this)
        mBinding.bivJobYear.setBlockingOnClickListener(this)
        mBinding.tvCommit.setBlockingOnClickListener(this)

        mViewModel.getCacheInfo()?.also { info ->
            info as ReqWorkInfo
            val jobTime = info.x6yR
            if (mJobYear.containsKey(jobTime)) {
                setBaseInfo(mBinding.bivJobYear, mJobYear[jobTime], jobTime)
            }
            val income = info.xgJ5
            if (mJobIncomeSource.containsKey(income)) {
                setBaseInfo(mBinding.bivIncome, mJobIncomeSource[income], income)
            }
            val payday = info.u0pn
            if (mPayCycle.containsKey(payday)) {
                setBaseInfo(mBinding.bivPayday, mPayCycle[payday], payday)
            }
            val jobType = info.AD8Jznx
            if (mJobType.containsKey(jobType)) {
                setBaseInfo(mBinding.bivType, mJobType[jobType], jobType)
            }
        }
        mViewModel.getInfo()
    }

    override fun initObserver() {
        mViewModel.mInfoLiveData.observerNonSticky(this) { rspInfo ->
            if (rspInfo !is RspWorkInfo) return@observerNonSticky
            rspInfo.dbxhWe4XWA?.let { info ->
                val jobTime = info.iBwnjiNbTX
                if (mJobYear.containsKey(jobTime)) {
                    setBaseInfo(mBinding.bivJobYear, mJobYear[jobTime], jobTime)
                }
                val income = info.P2i72V
                if (mJobIncomeSource.containsKey(income)) {
                    setBaseInfo(mBinding.bivIncome, mJobIncomeSource[income], income)
                }
                val payday = info.RbNJgGj
                if (mPayCycle.containsKey(payday)) {
                    setBaseInfo(mBinding.bivPayday, mPayCycle[payday], payday)
                }
                val jobType = info.V33vxNjkQf
                if (mJobType.containsKey(jobType)) {
                    setBaseInfo(mBinding.bivType, mJobType[jobType], jobType)
                }
            }
        }
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

    override fun getCommitInfo(): IReqBaseInfo {
        return ReqWorkInfo().also {
            it.AD8Jznx = mBinding.bivType.tag?.toString()
            it.u0pn = mBinding.bivPayday.tag?.toString()
            it.xgJ5 = mBinding.bivIncome.tag?.toString()
            it.x6yR = mBinding.bivJobYear.tag?.toString()
        }
    }

    override fun getViewModel(): BaseProcessViewModel = mViewModel
    override fun getNextType(): Int = STEP3
}