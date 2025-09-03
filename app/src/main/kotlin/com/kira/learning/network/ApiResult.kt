package com.kira.learning.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ApiResult<out T> {
    data class Success<T>(val data: T): ApiResult<T>()
    data class Error(val code: Int? = null, val message: String, val throwable: Throwable? = null): ApiResult<Nothing>()
    object NetworkUnavailable: ApiResult<Nothing>()
}

/** 基础映射；仅对 Success 进行数据转换 */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Error -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** 扁平化映射 */
inline fun <T, R> ApiResult<T>.flatMap(transform: (T) -> ApiResult<R>): ApiResult<R> = when (this) {
    is ApiResult.Success -> transform(data)
    is ApiResult.Error -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** 错误恢复 */
inline fun <T> ApiResult<T>.recover(block: (ApiResult.Error) -> T): ApiResult<T> = when (this) {
    is ApiResult.Success -> this
    is ApiResult.Error -> ApiResult.Success(block(this))
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** mapNotNull：null -> Error */
inline fun <T, R> ApiResult<T>.mapNotNull(transform: (T) -> R?): ApiResult<R> = when (this) {
    is ApiResult.Success -> {
        val v = transform(data)
        if (v == null) ApiResult.Error(message = "mapNotNull result is null") else ApiResult.Success(v)
    }
    is ApiResult.Error -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** fold: 收敛 */
inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (ApiResult.Error) -> R,
    onNetworkUnavailable: () -> R
): R = when (this) {
    is ApiResult.Success -> onSuccess(data)
    is ApiResult.Error -> onError(this)
    is ApiResult.NetworkUnavailable -> onNetworkUnavailable()
}

inline fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data
inline fun <T> ApiResult<T>.getOrElse(default: () -> T): T = when (this) {
    is ApiResult.Success -> data
    else -> default()
}
inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> { if (this is ApiResult.Success) block(data); return this }
inline fun <T> ApiResult<T>.onError(block: (ApiResult.Error) -> Unit): ApiResult<T> { if (this is ApiResult.Error) block(this); return this }
inline fun <T> ApiResult<T>.onNetworkUnavailable(block: () -> Unit): ApiResult<T> { if (this is ApiResult.NetworkUnavailable) block(); return this }

/** 挂起网络调用封装 */
suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        ApiResult.Success(block())
    } catch (e: IOException) {
        ApiResult.Error(message = e.message ?: "IO Error", throwable = e)
    } catch (e: HttpException) {
        ApiResult.Error(code = e.code(), message = e.message(), throwable = e)
    } catch (e: Throwable) {
        ApiResult.Error(message = e.message ?: "Unknown Error", throwable = e)
    }
}

/** BaseResponse 转换：默认 successCode == ResponseCode.SUCCESS_CODE */
fun <T> BaseResponse<T>.toApiResult(successCode: Int = ResponseCode.SUCCESS_CODE): ApiResult<T> =
    if (code == successCode) {
        val d = data
        if (d == null) ApiResult.Error(code = code, message = msg ?: "Empty body") else ApiResult.Success(d)
    } else ApiResult.Error(code = code, message = msg ?: "Unknown error")

/** Flow 扩展：仅对 Success.data 做转换 */
inline fun <T, R> Flow<ApiResult<T>>.mapData(crossinline transform: (T) -> R): Flow<ApiResult<R>> =
    onEach { }
        .let { upstream -> flow {
            upstream.collect { r ->
                when (r) {
                    is ApiResult.Success -> emit(ApiResult.Success(transform(r.data)))
                    is ApiResult.Error -> emit(r)
                    is ApiResult.NetworkUnavailable -> emit(ApiResult.NetworkUnavailable)
                }
            }
        } }

inline fun <T> Flow<ApiResult<T>>.onSuccessFlow(crossinline block: suspend (T) -> Unit): Flow<ApiResult<T>> = onEach { if (it is ApiResult.Success) block(it.data) }
inline fun <T> Flow<ApiResult<T>>.onErrorFlow(crossinline block: suspend (ApiResult.Error) -> Unit): Flow<ApiResult<T>> = onEach { if (it is ApiResult.Error) block(it) }
inline fun <T> Flow<ApiResult<T>>.onNetworkUnavailableFlow(crossinline block: suspend () -> Unit): Flow<ApiResult<T>> = onEach { if (it is ApiResult.NetworkUnavailable) block() }

fun <T> flowApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Flow<ApiResult<T>> = flow { emit(apiCall(dispatcher, block)) }

/** 组合两个 ApiResult Flow */
fun <A, B, R> combineResults(
    fa: Flow<ApiResult<A>>,
    fb: Flow<ApiResult<B>>,
    transform: (A, B) -> R
): Flow<ApiResult<R>> = combine(fa, fb) { ra, rb ->
    when {
        ra is ApiResult.Error -> ra
        rb is ApiResult.Error -> rb
        ra is ApiResult.NetworkUnavailable || rb is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
        ra is ApiResult.Success && rb is ApiResult.Success -> ApiResult.Success(transform(ra.data, rb.data))
        else -> ApiResult.Error(message = "Unknown combined state")
    }
}

/** 合并多个同类型 Flow */
fun <T> List<Flow<ApiResult<T>>>.mergeAllResults(): Flow<ApiResult<List<T>>> = when (size) {
    0 -> flow { emit(ApiResult.Success(emptyList())) }
    1 -> this[0].mapData { listOf(it) }
    else -> combine(this) { arr ->
        var networkUnavailable = false
        val dataList = ArrayList<T>(arr.size)
        arr.forEach { r ->
            when (r) {
                is ApiResult.Success -> dataList += r.data
                is ApiResult.Error -> return@combine r
                is ApiResult.NetworkUnavailable -> networkUnavailable = true
            }
        }
        if (networkUnavailable) ApiResult.NetworkUnavailable else ApiResult.Success(dataList)
    }
}
