package com.colombia.credit.expand

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.colombia.credit.dialog.AppUpgradeDialog
import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.dialog.NetErrorDialog
import com.colombia.credit.manager.Launch
import com.common.lib.BuildConfig
import com.common.lib.base.BaseActivity
import com.common.lib.dialog.DefaultDialog
import com.common.lib.dialog.DialogHandleMode


private var mNetErrorDialog: NetErrorDialog? = null
fun BaseActivity.showNetErrorDialog(refresh: () -> Unit): DefaultDialog {
    var dialog = mNetErrorDialog
    if (dialog == null) {
        dialog = NetErrorDialog(this)
    }
    if (dialog.isShowing) {
        dialog.dismiss()
    }
    dialog.setonClickListener(refresh, mobileNet = {
        Launch.skipDataPage(this)
    }, wifi = {
        Launch.skipWifiPage(this)
    })
    addDialog(dialog)
    return dialog
}

@SuppressLint("StaticFieldLeak")
private var mCustomDialog: CustomDialog? = null
fun BaseActivity.showCustomDialog(): CustomDialog {
    var dialog = mCustomDialog
    if (dialog == null) {
        dialog = CustomDialog(this)
        mCustomDialog = dialog
    } else if (dialog.isShowing) {
        return dialog
    }
    this.lifecycle.addObserver(object: LifecycleEventObserver{
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                mCustomDialog?.dismiss()
                mCustomDialog = null
            }
        }
    })
    mCustomDialog?.let {
        addDialog(it)
    }
    return dialog
}

private var appUpgradeDialog: AppUpgradeDialog? = null
fun BaseActivity.showAppUpgradeDialog(info: AppUpgradeInfo, clickListener: (type: Int) -> Unit) {
    var updateDialog = appUpgradeDialog
    if (updateDialog?.isShowing == true) {
        return
    }
    appUpgradeDialog = AppUpgradeDialog(this).setAppUpdateInfo(info)
    updateDialog = appUpgradeDialog
    updateDialog?.mListener = clickListener
    val mode =
        if (info.CHDnt3v == 2) DialogHandleMode.REMOVE_OTHERS else DialogHandleMode.ALL_PRIORITY_FIRST
    addDialog(updateDialog!!, mode = mode)
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
