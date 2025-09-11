package com.kira.learning.base.repository

import com.kira.learning.network.ApiResult
import com.kira.learning.network.BaseNetworkRepository
import com.kira.learning.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * 通用Repository基类
 * 继承BaseNetworkRepository，提供数据加载、缓存和错误处理的基础功能
 */
abstract class BaseRepository : BaseNetworkRepository() {

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
        val networkResult = safeApiCall {
            val networkData = loadFromNetwork()
            saveToCache(networkData)
            networkData
        }
        emit(networkResult)
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
