package com.common.lib.dialog

import android.app.Activity
import android.app.Dialog
import android.view.View
import com.common.lib.BuildConfig
import com.common.lib.R

/**
 * Create by weishl
 * 2022/8/31
 */

/** 显示loading框*/
internal fun Activity.showLoadingDialog(cancelable: Boolean = true): DefaultDialog? {
    val dialog = DefaultDialog(this)
    try {
        dialog.setContentView(R.layout.view_loading)
    } catch (e: Exception) {
        return null
    }
    val loading: View = dialog.findViewById(R.id.loading_root)
    loading.setOnClickListener {
        dialog.dismiss()
    }
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(cancelable)
    showDialoga(dialog)
    return dialog
}


internal fun Activity.showDialoga(dialog: Dialog?): Boolean {
    try {
        if (dialog == null || this.isFinishing || dialog.isShowing) {
            return false
        }
        dialog.show()
        return true
    } catch (t: Throwable) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace()
        }
    }
    return false
}


/**
 * 隐藏loading框
 */
internal fun Activity.hideLoadingDialog(dialog: Dialog?) {
    this.dismissDialoga(dialog)
}


/**
 * 安全地关闭dialog
 *
 * @param dialog
 */
internal fun Activity.dismissDialoga(dialog: Dialog?) {
    try {
        if (dialog == null || this.isFinishing || this.isDestroyed) {
            return
        }

        if (dialog.isShowing) {
            dialog.dismiss()
        }
    } catch (t: Throwable) {
        if (BuildConfig.DEBUG) {
            t.printStackTrace()
        }
    }
}