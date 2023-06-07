package com.bigdata.lib

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import com.util.lib.log.logger_d

object StorageHelper {

    private const val TAG = "StorageHelper"

    fun getImageNum(context: Context):ArrayList<String> {
        val result = arrayListOf<String>()
        try {
            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.DATA),
                null,
                null,
                null
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    logger_d(TAG, "getDownFileNum: data index=$index")
                    if (index == -1) continue
                    val path = cursor.getString(index)
                    logger_d(TAG, "getDownFileNum: data path=$path")
                    result.add(path)
                }
            }
        } catch (e: Exception) {
        }
        return result
    }

    fun getDownFileNum(): Int {
        return Environment.getDownloadCacheDirectory().absoluteFile.listFiles()?.size ?: 0
    }
}