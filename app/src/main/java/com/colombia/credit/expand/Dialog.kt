package com.colombia.credit.expand

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.colombia.credit.R
import com.colombia.credit.bean.resp.AppUpgradeInfo
import com.colombia.credit.dialog.AppUpgradeDialog
import com.colombia.credit.dialog.CustomDialog
import com.colombia.credit.dialog.NetErrorDialog
import com.colombia.credit.manager.Launch
import com.colombia.credit.module.home.HomeEvent
import com.colombia.credit.module.home.MainEvent
import com.colombia.credit.permission.HintDialog
import com.common.lib.BuildConfig
import com.common.lib.base.BaseActivity
import com.common.lib.dialog.DefaultDialog
import com.common.lib.dialog.DialogHandleMode
import com.common.lib.livedata.LiveDataBus
import java.lang.ref.WeakReference


private var mNetErrorDialog: WeakReference<NetErrorDialog>? = null
fun BaseActivity.showNetErrorDialog(refresh: () -> Unit): DefaultDialog {
    var dialog = mNetErrorDialog?.get()
    if (dialog == null) {
        dialog = NetErrorDialog(this)
        mNetErrorDialog = WeakReference(dialog)
    }
    lifecycle.addObserver(object: LifecycleEventObserver{
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                dialog?.dismiss()
                mNetErrorDialog = null
            } else if(event == Lifecycle.Event.ON_STOP){
                dialog?.dismiss()
            }
        }
    })
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

fun BaseActivity.showInvalidDialog(): DefaultDialog {
    val dialog = HintDialog(this)
        .setOnClickListener {
            LiveDataBus.post(HomeEvent(HomeEvent.EVENT_LOGOUT))
            LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
            Launch.skipMainActivity(this)
        }
        .showClose(false)
        .showTitle(HintDialog.type_GONE)
        .setMessage(getString(R.string.invalid_hint))
        .setBtnText(getString(R.string.confirm))
    addDialog(dialog)
    return dialog
}


fun BaseActivity.showCustomDialog(): CustomDialog {
    val dialog = CustomDialog(this)
    addDialog(dialog)
    return dialog
}

@SuppressLint("StaticFieldLeak")
private var appUpgradeDialog: WeakReference<AppUpgradeDialog>? = null
fun BaseActivity.showAppUpgradeDialog(info: AppUpgradeInfo) {
    if (appUpgradeDialog?.get()?.isShowing == true) {
        return
    }
    lifecycle.addObserver(object: LifecycleEventObserver{
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                source.lifecycle.removeObserver(this)
                appUpgradeDialog = null
            }
        }
    })
    val dialog = AppUpgradeDialog(this).setAppUpdateInfo(info)
    appUpgradeDialog = WeakReference(dialog)
    dialog.mListener = {
        if (it == AppUpgradeDialog.TYPE_APP_STORE) {
            Launch.skipAppStore(info.aTzLhoFtcl)
        } else {
            dialog.dismiss()
        }
    }
    val mode =
        if (info.CHDnt3v == 2) DialogHandleMode.REMOVE_OTHERS else DialogHandleMode.ALL_PRIORITY_FIRST

    addDialog(dialog, mode = mode)
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
