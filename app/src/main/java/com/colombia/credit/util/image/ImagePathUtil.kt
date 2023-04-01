package com.colombia.credit.util.image

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.colombia.credit.permission.PermissionHelper
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImagePathUtil {

    fun createSDCardTempFile(context: Context, fileName: String = ""): File {
        return createTempFile(context.externalCacheDir!!, fileName)
    }

     fun createInternalTempFile(context: Context, fileName: String = ""): File {
        return createTempFile(context.cacheDir!!, fileName)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createTempFile(storageDir: File, fileName: String =""): File {
        if (fileName.isBlank()) {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(Date())
            return File.createTempFile("Temp_${timeStamp}_", ".jpg", storageDir)
        }
        return File(storageDir, fileName)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createTempDir(parentDir: File, dir: String): File {
        val storageDir = File(parentDir.path, dir)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return storageDir
    }

    fun createTempFile(context: Context): File {
        return if (PermissionHelper.checkPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            createSDCardTempFile(context)
        } else {
            createInternalTempFile(context)
        }
    }
}