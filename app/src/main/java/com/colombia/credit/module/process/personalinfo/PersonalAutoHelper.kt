package com.colombia.credit.module.process.personalinfo

import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.colombia.credit.databinding.ActivityPersonalInfoBinding
import com.colombia.credit.module.process.AbsAutoCheckHelper
import com.util.lib.expand.hideSoftInput
import com.util.lib.expand.showSoftInput

// 基本信息
abstract class PersonalAutoHelper(vb: ActivityPersonalInfoBinding, isAuto: Boolean) :
    AbsAutoCheckHelper<ActivityPersonalInfoBinding>(vb, isAuto) {

    companion object {
        const val ITEM_EMAIL = 0
        const val ITEM_EDUCATION = 1
        const val ITEM_ADDR = 2
        const val ITEM_ADDR_DETAIL = 3
        const val ITEM_MARRIAGE = 4
    }

    override fun checkByValue(index: Int) {
        clearFocus()
        when (index) {
            ITEM_EMAIL -> checkItem1()
            ITEM_EDUCATION -> checkItem2()
            ITEM_ADDR -> checkItem3()
            ITEM_ADDR_DETAIL -> checkItem4()
            ITEM_MARRIAGE -> checkItem5()
        }
    }

    override fun checkIndexResult(index: Int): Boolean = index > ITEM_MARRIAGE

    init {
        initView()
    }

    private fun initView() {
        val editorActionListener = TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE) {
                clearFocus()
                hideSoftInput(v)
                startCheckNext()
            }
            false
        }
        vb.bivEmail.getEditView().setOnEditorActionListener(editorActionListener)
        vb.bivAddrDetail.getEditView().setOnEditorActionListener(editorActionListener)
    }

    fun clearFocus() {
        vb.nslContent.requestFocus()
        vb.bivEmail.clearFocus()
        vb.bivAddrDetail.clearFocus()
    }

    private fun checkItem1() {
        if (!checkInfoEmpty(vb.bivEmail)) {
            checkNextView()
            return
        }
        vb.bivEmail.requestFocus()
        vb.bivEmail.getEditView().requestFocus()
        showSoftInput(vb.bivEmail)
    }

    private fun checkItem2() {
        if (!checkInfoEmpty(vb.bivEducation)) {
            checkNextView()
            return
        }
        showItemDialog(ITEM_EDUCATION)
    }

    private fun checkItem3() {
        if (!checkInfoEmpty(vb.bivAddress)) {
            checkNextView()
            return
        }
        showItemDialog(ITEM_ADDR)
    }

    private fun checkItem4() {
        if (!checkInfoEmpty(vb.bivAddrDetail)) {
            vb.bivAddrDetail.clearFocus()
            checkNextView()
            return
        }
        vb.bivAddrDetail.requestFocus()
        showSoftInput(vb.bivAddrDetail)
    }


    private fun checkItem5() {
        if (!checkInfoEmpty(vb.bivMarriage)) {
            checkNextView()
            return
        }
        showItemDialog(ITEM_MARRIAGE)
    }

}