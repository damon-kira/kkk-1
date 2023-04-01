package com.colombia.credit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.webkit.WebView
import com.colombia.credit.camera.CaptureActivity
import com.colombia.credit.expand.isXiaomi
import com.colombia.credit.module.banklist.BankCardListActivity
import com.colombia.credit.module.process.contact.ContactInfoActivity
import com.colombia.credit.module.process.face.FaceActivity
import com.colombia.credit.module.process.face.FaceFailedActivity
import com.colombia.credit.module.process.kyc.KycInfoActivity
import com.colombia.credit.module.process.personalinfo.PersonalInfoActivity
import com.colombia.credit.module.process.work.WorkInfoActivity
import com.colombia.credit.module.upload.UploadActivity
import com.colombia.credit.module.webview.WebViewActivity
import com.common.lib.base.BaseActivity
import com.util.lib.log.logger_e

object Launch {

    private const val TAG = "debug_Launch"

    fun skipMainActivity(context: Context) {
        launch(context, MainActivity::class.java)
    }

    fun skipPersonalInfoActivity(context: Context) {
        launch(context, PersonalInfoActivity::class.java)
    }

    fun skipWorkInfoActivity(context: Context) {
        launch(context, WorkInfoActivity::class.java)
    }

    fun skipContactInfoActivity(context: Context) {
        launch(context, ContactInfoActivity::class.java)
    }

    fun skipKycInfoActivity(context: Context) {
        launch(context, KycInfoActivity::class.java)
    }

    fun skipBankCardListActivity(context: Context) {
        launch(context, BankCardListActivity::class.java)
    }

    fun skipFaceActivity(context: Context) {
        launch(context, FaceActivity::class.java)
    }

    fun skipFaceFailedActivity(context: Context) {
        launch(context, FaceFailedActivity::class.java)
    }

    fun skipUploadActivity(context: Context) {
        launch(context, UploadActivity::class.java)
    }

    fun skipWifiPage(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    }

    fun skipWebViewActivity(context: Context, url: String){
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_URL, url)
        launch(context, WebViewActivity::class.java, intent)
    }

    fun skipMobileNetPage(context: Context) {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            val managerClass = Class.forName(manager.javaClass.name)
            val field = managerClass.getDeclaredField("mService")
            field.isAccessible = true
            val managerObj = field.get(manager)
            val managerObjClass = Class.forName(managerObj.javaClass.name)
            val method =
                managerObjClass.getDeclaredMethod("setMobileDataEnabled", Boolean::class.java)
            method.isAccessible = true
            method.invoke(managerObj, true)
        } catch (e: Exception) {
            logger_e(TAG, "skipMobileNetPage: error = $e")
        }
    }

    /**
     * 跳转到本应用设置页面
     */
    fun Context.jumpToAppSettingPage() {
        if (isXiaomi()) {
            val miuiIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
            miuiIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            miuiIntent.putExtra("extra_pkgname", packageName)
            //检测是否有能接受该Intent的Activity存在
            val resolveInfos =
                packageManager.queryIntentActivities(miuiIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolveInfos.size > 0) {
                startActivity(miuiIntent)
                return
            }
        }
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts(
                "package",
                packageName, null
            )
            startActivity(intent)
        } catch (e: Exception) {
            try {
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            } catch (e: Exception) {

            }
        }
    }


}

private fun <T> launch(context: Context, clazz: Class<T>, intent: Intent? = null) {
    val intent0 = when (intent) {
        null -> {
            Intent().also {
                it.setClass(context, clazz)
            }
        }
        else -> intent
    }
    if (context !is Activity) {
        intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent0)
}

fun Activity.launchForResult(intent: Intent, requestCode: Int) {
    this.startActivityForResult(intent, requestCode)
}
