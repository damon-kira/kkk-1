package com.finance.analysis.push

import android.content.Context
import androidx.annotation.WorkerThread

/**
 * author: weishl
 * data: 2021/3/4
 **/
interface IPushManager {

    fun init(context: Context)

    @WorkerThread
    fun getGaid(context: Context): String?

    fun reportException(t: Throwable)

    fun getChannel(): Int

    fun skipAppStore(context: Context, jumpAddress: String?)

    fun getAppInstanceId(context: Context): String
}