package com.devoption.module

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Process

internal object DebugPackagerUtils {
    fun restartApp() {
        val ctx = DebugModeHolder.getContext() ?: return
        val intent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        val restartIntent: PendingIntent =
            PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager: AlarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + 1000,
            restartIntent
        ) // 1秒钟后重启应用
        Process.killProcess(Process.myPid())
    }

    @SuppressLint("CommitPrefEdits")
    fun clearCache() {
        val ctx = DebugModeHolder.getContext() ?: return
        ctx.getSharedPreferences("user.cache", Context.MODE_PRIVATE).edit().clear()
        ctx.getSharedPreferences("global.cache", Context.MODE_PRIVATE).edit().clear()
        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        am.clearApplicationUserData()
    }
}