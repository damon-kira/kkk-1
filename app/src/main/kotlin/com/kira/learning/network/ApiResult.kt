package com.kira.learning.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 统一的API返回结果封装
 * 所有网络请求都应该返回这个类型，确保一致性
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val code: Int? = null,
        val message: String,
        val errorType: ErrorType = ErrorType.UNKNOWN,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>()

    data class Loading(val message: String = "加载中...") : ApiResult<Nothing>()
    object NetworkUnavailable : ApiResult<Nothing>()
}

/**
 * 错误类型枚举，用于更精确的错误处理
 */
enum class ErrorType {
    NETWORK,        // 网络错误
    SERVER,         // 服务器错误
    AUTHENTICATION, // 认证错误
    AUTHORIZATION,  // 授权错误
    VALIDATION,     // 验证错误
    TIMEOUT,        // 超时错误
    UNKNOWN         // 未知错误
}

/**
 * 扩展函数：判断是否为成功状态
 */
val <T> ApiResult<T>.isSuccess: Boolean
    get() = this is ApiResult.Success

/**
 * 扩展函数：判断是否为错误状态
 */
val <T> ApiResult<T>.isError: Boolean
    get() = this is ApiResult.Error

/**
 * 扩展函数：判断是否为加载状态
 */
val <T> ApiResult<T>.isLoading: Boolean
    get() = this is ApiResult.Loading

/** 基础映射；仅对 Success 进行数据转换 */
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Error -> this
    is ApiResult.Loading -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** 扁平化映射 */
inline fun <T, R> ApiResult<T>.flatMap(transform: (T) -> ApiResult<R>): ApiResult<R> = when (this) {
    is ApiResult.Success -> transform(data)
    is ApiResult.Error -> this
    is ApiResult.Loading -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** 错误恢复 */
inline fun <T> ApiResult<T>.recover(block: (ApiResult.Error) -> T): ApiResult<T> = when (this) {
    is ApiResult.Success -> this
    is ApiResult.Error -> ApiResult.Success(block(this))
    is ApiResult.Loading -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** mapNotNull：null -> Error */
inline fun <T, R> ApiResult<T>.mapNotNull(transform: (T) -> R?): ApiResult<R> = when (this) {
    is ApiResult.Success -> {
        val v = transform(data)
        if (v == null) ApiResult.Error(message = "mapNotNull result is null") else ApiResult.Success(
            v
        )
    }

    is ApiResult.Error -> this
    is ApiResult.Loading -> this
    is ApiResult.NetworkUnavailable -> ApiResult.NetworkUnavailable
}

/** fold: 收敛 */
inline fun <T, R> ApiResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (ApiResult.Error) -> R,
    onLoading: (String) -> R,
    onNetworkUnavailable: () -> R
): R = when (this) {
    is ApiResult.Success -> onSuccess(data)
    is ApiResult.Error -> onError(this)
    is ApiResult.Loading -> onLoading(message)
    is ApiResult.NetworkUnavailable -> onNetworkUnavailable()
}

inline fun <T> ApiResult<T>.getOrNull(): T? = (this as? ApiResult.Success)?.data
inline fun <T> ApiResult<T>.getOrElse(default: T): T = when (this) {
    is ApiResult.Success -> data
    else -> default
}

inline fun <T> ApiResult<T>.onSuccess(block: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) block(data)
    return this
}

inline fun <T> ApiResult<T>.onError(block: (ApiResult.Error) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) block(this)
    return this
}

inline fun <T> ApiResult<T>.onLoading(block: (String) -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) block(message)
    return this
}

inline fun <T> ApiResult<T>.onNetworkUnavailable(block: () -> Unit): ApiResult<T> {
    if (this is ApiResult.NetworkUnavailable) block()
    return this
}

/** 统一的挂起网络调用封装 - 所有API调用都应使用这个方法 */
suspend fun <T> apiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): ApiResult<T> = withContext(dispatcher) {
    try {
        ApiResult.Success(block())
    } catch (e: UnknownHostException) {
        ApiResult.NetworkUnavailable
    } catch (e: ConnectException) {
        ApiResult.NetworkUnavailable
    } catch (e: SocketTimeoutException) {
        ApiResult.Error(
            message = "请求超时，请检查网络连接",
            errorType = ErrorType.TIMEOUT,
            throwable = e
        )
    } catch (e: HttpException) {
        val errorType = when (e.code()) {
            401 -> ErrorType.AUTHENTICATION
            403 -> ErrorType.AUTHORIZATION
            in 400..499 -> ErrorType.VALIDATION
            in 500..599 -> ErrorType.SERVER
            else -> ErrorType.UNKNOWN
        }
        ApiResult.Error(
            code = e.code(),
            message = ErrorMapper.mapHttpError(e.code(), e.message()),
            errorType = errorType,
            throwable = e
        )
    } catch (e: IOException) {
        ApiResult.Error(
            message = "网络连接异常",
            errorType = ErrorType.NETWORK,
            throwable = e
        )
    } catch (e: Exception) {
        ApiResult.Error(
            message = e.message ?: "未知错误",
            errorType = ErrorType.UNKNOWN,
            throwable = e
        )
    }
}

/**
 * Flow版本的API调用封装 - 支持响应式编程
 * 统一返回Flow<ApiResult<T>>格式
 */
fun <T> apiCallFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading())
    emit(apiCall(dispatcher, block))
}.flowOn(dispatcher)

/**
 * 带重试机制的API调用
 */
fun <T> apiCallWithRetry(
    maxRetries: Int = 3,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> T
): Flow<ApiResult<T>> = flow {
    emit(ApiResult.Loading())
    emit(apiCall(dispatcher, block))
}.retryWhen { cause, attempt ->
    attempt < maxRetries && (cause is IOException || cause is SocketTimeoutException)
}.catch { e ->
    emit(
        ApiResult.Error(
            message = e.message ?: "重试失败",
            errorType = ErrorType.NETWORK,
            throwable = e
        )
    )
}.flowOn(dispatcher)

/** BaseResponse 转：统一转换服务端响应格式 */
fun <T> BaseResponse<T>.toApiResult(): ApiResult<T> = when {
    isSuccess() -> {
        if (data != null) {
            ApiResult.Success(data)
        } else {
            ApiResult.Error(
                code = code,
                message = msg ?: "数据为空",
                errorType = ErrorType.SERVER
            )
        }
    }

    else -> {
        val errorType = when (code) {
            401 -> ErrorType.AUTHENTICATION
            403 -> ErrorType.AUTHORIZATION
            in 400..499 -> ErrorType.VALIDATION
            in 500..599 -> ErrorType.SERVER
            else -> ErrorType.UNKNOWN
        }
        ApiResult.Error(
            code = code,
            message = ErrorMapper.mapBusinessError(code, msg),
            errorType = errorType
        )
    }
}
