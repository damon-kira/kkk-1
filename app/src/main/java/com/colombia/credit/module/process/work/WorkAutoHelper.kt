package com.colombia.credit.module.process.work

import com.colombia.credit.databinding.ActivityWorkInfoBinding
import com.colombia.credit.module.process.AbsAutoCheckHelper
import com.colombia.credit.view.baseinfo.AbsBaseInfoView

abstract class WorkAutoHelper(vb: ActivityWorkInfoBinding) :
    AbsAutoCheckHelper<ActivityWorkInfoBinding>(vb) {

    companion object {
        const val ITEM_TYPE = 0
        const val ITEM_PAYDAY = 1
        const val ITEM_INCOME = 2
        const val ITEM_JOB_YEAR = 3
    }

    override fun checkIndexResult(index: Int): Boolean = index > ITEM_JOB_YEAR

    override fun checkByValue(index: Int) {
        when (index) {
            ITEM_TYPE -> checkItem(index, vb.bivType)
            ITEM_PAYDAY -> checkItem(index, vb.bivPayday)
            ITEM_INCOME -> checkItem(index, vb.bivIncome)
            ITEM_JOB_YEAR -> checkItem(index, vb.bivJobYear)
        }
    }

    private fun checkItem(index: Int, infoView: AbsBaseInfoView) {
        if (!checkInfoEmpty(infoView)) {
            checkNextView()
            return
        }
        showItemDialog(index)
    }
}