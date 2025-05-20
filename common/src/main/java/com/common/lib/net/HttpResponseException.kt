package com.common.lib.net

class HttpResponseException(var code: Int, message: String?, e: Throwable?) : Exception(message) {
    override fun toString(): String {
        return "HttpResponseException(code=$code,message=$message)"
    }
}