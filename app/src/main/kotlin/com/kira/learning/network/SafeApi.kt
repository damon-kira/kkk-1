package com.kira.learning.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.CancellationException

/**
 * 与已有 apiCall 区分：safeApiCall 会把典型网络不可用场景折叠为 NetworkUnavailable，
 * 其余异常包装成 Error。调用方只需处理 Success / Error / NetworkUnavailable 三态。
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        val data = block()
        ApiResult.Success(data)
    } catch (ce: CancellationException) { // 协程取消向外抛
        throw ce
    } catch (e: UnknownHostException) {
        ApiResult.NetworkUnavailable
    } catch (e: SocketTimeoutException) {
        ApiResult.Error(message = e.message ?: "Timeout", throwable = e)
    } catch (e: IOException) { // 其他 IO
        ApiResult.Error(message = e.message ?: "IO Error", throwable = e)
    } catch (e: Throwable) {
        ApiResult.Error(message = e.message ?: "Unknown Error", throwable = e)
    }
}
