package com.kira.learning.base.repository

import com.kira.learning.network.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * 通用Repository基类
 * 提供数据加载、缓存和错误处理的基础功能
 */
abstract class BaseRepository {

    /**
     * 执行API调用并处理结果
     */
    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ApiResult<T> {
        return try {
            val result = apiCall()
            ApiResult.Success(result)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    /**
     * 处理异常并转换为ApiResult
     */
    private fun <T> handleException(e: Exception): ApiResult<T> {
        return when (e) {
            is UnknownHostException,
            is ConnectException -> ApiResult.NetworkUnavailable
            else -> ApiResult.Error(message = e.message ?: "未知错误", throwable = e)
        }
    }

    /**
     * 执行带缓存的数据加载
     */
    protected fun <T> loadWithCache(
        loadFromCache: suspend () -> T?,
        loadFromNetwork: suspend () -> T,
        saveToCache: suspend (T) -> Unit,
        forceRefresh: Boolean = false
    ): Flow<ApiResult<T>> = flow {
        // 如果不强制刷新，先尝试从缓存加载
        if (!forceRefresh) {
            val cached = loadFromCache()
            if (cached != null) {
                emit(ApiResult.Success(cached))
            }
        }

        // 从网络加载
        emit(safeApiCall {
            val networkData = loadFromNetwork()
            saveToCache(networkData)
            networkData
        })
    }
}

/**
 * 带分页的Repository基类
 */
abstract class BasePagingRepository : BaseRepository() {

    data class PageData<T>(
        val items: List<T>,
        val hasNextPage: Boolean,
        val currentPage: Int
    )

    /**
     * 加载分页数据
     */
    protected suspend fun <T> loadPage(
        page: Int,
        pageSize: Int = 20,
        loadPageData: suspend (page: Int, pageSize: Int) -> List<T>
    ): ApiResult<PageData<T>> {
        return safeApiCall {
            val items = loadPageData(page, pageSize)
            PageData(
                items = items,
                hasNextPage = items.size == pageSize,
                currentPage = page
            )
        }
    }
}
