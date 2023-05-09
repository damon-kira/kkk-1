package com.finance.analysis.push

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.finance.analysis.FirebaseInfo.getInstanceId
import com.finance.analysis.FirebaseInfo.saveFcmToken
import com.finance.analysis.FirebaseInfo.saveInstanceId
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.util.lib.ThreadPoolUtil
import com.util.lib.expand.isNotEmpty
import com.util.lib.log.isDebug
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e


class FirebaseManager : IPushManager {

    private val TAG = "debug_FirebaseManager"

    private var mGaid: String? = null
    override fun init(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                if (isDebug()) {
                    logger_e(TAG, "fcm token = $token")
                }
                token?.let { value ->
                    saveFcmToken(value)
                }
            }
        }

        getFirebaseInstanceId(context)
    }


    override fun getGaid(context: Context): String {
        ThreadPoolUtil.executor("gaid"){
            val gaid = try {
                AdvertisingIdClient.getAdvertisingIdInfo(context).id.orEmpty()
            }catch (e: Exception) {
                ""
            }
            mGaid = gaid
            if (isDebug()) {
                logger_e(TAG, "getGaid()  = $gaid")
            }
        }
        return mGaid.orEmpty()
    }

    override fun reportException(t: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(t)
    }

    override fun getChannel(): Int = 0

    override fun skipAppStore(context: Context, jumpAddress: String?) {
        val packageName = context.packageName
        val appAddress = "https://play.google.com/store/apps/details?id=$packageName"
        try {
            var intent: Intent
            if (isNotEmpty(jumpAddress)) {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(jumpAddress))
            } else {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
                intent.setPackage("com.android.vending")
                if (intent.resolveActivity(context.packageManager) == null) {
                    intent = Intent(Intent.ACTION_VIEW, Uri.parse(appAddress))
                }
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
//            launch(context, MainActivity::class.java, intent)
        } catch (e: Exception) {
            if (isDebug()) {
                logger_e("debug_Launch", "skipAppStore Launch 290 error = $e")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getFirebaseInstanceId(ctx: Context) {
        FirebaseAnalytics.getInstance(ctx).appInstanceId.addOnCompleteListener {
            logger_d(TAG, "onClick: isSuccessful = ${it.isSuccessful}")
            if (it.isSuccessful) {
                val result = it.result.orEmpty()
                logger_d(TAG, "onClick: instanceId = $result")
                if (result.isNotEmpty()) {
                    saveInstanceId(result)
                }
            }
        }
    }

    override fun getAppInstanceId(context: Context): String {
        val instanceId = getInstanceId()
        if (instanceId.isEmpty()) {
            getFirebaseInstanceId(context)
        }
        return instanceId
    }
}