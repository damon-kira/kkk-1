package com.colombia.credit.module.home

import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.view.ToolbarLayout
import com.common.lib.base.BaseFragment

abstract class BaseHomeFragment : BaseFragment() {

    fun parentRefresh() {
        (parentFragment as? HomeFragment)?.onRefresh()
    }

    private val mCustomDialog by lazy {
        CustomDialog(getSupportContext())
    }

    fun setCustomListener(toolbarLayout: ToolbarLayout) {
        toolbarLayout.setCustomClickListener {
            if (mCustomDialog.isShowing) return@setCustomClickListener
            mCustomDialog.show()
        }
    }
}