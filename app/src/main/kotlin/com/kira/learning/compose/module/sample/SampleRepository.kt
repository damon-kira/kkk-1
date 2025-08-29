package com.kira.learning.compose.module.sample

import com.kira.learning.compose.network.ApiResult
import com.kira.learning.compose.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRepository @Inject constructor(
    private val api: SampleApi
) {
    private var cached: List<SampleImage>? = null

    suspend fun load(force: Boolean = false): ApiResult<List<SampleImage>> {
        if (!force) cached?.let { return ApiResult.Success(it) }
        return safeApiCall { api.fetchPhotos().map { it.toDomain() }.also { cached = it } }
    }
}
