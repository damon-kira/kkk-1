package com.kira.learning.compose.module.sample

import retrofit2.http.GET

/** 简单示例 API: 取前 N 条公开图片 (jsonplaceholder 示例数据) */
interface SampleApi {
    @GET("photos?_limit=20")
    suspend fun fetchPhotos(): List<PhotoDTO>
}

// 网络 DTO
data class PhotoDTO(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val url: String,
)

fun PhotoDTO.toDomain() = SampleImage(
    id = id.toString(),
    title = title,
    thumbnailUrl = thumbnailUrl,
)

