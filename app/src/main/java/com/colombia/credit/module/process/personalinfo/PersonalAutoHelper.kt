package com.colombia.credit.module.process.personalinfo

import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.module.process.AbsAutoCheckHelper
import com.colombia.credit.view.baseinfo.AbsBaseInfoView

// 基本信息
abstract class PersonalAutoHelper(vb: ActivityPersonalInfoBinding, isAuto: Boolean) :
    AbsAutoCheckHelper<ActivityPersonalInfoBinding>(vb, isAuto) {

    companion object {
        const val ITEM_JOB_TYPE = 1
        const val ITEM_EDUCATION = 2
        const val ITEM_MARRIAGE = 3
        const val ITEM_INCOME = 4
        const val ITEM_ADDR = 5
    }

    override fun checkByValue(index: Int) {
        clearFocus()
        when (index) {
            ITEM_JOB_TYPE -> checkItem(index, vb.bivType)
            ITEM_EDUCATION -> checkItem(index, vb.bivEducation)
            ITEM_MARRIAGE -> checkItem(index, vb.bivMarriage)
            ITEM_INCOME -> checkItem(index, vb.bivIncome)
            ITEM_ADDR -> checkItem(index, vb.bivAddress)
            else -> {
                if (index < ITEM_JOB_TYPE) {
                    checkNextView()
                }
            }
        }
    }

    override fun checkIndexResult(index: Int): Boolean = index > ITEM_ADDR

    fun clearFocus() {
        vb.nslContent.requestFocus()
    }

    private fun checkItem(index: Int, infoView: AbsBaseInfoView) {
        if (!checkInfoEmpty(infoView)) {
            checkNextView()
            return
        }
        showItemDialog(index)
    }
}