package com.finance.analysis

import android.content.Context
import android.text.TextUtils.isEmpty
import com.cache.lib.SharedPrefGlobal
import com.finance.analysis.push.PushManagerFactory

/**
 * Created by weishl on 2022/11/24
 *
 */
object FirebaseInfo {

    fun getFcmToken(context: Context): String {
        val fcmToken = SharedPrefGlobal.getString(AnalysisSpkey.KEY_FCM_TOKEN, "")
        if (isEmpty(fcmToken)) {
            PushManagerFactory.init(context)
        }
        return fcmToken
    }

    fun saveFcmToken(token: String) {
        SharedPrefGlobal.setString(AnalysisSpkey.KEY_FCM_TOKEN, token)
    }

    fun saveInstanceId(instanceId: String) {
        SharedPrefGlobal.setString(AnalysisSpkey.KEY_FIREBASE_INSTANCEID, instanceId)
    }

    fun getInstanceId(): String {
        return SharedPrefGlobal.getString(AnalysisSpkey.KEY_FIREBASE_INSTANCEID, "")
    }
}