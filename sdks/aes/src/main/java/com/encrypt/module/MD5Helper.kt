package com.encrypt.module

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

object MD5Helper {
    private val DEBUG = BuildConfig.DEBUG
    private const val TAG = "debug_MD5Helper"

    @JvmStatic
    fun getHexMD5(input: String?): String {
        return if (input == null) "" else getHexMD5(input.toByteArray())
    }

    @JvmStatic
    fun getHexMD5(input: ByteArray): String {
        return ByteConvertor.bytesToHexString(getMd5(input)).orEmpty()
    }

    @JvmStatic
    fun getMd5(input: ByteArray): ByteArray? {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(input)
            return md.digest()
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return null
    }

    @JvmStatic
    fun getMd5(file: File): String? {
        if (!file.exists()) {
            return null
        }
        return try {
            val digest = MessageDigest.getInstance("MD5")
            FileInputStream(file).use { fis ->
                val buffer = ByteArray(8192)
                var read: Int
                while (fis.read(buffer).also { read = it } > 0) {
                    digest.update(buffer, 0, read)
                }
                val md5sum = digest.digest()
                ByteConvertor.bytesToHexString(md5sum)
            }
        } catch (e: Exception) {
            null
        }
    }
}