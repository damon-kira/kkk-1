package com.devoption.finance

/**
 * Created by weishl on 2021/7/13
 *
 */

internal class ApiGlobalConfig {
    var api: ApiInfo = ApiInfo()
    var bigData = ApiInfo()
    var h5Host: String = ""
}

internal class ApiConfig {
    var debug = ApiGlobalConfig().also {
//        it.api.apply {
//            host = "https://api-test.microloan.mx.fg-example.com/"
//            iv = KeyUtil.apiIvD
//            key = KeyUtil.apiKeyD
//            secret = "FA5BDMX98/DA\$DAF"
//        }
//        it.bigData.apply {
//            host = "https://ginlogs-test.microloan.mx.fg-example.com/v2/basedata/okredito"
//            iv = KeyUtil.bigIvD
//            key = KeyUtil.bigKeyD
//        }
//        it.h5Host = "https://h5.microloan.mx.fg-example.com"
    }

    var release = ApiGlobalConfig().also {
//        it.api.apply {
//            host = "https://api.okredito.com/"
//            iv = KeyUtil.apiIvR
//            key = KeyUtil.apiKeyR
//            secret = "26E3EE1542710866"
//        }
//        it.bigData.apply {
//            host = "https://bigdata.okredito.com/v2/basedata/okredito"
//            iv = KeyUtil.bigIvR
//            key = KeyUtil.bigKeyR
//        }
//        it.h5Host = "https://h5.okredito.com"
    }
}

internal class ApiInfo {
    var host: String = ""
    var key: ByteArray = byteArrayOf()
    var iv: ByteArray = byteArrayOf()
    var secret: String = ""
}