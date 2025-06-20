package com.encrypt.module

import android.util.Log
import java.net.URLDecoder
import java.net.URLEncoder

object AesHelper {
    val TAG = "debug_AesHelper"
    val DEBUG = BuildConfig.DEBUG

    /**
     * AES加解密算法
     * key 转换为 16位二进制（String 转换为数组）再base64，再URLencoder
     * @author
     */
    // 加密
    @JvmStatic
    fun encrypt(
        sSrc: String,
        key: ByteArray,
        iv: ByteArray,
        needUrlEncoder: Boolean = true
    ): String? {
        if (sSrc.isEmpty()) {
            return sSrc
        }
        return encrypt(sSrc.toByteArray(), key, iv, needUrlEncoder)
    }

    /**
     * AES加解密算法
     * key 转换为 16位二进制（String 转换为数组）再base64，再URLencoder
     *
     */
    @JvmStatic
    fun encrypt(
        sSrc: ByteArray,
        key: ByteArray,
        iv: ByteArray,
        needUrlEncoder: Boolean = true
    ): String? {
        if (DEBUG) {
            Log.i(TAG, "encrypt src = $sSrc")
        }
        try {
            val encryData = Encryptor.aesEncrypt(sSrc, key, iv)
            var finalData = Base64Helper.encode(encryData)
            if (needUrlEncoder) {
                finalData = URLEncoder.encode(finalData)
            }
            if (DEBUG) {
                Log.d(TAG, "src encrypt final data = $finalData")
            }
            return finalData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    //java 合并两个byte数组
    @JvmStatic
    fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
        val byte_3 = ByteArray(byte_1.size + byte_2.size)
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
        System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
        return byte_3
    }

    // 解密
    @JvmStatic
    fun decrypt(
        sSrc: String,
        key: ByteArray,
        iv: ByteArray,
        needUrlDecoder: Boolean = true
    ): String? {
        try {
            var urlDecoder = sSrc
            if (needUrlDecoder) {
                urlDecoder = URLDecoder.decode(sSrc, "utf-8")
            }
            val decodeData = Base64Helper.decode(urlDecoder)
            val encrypData = Encryptor.aesDecrypt(decodeData, key, iv)
            val finalData = String(encrypData)
            if (DEBUG) {
                Log.i("okhttp", "decrypt = $finalData")
            }
            return finalData
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "e.toString() = $e")
            }
        }
        return null
    }
}

