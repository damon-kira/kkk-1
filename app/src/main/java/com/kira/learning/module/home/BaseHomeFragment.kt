package com.kira.learning.module.home

import androidx.core.content.ContextCompat
import com.kira.learning.dialog.CustomDialog
import com.kira.learning.expand.showCustomDialog
import com.kira.learning.view.ToolbarLayout
import com.common.lib.base.BaseFragment
import com.kira.learning.R
import com.util.lib.StatusBarUtil.setStatusBarColor

abstract class BaseHomeFragment : BaseFragment() {

    fun parentRefresh() {
        (parentFragment as? HomeFragment)?.onRefresh()
    }

    private var mCustomDialog: CustomDialog? = null

    fun setCustomListener(toolbarLayout: ToolbarLayout) {
        if(this.isAdded) {
            toolbarLayout.setCustomClickListener {
                if (mCustomDialog?.isShowing == true) return@setCustomClickListener
                mCustomDialog = getBaseActivity()?.showCustomDialog()
            }
        }
    }

    override fun onFragmentVisibilityChanged(visible: Boolean) {
        super.onFragmentVisibilityChanged(visible)
        if (visible) {
            getBaseActivity()?.setStatusBarColor(
                ContextCompat.getColor(
                    getSupportContext(),
                    R.color.colorPrimary
                ), false
            )
        }
    }

    override fun onDestroy() {
        mCustomDialog = null
        super.onDestroy()
    }
}