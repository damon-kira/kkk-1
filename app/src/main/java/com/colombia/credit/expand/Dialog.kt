package com.colombia.credit.expand

import android.app.Activity
import android.app.Dialog
import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.colombia.credit.dialog.AppUpgradeDialog
import com.colombia.credit.dialog.NetErrorDialog
import com.colombia.credit.manager.Launch
import com.common.lib.BuildConfig
import com.common.lib.base.BaseActivity
import com.common.lib.dialog.DialogHandleMode


fun Activity.showNetErrorDialog(refresh: () -> Unit) {
    val dialog = NetErrorDialog(this)
    dialog.setonClickListener(refresh, mobileNet = {
        Launch.skipMobileNetPage(this)
    }, wifi = {
        Launch.skipWifiPage(this)
    })
}

private var appUpgradeDialog: AppUpgradeDialog? = null
fun BaseActivity.showAppUpgradeDialog(info: AppUpgradeInfo) {
    var updateDialog = appUpgradeDialog
    if (updateDialog?.isShowing == true) {
        return
    }
    appUpgradeDialog = AppUpgradeDialog(this).setAppUpdateInfo(info)
    updateDialog = appUpgradeDialog
    val mode = if (info.CHDnt3v == 2) DialogHandleMode.REMOVE_OTHERS else DialogHandleMode.ALL_PRIORITY_FIRST
    addDialog(updateDialog!!, mode = mode)
    showDialoga(appUpgradeDialog)
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
