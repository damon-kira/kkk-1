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
    fun mexicoEncrypt(src: String, needUrlEncoder: Boolean = true): String? {
        return AESUtil.mexicoEncrypt(
            src, AesConstant.apiKey, AesConstant.apiIv,
            needUrlEncoder
        )
    }

    /**
     * cash loan 通用解密
     */
    @JvmStatic
    fun mexicoDecrypt(src: String, needUrlDecoder: Boolean = true): String? {
        return AESUtil.mexicoDecrypt(
            src,
            AesConstant.apiKey, AesConstant.apiIv,
            needUrlDecoder
        )
    }
}