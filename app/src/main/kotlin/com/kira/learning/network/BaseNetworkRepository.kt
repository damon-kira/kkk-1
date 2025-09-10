package com.kira.learning.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 网络请求Repository基类
 * 提供统一的网络请求处理模式
 */
abstract class BaseNetworkRepository {

    /**
     * 执行网络请求并返回Flow
     */
    protected fun <T> networkCall(
        call: suspend () -> T
    ): Flow<ApiResult<T>> = flow {
        emit(safeApiCallWithMapping { call() })
    }

    /**
     * 执行BaseResponse类型的网络请求
     */
    protected fun <T> baseResponseCall(
        call: suspend () -> BaseResponse<T>
    ): Flow<ApiResult<T>> = flow {
        val result = safeApiCallWithMapping { call() }
        when (result) {
            is ApiResult.Success -> emit(result.data.toApiResult())
            is ApiResult.Error -> emit(result)
            is ApiResult.NetworkUnavailable -> emit(result)
        }
    }
}

/**
 * 示例Repository实现
 */
@Singleton
class ExampleRepository @Inject constructor(
    private val apiService: ComposeApiService
) : BaseNetworkRepository() {

    fun login(request: com.kira.learning.model.LoginRequest): Flow<ApiResult<com.kira.learning.model.LoginData>> {
        return baseResponseCall { apiService.login(request) }
    }

    fun getProfile(): Flow<ApiResult<com.kira.learning.model.dao.UserProfileDTO>> {
        return networkCall { apiService.getProfile() }
    }
}
