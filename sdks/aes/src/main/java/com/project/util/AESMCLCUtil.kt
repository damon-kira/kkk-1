package com.project.util


/**
 * Created by sunsg on 2018/1/31.
 * 短信 通话记录 联系人AES
 */
object AESMCLCUtil {
    /**
     * app数据抓取加密（短信，通话记录，通讯录）
     */
    @JvmStatic
    fun encryptMessageCallLogContact(src: String): String? {
        return AESUtil.mexicoEncrypt(src, AesConstant.bigKey, AesConstant.bigIv)
    }

    /**
     * app数据抓取加密（短信，通话记录，通讯录）
     */
    @JvmStatic
    fun encryptMessageCallLogContact(src: ByteArray): String? {
        return AESUtil.mexicoEncrypt(src, AesConstant.bigKey, AesConstant.bigIv)
    }

    /**
     * app数据抓取解密（短信，通话记录，通讯录）
     */
    @JvmStatic
    fun decryptMessageCallLogContact(src: String): String? {
        return AESUtil.mexicoDecrypt(src,  AesConstant.bigKey, AesConstant.bigIv)
    }
}