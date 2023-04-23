package com.colombia.credit.module.home

import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.expand.showCustomDialog
import com.colombia.credit.view.ToolbarLayout
import com.common.lib.base.BaseFragment

abstract class BaseHomeFragment : BaseFragment() {

    fun parentRefresh() {
        (parentFragment as? HomeFragment)?.onRefresh()
    }

    private var mCustomDialog: CustomDialog? = null

    fun setCustomListener(toolbarLayout: ToolbarLayout) {
        toolbarLayout.setCustomClickListener {
            if (mCustomDialog?.isShowing == true) return@setCustomClickListener
            mCustomDialog = getBaseActivity()?.showCustomDialog()
        }
    }
}