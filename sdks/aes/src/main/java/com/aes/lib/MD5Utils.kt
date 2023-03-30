package com.aes.lib

import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Created by weishl on 2022/6/17
 *
 */
object MD5Utils {
    private val DEBUG = BuildConfig.DEBUG
    private const val TAG = "MD5Utils"

    @JvmStatic
    fun getMD5(input: String?): String {
        return if (input == null) "" else getMD5(input.toByteArray())
    }

    @JvmStatic
    fun getMD5(input: ByteArray): String {
        return ByteConvertor.bytesToHexString(MD5(input)).orEmpty()
    }

    @JvmStatic
    private fun MD5(input: ByteArray): ByteArray? {
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

    /**
     * 字符串MD5加密,在做Hex
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun des_encrypt_hex(input: String, key: String): String? {
        try {
            val output = des_encrypt(input, key)
            return ByteConvertor.bytesToHexString(output)
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return ""
    }

    /**
     * Hex字符串解密
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun des_decrypt_hex(input: String?, key: String): String {
        try {
            val bytes = ByteConvertor.hexStringToBytes(input)
            val output = des_decrypt(bytes, key) ?: byteArrayOf()
            return String(output)
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return ""
    }

    /**
     * 字符串MD5加密
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun des_encrypt(input: String, key: String): ByteArray? {
        try {
            return des_encrypt(input.toByteArray(), key)
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return null
    }

    /**
     * byte数组MD5加密
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun des_encrypt(input: ByteArray?, key: String): ByteArray? {
        try {
            val e = SecureRandom()
            val dks = DESKeySpec(MD5(key.toByteArray()))
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val secretKey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, e)
            return cipher.doFinal(input)
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return null
    }

    /**
     * byte数组解密
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun des_decrypt(input: ByteArray?, key: String): ByteArray? {
        try {
            val sr = SecureRandom()
            val dks = DESKeySpec(MD5(key.toByteArray()))
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val secretKey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr)
            return cipher.doFinal(input)
        } catch (e: Throwable) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        }
        return null
    }

    /**
     * 加密  pwd 不用md5
     *
     * @param src
     * @param pwd
     * @return
     */
    @JvmStatic
    fun des_encrypt_no_md5(src: ByteArray?, pwd: String): ByteArray? {
        return try {
            val sr = SecureRandom()
            val dks = DESKeySpec(pwd.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val securekey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(1, securekey, sr)
            cipher.doFinal(src)
        } catch (var7: Throwable) {
            null
        }
    }

    /**
     * 加密  pwd 不用md5
     *
     * @param src
     * @param pwd
     * @return
     */
    @JvmStatic
    fun des_decrypt_no_md5(src: ByteArray?, pwd: String): ByteArray? {
        return try {
            val sr = SecureRandom()
            val dks = DESKeySpec(pwd.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val securekey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(2, securekey, sr)
            cipher.doFinal(src)
        } catch (var7: Throwable) {
            null
        }
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