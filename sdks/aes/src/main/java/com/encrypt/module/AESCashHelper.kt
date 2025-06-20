package com.encrypt.module

object AESCashHelper {

    @JvmStatic
    fun encrypt(src: String, needUrlEncoder: Boolean = true): String? {
        return AesHelper.encrypt(
            src, AesConstant.apiKey, AesConstant.apiIv,
            needUrlEncoder
        )
    }


    @JvmStatic
    fun decrypt(src: String, needUrlDecoder: Boolean = true): String? {
        return AesHelper.decrypt(
            src,
            AesConstant.apiKey, AesConstant.apiIv,
            needUrlDecoder
        )
    }
}