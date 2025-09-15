package com.kira.learning.base.repository

import com.kira.learning.network.ApiResult
import com.kira.learning.network.BaseResponse
import com.kira.learning.network.apiCall
import com.kira.learning.network.apiCallFlow
import com.kira.learning.network.apiCallWithRetry
import com.kira.learning.network.GlobalErrorHandler
import com.kira.learning.network.toApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach

/**
 * 统一的Repository基类
 * 提供标准化的API调用方式和错误处理
 */
abstract class BaseRepository {

    /**
     * 标准的挂起API调用
     * 所有Repository的suspend方法都应使用这个
     */
    protected suspend fun <T> executeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> T
    ): ApiResult<T> {
        return apiCall(dispatcher) { call() }
    }

    /**
     * 标准的Flow API调用
     * 支持Loading状态，适用于UI响应式更新
     */
    protected fun <T> executeApiFlow(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> T
    ): Flow<ApiResult<T>> {
        return apiCallFlow(dispatcher) { call() }
            .onEach { result ->
                // 统一错误处理
                if (result is ApiResult.Error) {
                    GlobalErrorHandler.handleError(error = result, showUserMessage = false)
                }
            }
    }

    /**
     * 带重试机制的API调用
     * 适用于重要的网络请求
     */
    protected fun <T> executeApiWithRetry(
        maxRetries: Int = 3,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> T
    ): Flow<ApiResult<T>> {
        return apiCallWithRetry(maxRetries, dispatcher) { call() }
            .onEach { result ->
                if (result is ApiResult.Error) {
                    GlobalErrorHandler.handleError(error = result, showUserMessage = false)
                }
            }
    }

    /**
     * 标准的BaseResponse API调用
     * 自动转换BaseResponse到ApiResult
     */
    protected suspend fun <T> executeBaseResponseCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> BaseResponse<T>
    ): ApiResult<T> {
        return apiCall(dispatcher) {
            val response = call()
            when (val result = response.toApiResult()) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> throw Exception(result.message)
                is ApiResult.Loading -> throw Exception("Unexpected loading state")
                is ApiResult.NetworkUnavailable -> throw Exception("Network unavailable")
            }
        }
    }

    /**
     * BaseResponse Flow调用
     */
    protected fun <T> executeBaseResponseFlow(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> BaseResponse<T>
    ): Flow<ApiResult<T>> = flow {
        emit(ApiResult.Loading())
        val response = executeBaseResponseCall(dispatcher, call)
        emit(response)
    }

    /**
     * 带缓存的数据加载
     * 先从缓存读取，再从网络更新
     */
    protected fun <T> loadWithCache(
        loadFromCache: suspend () -> T?,
        loadFromNetwork: suspend () -> T,
        saveToCache: suspend (T) -> Unit,
        forceRefresh: Boolean = false,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<ApiResult<T>> = flow {
        emit(ApiResult.Loading("加载中..."))

        // 如果不强制刷新，先尝试从缓存加载
        if (!forceRefresh) {
            try {
                val cached = loadFromCache()
                if (cached != null) {
                    emit(ApiResult.Success(cached))
                }
            } catch (_: Exception) {
                // 缓存读取失败，继续网络请求
            }
        }

        // 从网络加载
        val networkResult = executeApiCall(dispatcher) {
            val networkData = loadFromNetwork()
            try {
                saveToCache(networkData)
            } catch (_: Exception) {
                // 缓存保存失败不影响数据返回
            }
            networkData
        }
        emit(networkResult)
    }

    /**
     * 分页数据加载
     */
    protected fun <T> loadPagedData(
        page: Int,
        pageSize: Int = 20,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend (page: Int, pageSize: Int) -> BaseResponse<PageData<T>>
    ): Flow<ApiResult<PageData<T>>> {
        return executeApiFlow(dispatcher) {
            val response = call(page, pageSize)
            when (val result = response.toApiResult()) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> throw Exception(result.message)
                is ApiResult.Loading -> throw Exception("Unexpected loading state")
                is ApiResult.NetworkUnavailable -> throw Exception("Network unavailable")
            }
        }
    }

    /**
     * 文件上传
     */
    protected fun <T> uploadFile(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        call: suspend () -> BaseResponse<T>
    ): Flow<ApiResult<T>> {
        return executeApiWithRetry(maxRetries = 1, dispatcher = dispatcher) {
            val response = call()
            when (val result = response.toApiResult()) {
                is ApiResult.Success -> result.data
                is ApiResult.Error -> throw Exception(result.message)
                is ApiResult.Loading -> throw Exception("Unexpected loading state")
                is ApiResult.NetworkUnavailable -> throw Exception("Network unavailable")
            }
        }
    }
}

/**
 * 分页数据结构
 */
data class PageData<T>(
    val items: List<T>,
    val hasNextPage: Boolean,
    val currentPage: Int,
    val totalPages: Int = 0,
    val totalItems: Long = 0
)
