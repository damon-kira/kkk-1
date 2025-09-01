package com.kira.learning.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.kira.learning.LoanApplication.Companion.getAppContext
import com.kira.learning.R
import com.kira.learning.xml.expand.getEmail
import com.kira.learning.xml.expand.getServiceTel
import com.kira.learning.xml.expand.getWhatsAppTel
import com.kira.learning.xml.expand.isXiaomi
import com.kira.learning.xml.expand.toast
import com.kira.learning.xml.modules.ai.AIChatActivity
import com.kira.learning.xml.modules.answer.AnswerActivity
import com.kira.learning.xml.modules.answer.CourseActivity
import com.kira.learning.xml.modules.applysuccess.ApplySuccessActivity
import com.kira.learning.xml.modules.chat.ChatActivity
import com.kira.learning.xml.modules.codeeditor.CodePlaygroundActivity
import com.kira.learning.xml.modules.history.HistoryActivity
import com.kira.learning.xml.modules.home.XMLMainActivity
import com.kira.learning.xml.modules.ocr.PhotographActivity
import com.kira.learning.xml.modules.player.PlayerManageActivity
import com.kira.learning.xml.modules.python.CodingActivity
import com.kira.learning.xml.modules.quiz.QuizActivity
import com.kira.learning.xml.modules.review.RepeatReviewActivity
import com.kira.learning.xml.modules.richview.ZoomImageActivity
import com.kira.learning.xml.modules.setting.SettingActivity
import com.kira.learning.xml.modules.stepbar.StepBarViewActivity
import com.kira.learning.xml.modules.supereditor.SuperEditorActivity
import com.kira.learning.xml.modules.webview.WebViewActivity
import com.kira.learning.utils.camera.CaptureActivity
import com.util.lib.expand.isNotEmpty
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import kira.learning.chat.AiChatActivity
import com.kira.learning.module.main.MainActivity as ComposeMainActivity

object Launch {

    private const val TAG = "debug_Launch"

    fun skipXMLMainActivity(context: Context) {
        launch(context, XMLMainActivity::class.java)
    }

    fun skipHistoryActivity(context: Context) {
        launch(context, HistoryActivity::class.java)
    }

    fun skipApplySuccessActivity(context: Context) {
        launch(context, ApplySuccessActivity::class.java)
    }

    fun skipWifiPage(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    fun skipDataPage(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
            context.startActivity(intent)
        } catch (e: Exception) {
        }
    }

    fun skipWebViewActivity(context: Context, url: String) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_URL, url)
        launch(context, WebViewActivity::class.java, intent)
    }

    fun skipSettingActivity(context: Context) {
        launch(context, SettingActivity::class.java)
    }

    // 复盘审核中页面
    fun skipRepeatReviewActivity(context: Context) {
        launch(context, RepeatReviewActivity::class.java)
    }

    fun skipPhotographActivity(context: Context) {
        launch(context, PhotographActivity::class.java)
    }

    fun skipAIChatActivity(context: Context) {
//        launch(context, CourseActivity::class.kotlin)
        launch(context, AIChatActivity::class.java)
    }

    fun skipCodingActivity(context: Context) {
        launch(context, CodingActivity::class.java)
    }

    fun skipCourseActivity(context: Context) {
        launch(context, CourseActivity::class.java)
    }

    fun skipAIIMActivity(context: Context) {
        launch(context, AIChatActivity::class.java)
    }

    fun skipChatActivity(context: Context) {
        launch(context, ChatActivity::class.java)
    }

    fun skipAnswerActivity(context: Context) {
        launch(context, AnswerActivity::class.java)
    }

    fun skipQuizActivity(context: Context) {
        launch(context, QuizActivity::class.java)
    }

    fun skipPlayerManageActivity(context: Context) {
        launch(context, PlayerManageActivity::class.java)
    }

    fun skipZoomImageActivity(context: Context) {
        launch(context, ZoomImageActivity::class.java)
    }

    fun skipCaptureActivity(context: Context) {
        launch(context, CaptureActivity::class.java)
    }

    fun skipStepBarViewActivity(context: Context) {
        launch(context, StepBarViewActivity::class.java)
    }

    fun skipCodePlaygroundActivity(context: Context) {
        launch(context, CodePlaygroundActivity::class.java)
    }

    //    fun skipKCodingEditorActivity(context: Context){
//        launch(context, KCodingEditorActivity::class.kotlin)
//    }
    fun skipSuperEditorActivity(context: Context) {
        launch(context, SuperEditorActivity::class.java)
    }

    fun skipAiChatActivity(context: Context) {
        launch(context, AiChatActivity::class.java)
    }

    fun skipComposeMainActivity(context: Context) {
        launch(context, ComposeMainActivity::class.java)
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
                intent = Intent(Intent.ACTION_VIEW, jumpAddress?.toUri())
            } else {
                intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
                intent.setPackage("com.android.vending")
                if (intent.resolveActivity(ctx.packageManager) == null) {
                    intent = Intent(Intent.ACTION_VIEW, appAddress.toUri())
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
        val intent = Intent(context, XMLMainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("exit", 0)
        launch(context, XMLMainActivity::class.java, intent)
    }

    @SuppressLint("IntentReset")
    fun skipToEmail(context: Context) {
        val uri = "mailto:${getEmail()}".toUri()
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
        if (whatsapp.isEmpty()) {
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
            intent.data = "tel:$mobile".toUri()
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
            if (resolveInfos.isNotEmpty()) {
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
