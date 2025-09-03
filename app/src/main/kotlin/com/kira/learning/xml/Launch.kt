package com.kira.learning.xml

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.kira.learning.LoanApplication.Companion.getAppContext
import com.kira.learning.xml.modules.codeeditor.CodePlaygroundActivity
import com.kira.learning.xml.modules.player.PlayerManageActivity
import com.kira.learning.xml.modules.python.CodingActivity
import com.kira.learning.xml.modules.stepbar.StepBarViewActivity
import com.kira.learning.xml.modules.supereditor.SuperEditorActivity
import com.util.lib.expand.isNotEmpty
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import kira.learning.chat.AiChatActivity

object Launch {

    private const val TAG = "debug_Launch"

    fun skipCodingActivity(context: Context) {
        launch(context, CodingActivity::class.java)
    }

    fun skipPlayerManageActivity(context: Context) {
        launch(context, PlayerManageActivity::class.java)
    }

    fun skipStepBarViewActivity(context: Context) {
        launch(context, StepBarViewActivity::class.java)
    }

    fun skipCodePlaygroundActivity(context: Context) {
        launch(context, CodePlaygroundActivity::class.java)
    }

    fun skipSuperEditorActivity(context: Context) {
        launch(context, SuperEditorActivity::class.java)
    }

    fun skipAiChatActivity(context: Context) {
        launch(context, AiChatActivity::class.java)
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