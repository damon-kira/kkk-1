package com.kira.learning.modules.sample

import com.kira.learning.models.SampleImage
import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.models.dao.toDomain
import com.kira.learning.base.repository.BaseRepository
import com.kira.learning.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRepository @Inject constructor(
    private val api: ComposeApiService
) : BaseRepository() {
    private var cached: List<SampleImage>? = null

    suspend fun load(force: Boolean = false): ApiResult<List<SampleImage>> {
        return if (!force && cached != null) {
            ApiResult.Success(cached!!)
        } else {
            safeApiCall {
                api.fetchPhotos().map { it.toDomain() }.also { cached = it }
            }
        }
    }
}
