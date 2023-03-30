package com.common.lib.net

/**
 * Created by weisl on 2018/12/19.
 */
class HttpResponseException(var code: Int, message: String?) : Exception(message) {
    override fun toString(): String {
        return "HttpResponseException(code=$code,message=$message)"
    }
}