package com.colombia.credit.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.colombia.credit.LoanApplication.Companion.getAppContext
import com.colombia.credit.R
import com.colombia.credit.module.home.MainActivity
import com.colombia.credit.expand.*
import com.colombia.credit.module.applysuccess.ApplySuccessActivity
import com.colombia.credit.module.banklist.BankInfoAddActivity
import com.colombia.credit.module.banklist.ConfirmBankCardListActivity
import com.colombia.credit.module.banklist.MeBankAccountListActivity
import com.colombia.credit.module.defer.DeferActivity
import com.colombia.credit.module.defer.RepayTabDeferActivity
import com.colombia.credit.module.history.HistoryActivity
import com.colombia.credit.module.process.bank.BankInfoActivity
import com.colombia.credit.module.process.contact.ContactInfoActivity
import com.colombia.credit.module.process.face.FaceActivity
import com.colombia.credit.module.process.face.FaceFailedActivity
import com.colombia.credit.module.process.kyc.KycInfoActivity
import com.colombia.credit.module.process.personalinfo.PersonalInfoActivity
import com.colombia.credit.module.process.work.WorkInfoActivity
import com.colombia.credit.module.repaydetail.RepayDetailActivity
import com.colombia.credit.module.repeat.confirm.RepeatConfirmActivity
import com.colombia.credit.module.setting.SettingActivity
import com.colombia.credit.module.upload.UploadActivity
import com.colombia.credit.module.webview.WebViewActivity
import com.common.lib.base.BaseActivity
import com.common.lib.base.BaseFragment
import com.util.lib.expand.isNotEmpty
import com.util.lib.log.isDebug
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

    fun skipBankInfoActivity(context: Context) {
        launch(context, BankInfoActivity::class.java)
    }

    // 其它流程添加银行卡信息页面
    fun skipBankInfoAddActivity(context: Context) {
        launch(context, BankInfoAddActivity::class.java)
    }

    fun skipBankInfoAddActivityResult(activity: BaseActivity, requestCode: Int) {
        val intent = Intent(activity, BankInfoAddActivity::class.java)
        activity.launchForResult(intent, requestCode)
    }

    fun skipKycInfoActivity(context: Context) {
        launch(context, KycInfoActivity::class.java)
    }

    // 银行账户页面
    fun BaseActivity.skipBankCardListActivity(amount: String, productId: String) {
        val intent = Intent(this, ConfirmBankCardListActivity::class.java)
        intent.putExtra(ConfirmBankCardListActivity.EXTRA_LOAN_AMOUNT, amount)
        intent.putExtra(ConfirmBankCardListActivity.EXTRA_PRODUCT_ID, productId)
        launchForResult(intent, 10)
    }

    // 银行账户页面
    fun skipBankCardListActivity(
        context: Context,
        amount: String,
        productId: String,
        bankNo: String
    ) {
        val intent = Intent(context, ConfirmBankCardListActivity::class.java)
        intent.putExtra(ConfirmBankCardListActivity.EXTRA_LOAN_AMOUNT, amount)
        intent.putExtra(ConfirmBankCardListActivity.EXTRA_PRODUCT_ID, productId)
        intent.putExtra(ConfirmBankCardListActivity.EXTRA_BANK_NO, bankNo)
        launch(context, ConfirmBankCardListActivity::class.java, intent)
    }

    fun skipMeBankCardListActivity(context: Context) {
        launch(context, MeBankAccountListActivity::class.java)
    }

    fun skipHistoryActivity(context: Context) {
        launch(context, HistoryActivity::class.java)
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

    fun skipRepeatConfirmActivity(context: Context, ids: String) {
        val intent = Intent(context, RepeatConfirmActivity::class.java)
        intent.putExtra(RepeatConfirmActivity.EXTRA_IDS, ids)
        launch(context, RepeatConfirmActivity::class.java, intent)
    }

    fun skipDeferActivity(context: Context, infoJson: String) {
        val intent = Intent(context, DeferActivity::class.java)
        intent.putExtra(DeferActivity.EXTRA_INFO, infoJson)
        launch(context, DeferActivity::class.java, intent)
    }
    fun skipRepayDeferActivity(context: Context, infoJson: String) {
        val intent = Intent(context, RepayTabDeferActivity::class.java)
        intent.putExtra(DeferActivity.EXTRA_INFO, infoJson)
        launch(context, RepayTabDeferActivity::class.java, intent)
    }

    fun skipRepayDetailActivity(context: Context, productId: String) {
        val intent = Intent(context, RepayDetailActivity::class.java)
        intent.putExtra(RepayDetailActivity.EXTRA_ID, productId)
        launch(context, RepayDetailActivity::class.java, intent)
    }

    fun skipApplySuccessActivity(context: Context) {
        launch(context, ApplySuccessActivity::class.java)
    }

    fun skipWifiPage(context: Context) {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    }

    fun skipDataPage(context: Context) {
        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
        context.startActivity(intent)
    }

    fun skipWebViewActivity(context: Context, url: String) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_URL, url)
        launch(context, WebViewActivity::class.java, intent)
    }

    fun skipSettingActivity(context: Context) {
        launch(context, SettingActivity::class.java)
    }

    /**
     * 跳转到应用商店，并退出app
     * @param jumpAddress 跳转地址
     */
    fun skipAppStore(jumpAddress: String?, pkgName: String? = null) {
        val ctx = getAppContext()
        val packageName = if (pkgName.isNullOrEmpty()) ctx.packageName else pkgName
        val appAddress = "https://play.google.com/store/apps/details?id=$packageName"
        try {
            var intent: Intent
            if (isNotEmpty(jumpAddress)) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(jumpAddress))
            } else {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                intent.setPackage("com.android.vending")
                if (intent.resolveActivity(ctx.packageManager) == null) {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(appAddress))
                }
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ctx.startActivity(intent)
        } catch (e: Exception) {
            if (isDebug()) {
                logger_e("debug_Launch", "skipAppStore Launch 290 error = $e")
            }
        }
//        if (isUpdate) {
//            skipMainAndExitApp(getAppContext())
//        }
    }

    /**
     * 跳转首页然后退出app
     */
    fun skipMainAndExitApp(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("exit", 0)
        launch(context, MainActivity::class.java, intent)
    }

    @SuppressLint("IntentReset")
    fun skipToEmail(context: Context) {
        val uri = Uri.parse("mailto:${getEmail()}")
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getEmail()))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = uri
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            toast(R.string.email_error_hint)
        }
    }

    fun skipWhatsApp(context: Context) {
        val whatsapp = getWhatsAppTel()
        if (whatsapp.isNullOrEmpty()) {
            return
        }
        val url = "https://api.whatsapp.com/send?phone=$whatsapp"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        val pkgName = "com.whatsapp"
        intent.setPackage(pkgName)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            skipAppStore(null, pkgName)
        }
    }

    fun skipCallPage(context: Context) {
        val mobile = getServiceTel()
        try {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$mobile")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
        }
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

fun Fragment.launchForResult(intent: Intent, requestCode: Int) {
    this.startActivityForResult(intent, requestCode)
}
