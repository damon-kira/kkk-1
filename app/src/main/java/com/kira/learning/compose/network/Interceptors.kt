package com.kira.learning.compose.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException

/** 添加通用 Header */
class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "KiraApp/1.0 (Android)")
            .build()
        return chain.proceed(req)
    }
}

/** 简单重试拦截器(网络瞬时错误) */
class RetryInterceptor(private val maxRetry: Int = 2): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var lastE: IOException? = null
        while (attempt <= maxRetry) {
            try {
                return chain.proceed(chain.request())
            } catch (e: SocketTimeoutException) {
                lastE = e
                attempt++
                if (attempt > maxRetry) throw e
            } catch (e: IOException) {
                lastE = e
                attempt++
                if (attempt > maxRetry) throw e
            }
        }
        throw lastE ?: IOException("未知网络错误")
    }
}

interface AuthTokenProvider { fun currentToken(): String? }
class AuthInterceptor(private val provider: AuthTokenProvider): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if (original.header("Authorization")?.isNotBlank() == true) return chain.proceed(original)
        val token = provider.currentToken()
        val newReq: Request = if (!token.isNullOrBlank()) {
            original.newBuilder().addHeader("Authorization", "Bearer $token").build()
        } else original
        return chain.proceed(newReq)
    }
}

object ErrorMapper {
    fun map(code: Int?, raw: String?): String = when(code) {
        401 -> "未授权, 请重新登录"
        403 -> "无权限"
        404 -> "未找到资源"
        408 -> "请求超时"
        500 -> "服务器错误"
        else -> raw ?: "未知错误"
    }
}
