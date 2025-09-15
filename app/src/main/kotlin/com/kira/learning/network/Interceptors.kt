package com.kira.learning.network

import com.kira.learning.base.keyvalue.SettingsManager
import java.io.IOException
import java.net.SocketTimeoutException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/** 添加通用 Header */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request().newBuilder()
            .addHeader("Accept", NetworkConfig.ACCEPT_TYPE)
            .addHeader("Content-Type", NetworkConfig.CONTENT_TYPE)
            .addHeader("User-Agent", NetworkConfig.USER_AGENT)
            .build()
        return chain.proceed(req)
    }
}

/**
 * 改进的重试拦截器，支持指数退避和网络状态检查
 */
class RetryInterceptor(
    private val maxRetry: Int = NetworkConfig.MAX_RETRY_COUNT,
    private val networkMonitor: NetworkMonitor? = null
) : Interceptor {
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

                // 指数退避
                val delay = NetworkConfig.RETRY_DELAY_MS * (1L shl (attempt - 1))
                Thread.sleep(delay)

            } catch (e: IOException) {
                lastE = e
                attempt++
                if (attempt > maxRetry) throw e

                // 检查网络状态
                networkMonitor?.let { monitor ->
                    if (!monitor.isNetworkAvailable()) {
                        throw java.net.UnknownHostException("Network unavailable")
                    }
                }

                // 指数退避
                val delay = NetworkConfig.RETRY_DELAY_MS * (1L shl (attempt - 1))
                Thread.sleep(delay)
            }
        }
        throw lastE ?: IOException("未知网络错误")
    }
}

interface AuthTokenProvider {
    fun currentToken(): String?
}

class AuthInterceptor(private val provider: AuthTokenProvider) : Interceptor {
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

class Logout401Interceptor(
    private val settings: SettingsManager,
    private val provider: InMemoryAuthTokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val resp = chain.proceed(chain.request())
        if (resp.code == 401) {
            settings.apiToken = null
            settings.apiTokenExpireAt = 0
            provider.updateToken(null)
        }
        return resp
    }
}
