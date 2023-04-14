package com.bigdata.lib

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log

object StorageHelper {

    private const val TAG = "StorageHelper"

    fun getImageNum(context: Context) {
        val selection = MediaStore.Images.Media.MIME_TYPE + "= ?"
        val selectionArgs = arrayOf("image/jpeg", "image/png")
        val results = arrayListOf<String>()

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            selectionArgs,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)

                Log.d(TAG, "getDownFileNum: index=$index")

                if (index == -1) continue

                val path = cursor.getString(index)
                Log.d(TAG, "getDownFileNum: path=$path")
            }
        }
    }

    fun getDownFileNum(): Int {
        return Environment.getDownloadCacheDirectory().absoluteFile.listFiles()?.size ?: 0
    }
}