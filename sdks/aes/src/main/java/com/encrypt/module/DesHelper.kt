package com.encrypt.module

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

object DesHelper {


    /**
     * 字符串MD5加密,在做Hex
     *
     * @param input
     * @param key
     * @return
     */
    @JvmStatic
    fun encryptHex(input: String, key: String): String? {
        try {
            val output = encrypt(input, key)
            return ByteConvertor.bytesToHexString(output)
        } catch (e: Exception) {
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
    fun decryptHex(input: String?, key: String): String {
        try {
            val bytes = ByteConvertor.hexStringToBytes(input)
            val output = decrypt(bytes, key) ?: byteArrayOf()
            return String(output)
        } catch (e: Exception) {
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
    fun encrypt(input: String, key: String): ByteArray? {
        try {
            return encrypt(input.toByteArray(), key)
        } catch (e: Exception) {
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
    fun encrypt(input: ByteArray?, key: String, keyMd5: Boolean = false): ByteArray? {
        try {
            val e = SecureRandom()
            val finalKey = if (keyMd5) MD5Helper.getMd5(key.toByteArray()) else key.toByteArray()
            val dks = DESKeySpec(finalKey)
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val secretKey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, e)
            return cipher.doFinal(input)
        } catch (e: Exception) {
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
    fun decrypt(input: ByteArray?, key: String, keyMd5: Boolean = false): ByteArray? {
        try {
            val sr = SecureRandom()
            val finalKey = if (keyMd5) MD5Helper.getMd5(key.toByteArray()) else key.toByteArray()
            val dks = DESKeySpec(finalKey)
            val keyFactory = SecretKeyFactory.getInstance("DES")
            val secretKey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance("DES")
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr)
            return cipher.doFinal(input)
        } catch (e: Throwable) {
        }
        return null
    }
}