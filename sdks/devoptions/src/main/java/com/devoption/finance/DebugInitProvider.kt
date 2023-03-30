package com.devoption.finance

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.StrictMode
import android.util.Log
import java.util.concurrent.Executors

/**
 * Created by weishl on 2021/7/13
 *
 */
class DebugInitProvider : ContentProvider() {
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun onCreate(): Boolean {
        DebugModeHolder.init(context!!.applicationContext)
        openDebug()
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null

    private fun openDebug() {
        val policy = StrictMode.VmPolicy.Builder()
        policy.detectLeakedClosableObjects()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            policy.detectNonSdkApiUsage()
            policy.penaltyListener(
                Executors.newSingleThreadExecutor(),
                StrictMode.OnVmViolationListener {
                    Log.e("DebugInitProvider", "penaltyListener ：$it.message")
                })
        }
        StrictMode.setVmPolicy(policy.build())
    }

}