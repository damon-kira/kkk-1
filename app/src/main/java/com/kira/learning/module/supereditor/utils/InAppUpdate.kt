package com.kira.learning.module.supereditor.utils

import android.app.Activity

interface InAppUpdate {
    fun checkForUpdates(activity: Activity, onComplete: () -> Unit)
    fun completeUpdate()
}