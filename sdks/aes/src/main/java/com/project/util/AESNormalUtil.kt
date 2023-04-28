package com.project.util


/**
 * Created by sunsg on 2018/1/31.
 * 通用AES
 */
object AESNormalUtil {

    /**
     * cash loan 通用加密
     */
    @JvmStatic
    fun encrypt(src: String, needUrlEncoder: Boolean = true): String? {
        return AESUtil.mexicoEncrypt(
            src, AesConstant.apiKey, AesConstant.apiIv,
            needUrlEncoder
        )
    }

    /**
     * app数据抓取加密（短信，通话记录，通讯录）
     */
    @JvmStatic
    fun encrypt(src: ByteArray, needUrlEncoder: Boolean = true): String? {
        return AESUtil.mexicoEncrypt(src, AesConstant.bigKey, AesConstant.bigIv, needUrlEncoder)
    }

    /**
     * cash loan 通用解密
     */
    @JvmStatic
    fun decrypt(src: String, needUrlDecoder: Boolean = true): String? {
        return AESUtil.mexicoDecrypt(
            src,
            AesConstant.apiKey, AesConstant.apiIv,
            needUrlDecoder
        )
    }
}