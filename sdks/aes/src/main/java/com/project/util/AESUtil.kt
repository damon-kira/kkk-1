package com.project.util

import android.util.Log
import com.aes.lib.*
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by sunsg on 2017/9/6.
 */

class AESUtil {
    companion object {
        val TAG = "AESUtil"
        val DEBUG = BuildConfig.DEBUG

        /**
         * AES加解密算法
         *
         * key 转换为 16位二进制（String 转换为数组）再base64，再URLencoder
         *
         * @author arix04
         */
        // 加密
        fun mexicoEncrypt(sSrc: String, key: ByteArray, iv: ByteArray, needUrlEncoder:Boolean = true): String? {
            if (DEBUG) {
                Log.i(TAG, "encrypt src = $sSrc")
            }
            if (sSrc.isEmpty()) {
                if (DEBUG) {
                    Log.i(TAG, "encrypt sSrc is isEmpty")
                }
                return sSrc
            }
            return mexicoEncrypt(sSrc.toByteArray(), key, iv, needUrlEncoder)
        }

        /**
         * AES加解密算法
         * key 转换为 16位二进制（String 转换为数组）再base64，再URLencoder
         * @author arix04
         */
        fun mexicoEncrypt(sSrc: ByteArray,  key: ByteArray, iv: ByteArray, needUrlEncoder:Boolean = true): String? {
            try {
                val encryData = Encryptor.aesEncrypt(sSrc, key, iv)
                var finalData = Base64Utils.encode(encryData)
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
        fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
            val byte_3 = ByteArray(byte_1.size + byte_2.size)
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
            System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
            return byte_3
        }

        // 解密
        fun mexicoDecrypt(sSrc: String, key: ByteArray, iv: ByteArray, needUrlDecoder: Boolean = true): String? {
            try {
                var urlDecoder = sSrc
                if (needUrlDecoder) {
                    urlDecoder = URLDecoder.decode(sSrc, "utf-8")
                }
                val decodeData = Base64Utils.decode(urlDecoder)
                val encrypData = Encryptor.aesDecrypt(decodeData, key, iv)
                val finalData = String(encrypData)
                if (DEBUG) {
                    Log.i(TAG,"decrypt = $finalData")
                    Log.i("okhttp","decrypt = $finalData")
                }
                return finalData
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.i(TAG, "e.toString() = $e")
                }
            }
            return null
        }
    }
}

