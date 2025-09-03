package com.kira.learning.network

import com.kira.learning.module.auth.LoginRequest
import com.kira.learning.module.auth.LoginResponse
import com.kira.learning.model.QuestionProcessInfo
import com.kira.learning.model.dao.AIResponseInfo
import com.kira.learning.model.dao.AppSettingsDTO
import com.kira.learning.model.dao.PhotoDTO
import com.kira.learning.model.dao.UpdateAvatarRequest
import com.kira.learning.model.dao.UpdateProfileRequest
import com.kira.learning.model.dao.UserProfileDTO
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url

interface ComposeApiService {

    @POST("/v1/chat/completions123456")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun aiSendRequest(@Body body: RequestBody): BaseResponse<AIResponseInfo>

    @POST("/v1/chat/searchQuestion")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun searchQuestion(@Body body: RequestBody): BaseResponse<QuestionProcessInfo>

    @POST("/v1/kira/activities")
    @Headers("Authorization:Bearer sk-FehZFlRbSd6NlCgUt8o6RxW7fQgiwWm8sh9uguhqMFQjZ1uU")
    suspend fun getQuizInfo(@Body body: RequestBody): BaseResponse<Document>

    // ===== Compose Module Unified APIs =====
    // Sample photos (use full url so we don't need second Retrofit)
    @GET
    suspend fun fetchPhotos(@Url url: String = "https://jsonplaceholder.typicode.com/photos?_limit=20"): List<PhotoDTO>

    // Profile
    @GET("/compose/profile")
    suspend fun getProfile(): UserProfileDTO

    @PUT("/compose/profile")
    suspend fun updateProfile(@Body req: UpdateProfileRequest): UserProfileDTO

    @PUT("/compose/profile/avatar")
    suspend fun updateAvatar(@Body req: UpdateAvatarRequest): UserProfileDTO

    @PUT("/compose/profile/settings")
    suspend fun updateSettings(@Body settings: AppSettingsDTO): AppSettingsDTO

    @POST("login/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
}
