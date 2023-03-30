package com.util.lib.uuid

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.core.content.PermissionChecker
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Created by weishl on 2022/1/17
 *
 */
class DevicesStorage {

    private val TAG = "debug_DevicesStorage"
    private var FILE_ANDROID: String? = null
    private var FILE_DCIM: String? = null
    private val DEFAULT_FILE_NAME = ".system_device_id1"

    private var mCtx: Context? = null


    fun init(context: Context) {
        mCtx = context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //Android Q+ 保存在私有目录
            FILE_ANDROID = context.getExternalFilesDir("Android")
                .toString() + File.separator + DEFAULT_FILE_NAME
            FILE_DCIM = context.getExternalFilesDir("DCIM")
                .toString() + File.separator + DEFAULT_FILE_NAME
        } else {
            FILE_ANDROID = Environment.getExternalStoragePublicDirectory("Android")
                .toString() + File.separator + DEFAULT_FILE_NAME
            FILE_DCIM = Environment.getExternalStoragePublicDirectory("DCIM")
                .toString() + File.separator + DEFAULT_FILE_NAME
        }
    }

    @WorkerThread
    @Synchronized
    fun getUUid(): String? {
        if (!checkPermission()) {
            return ""
        }
        if (FILE_ANDROID == null) {
            throw IllegalStateException("")
        }
        var uuid = getFromLocalFile(FILE_ANDROID)
        if (uuid.isNullOrEmpty()) {
            uuid = getFromLocalFile(FILE_DCIM)
        }
        return uuid
    }

    @WorkerThread
    @Synchronized
    fun saveUUid(uuid: String) {
        if (!checkPermission()) {
            return
        }
        val fileAndroid = FILE_ANDROID
        if (!fileAndroid.isNullOrEmpty()) {
            saveToLocalPath(fileAndroid, uuid)
        }

        val fileDCIM = FILE_DCIM
        if (!fileDCIM.isNullOrEmpty()) {
            saveToLocalPath(fileDCIM, uuid)
        }
    }

    private fun checkPermission():Boolean {
        val ctx = mCtx ?: return true
        if (PermissionChecker.checkSelfPermission(ctx, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED
            && PermissionChecker.checkSelfPermission(ctx, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED) {
               return true
        }
        return false
    }

    fun deleteUUid() {
        if (!checkPermission()) {
            return
        }
        val fileAndroid = FILE_ANDROID
        if (fileAndroid?.isNotEmpty() == true) {
            deleteLocalUUid(fileAndroid)
        }
        val fileDcim = FILE_DCIM.orEmpty()
        if (fileDcim.isNotEmpty()) {
            deleteLocalUUid(fileDcim)
        }
    }

    private fun deleteLocalUUid(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    @Synchronized
    private fun saveToLocalPath(filePath: String, id: String) {
        var writer: FileWriter? = null
        try {
            if (TextUtils.isEmpty(id)) return
            val file = File(filePath)
            writer = FileWriter(file)
            writer.write(id)
            writer.flush()
            writer.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            closeFileWriter(writer)
        }
    }

    @Synchronized
    private fun getFromLocalFile(filePath: String?): String? {
        if (filePath == null || filePath.isEmpty()) {
            return null
        }
        var reader: BufferedReader? = null
        return try {
            val file = File(filePath)
            reader = BufferedReader(FileReader(file))
            reader.readLine()
        } catch (e: Exception) {
            Log.e(TAG, "getFromLocalFile: error = $e")
            null
        } finally {
            closeBufferReader(reader)
        }
    }

    private fun closeBufferReader(bufferedReader: BufferedReader?) {
        try {
            bufferedReader?.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun closeFileWriter(FileWriter: FileWriter?) {
        try {
            FileWriter?.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}