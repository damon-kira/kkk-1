package com.util.lib.uuid

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.util.lib.BuildConfig
import com.util.lib.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile

/**
 * Created by weishl on 2022/1/24
 *
 */
object UUidFile {

    private const val DEVICE_ID_FILENAME_NEW = "DEV2"
    private val DEBUG = BuildConfig.DEBUG
    private const val TAG = "debug_UUidFile"

    @JvmStatic
    fun readDeviceIdFile(context: Context, deviceFile: File, decode: Boolean): String? {
        var f: RandomAccessFile? = null
        var deviceId: String? = null
        try {
            f = RandomAccessFile(deviceFile, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            deviceId = if (decode) {
                FileUtils.DES_decrypt(String(bytes), context.packageName)
            } else {
                String(bytes)
            }
        } catch (ex: Throwable) {
            //Caused by: java.lang.ExceptionInInitializerError
        } finally {
            if (f != null) {
                try {
                    f.close()
                } catch (ex: Exception) {
                }

            }
        }
        return deviceId
    }

    @JvmStatic
    fun readFile(context: Context): String {
        val newFile = File(context.filesDir,
            DEVICE_ID_FILENAME_NEW
        )
        var uuid: String? = ""
        if (newFile.exists()) {
            if (DEBUG) {
                Log.d(TAG, "UUID(), newFile exist.")
            }
            //新文件存在，直接读取
           uuid = readDeviceIdFile(
                    context,
                    newFile,
                    true
                )
        }
        return uuid.orEmpty()
    }

    @JvmStatic
    fun writeCashDeviceIdFile(context: Context, deviceId: String, encode: Boolean) {
        var deviceId = deviceId
        if (TextUtils.isEmpty(deviceId)) {
            return
        }

        var out: FileOutputStream? = null
        val newFile = File(context.filesDir,
            DEVICE_ID_FILENAME_NEW
        )
        try {
            out = FileOutputStream(newFile, false)
            if (encode) {
                deviceId = FileUtils.DES_encrypt(deviceId, context.packageName)
            }
            out.write(deviceId.toByteArray())
        } catch (ex: Exception) {
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (ex: Exception) {
                }

            }
        }
    }
}