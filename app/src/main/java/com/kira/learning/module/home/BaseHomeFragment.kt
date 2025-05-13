package com.kira.learning.module.home

import com.kira.learning.dialog.CustomDialog
import com.kira.learning.expand.showCustomDialog
import com.kira.learning.view.ToolbarLayout
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

    override fun onDestroy() {
        super.onDestroy()
        mCustomDialog = null
    }
}