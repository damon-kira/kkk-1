package com.kira.learning.compose.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRepository @Inject constructor(
    private val api: SampleApi
) {
    private var cached: List<SampleImage>? = null

    suspend fun load(force: Boolean = false): Result<List<SampleImage>> = withContext(Dispatchers.IO) {
        if (!force) {
            cached?.let { return@withContext Result.success(it) }
        }
        return@withContext runCatching {
            val data = api.fetchPhotos().map { it.toDomain() }
            cached = data
            data
        }
    }
}

