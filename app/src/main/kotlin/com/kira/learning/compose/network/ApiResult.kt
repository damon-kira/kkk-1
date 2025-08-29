package com.kira.learning.compose.network

import com.common.lib.net.bean.BaseResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error(val code: Int? = null, val message: String, val throwable: Throwable? = null): ApiResult<Nothing>()
    object NetworkUnavailable: ApiResult<Nothing>()
}

inline fun <T,R> ApiResult<T>.map(transform: (T)->R): ApiResult<R> = when(this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Error -> this // T 与 R 协变, 直接返回
    ApiResult.NetworkUnavailable -> this
} as ApiResult<R>

inline fun <T,R> ApiResult<T>.flatMap(transform: (T)->ApiResult<R>): ApiResult<R> = when(this) {
    is ApiResult.Success -> transform(data)
    is ApiResult.Error -> this
    ApiResult.NetworkUnavailable -> this
} as ApiResult<R>

inline fun <T> ApiResult<T>.recover(block: (ApiResult.Error)->T): ApiResult<T> = when(this) {
    is ApiResult.Success -> this
    is ApiResult.Error -> ApiResult.Success(block(this))
    ApiResult.NetworkUnavailable -> this
}

inline fun <T> ApiResult<T>.onSuccess(block:(T)->Unit): ApiResult<T> { if (this is ApiResult.Success) block(data); return this }
inline fun <T> ApiResult<T>.onError(block:(ApiResult.Error)->Unit): ApiResult<T> { if (this is ApiResult.Error) block(this); return this }
fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data

suspend inline fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        ApiResult.Success(block())
    } catch (e: IOException) {
        ApiResult.Error(message = e.message ?: "网络错误", throwable = e)
    } catch (e: HttpException) {
        ApiResult.Error(code = e.code(), message = e.message(), throwable = e)
    } catch (e: Throwable) {
        ApiResult.Error(message = e.message ?: "未知错误", throwable = e)
    }
}

inline fun <T> BaseResponse<T>.toApiResult(): ApiResult<T> = if (isSuccess()) {
    val d = data
    if (d == null) ApiResult.Error(code = code, message = "空数据") else ApiResult.Success(d)
} else ApiResult.Error(code = code, message = ErrorMapper.map(code, msg))
