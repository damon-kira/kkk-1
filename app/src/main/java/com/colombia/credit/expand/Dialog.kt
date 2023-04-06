package com.colombia.credit.expand

import android.app.Activity
import com.colombia.credit.dialog.NetErrorDialog
import com.colombia.credit.manager.Launch


fun Activity.showNetErrorDialog(refresh: () -> Unit) {
    val dialog = NetErrorDialog(this)
    dialog.setonClickListener(refresh, mobileNet = {
        Launch.skipMobileNetPage(this)
    }, wifi = {
        Launch.skipWifiPage(this)
    })
}