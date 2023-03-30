package com.devoption.finance

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Process


/**
 * Created by weishl on 2021/7/13
 *
 */
internal object DebugPackagerUtils {
    fun restartApp() {
        val ctx = DebugModeHolder.getContext() ?: return
        val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        val restartIntent: PendingIntent =
            PendingIntent.getActivity(ctx, 0, intent, 0)
        val alarmManager: AlarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent) // 1秒钟后重启应用
        Process.killProcess(Process.myPid())
    }

    fun clearCache() {
        val ctx = DebugModeHolder.getContext() ?: return
        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE)as ActivityManager
        am.clearApplicationUserData()
    }
}