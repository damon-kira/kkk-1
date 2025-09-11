package com.kira.learning.network

import com.kira.learning.models.LoginRequest
import com.kira.learning.models.LoginData
import com.kira.learning.models.QuestionProcessInfo
import com.kira.learning.models.RoleSwitchRequest
import com.kira.learning.models.ValidationResponse
import com.kira.learning.models.dao.AIResponseInfo
import com.kira.learning.models.dao.AppSettingsDTO
import com.kira.learning.models.dao.PhotoDTO
import com.kira.learning.models.dao.UpdateAvatarRequest
import com.kira.learning.models.dao.UpdateProfileRequest
import com.kira.learning.models.dao.UserProfileDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ComposeApiService {

    @POST("/v1/chat/completions123456")
    suspend fun aiSendRequest(@Body body: RequestBody): BaseResponse<AIResponseInfo>

    @POST("/v1/chat/searchQuestion")
    suspend fun searchQuestion(@Body body: RequestBody): BaseResponse<QuestionProcessInfo>

    @POST("/v1/kira/activities")
    suspend fun getQuizInfo(@Body body: RequestBody): BaseResponse<Document>

    // ===== Compose Module Unified APIs =====
    // Sample photos (use full url so we don't need second Retrofit)
    @GET
    suspend fun fetchPhotos(@Url url: String = "https://jsonplaceholder.typicode.com/photos?_limit=20"): List<PhotoDTO>

    // Profile APIs
    @GET("/compose/profile")
    suspend fun getProfile(): UserProfileDTO

    @PUT("/compose/profile")
    suspend fun updateProfile(@Body req: UpdateProfileRequest): UserProfileDTO

    @PUT("/compose/profile/avatar")
    suspend fun updateAvatar(@Body req: UpdateAvatarRequest): UserProfileDTO

    @PUT("/compose/profile/settings")
    suspend fun updateSettings(@Body settings: AppSettingsDTO): AppSettingsDTO

    // Auth APIs
    @POST("login/login")
    suspend fun login(@Body body: LoginRequest): BaseResponse<LoginData>

    @POST("/auth/register")
    suspend fun register(@Body body: com.kira.learning.models.RegisterRequest): BaseResponse<LoginData>

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body body: com.kira.learning.models.RefreshTokenRequest): BaseResponse<LoginData>

    @POST("/auth/logout")
    suspend fun logout(): BaseResponse<Unit>

    @GET("/auth/validate")
    suspend fun validateToken(@Query("token") token: String): ValidationResponse

    @POST("/auth/switch-role")
    suspend fun switchRole(@Body body: RoleSwitchRequest): BaseResponse<LoginData>

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body body: RequestBody): BaseResponse<Unit>

    // Code Execution APIs
    @POST("/code/execute")
    suspend fun executeCode(@Body body: RequestBody): BaseResponse<com.kira.learning.modules.coding.CodeExecutionResponse>

    // Main/Home APIs
    @GET("/home/recommended")
    suspend fun getRecommendedContent(): BaseResponse<List<com.kira.learning.modules.main.RecommendedItem>>

    @GET("/user/stats")
    suspend fun getUserLearningStats(): BaseResponse<com.kira.learning.modules.main.LearningStats>

    // Image Player APIs
    @GET("/images/{id}")
    suspend fun getImageDetail(@Path("id") imageId: String): BaseResponse<com.kira.learning.modules.imageplayer.ImageDetail>

    @POST("/images/{id}/favorite")
    suspend fun favoriteImage(@Path("id") imageId: String): BaseResponse<Unit>

    @DELETE("/images/{id}/favorite")
    suspend fun unfavoriteImage(@Path("id") imageId: String): BaseResponse<Unit>

    @GET("/images/favorites")
    suspend fun getFavoriteImages(): BaseResponse<List<PhotoDTO>>

    // OCR APIs
    @Multipart
    @POST("/ocr/recognize")
    suspend fun recognizeText(@Part image: MultipartBody.Part): BaseResponse<com.kira.learning.modules.ocr.OcrResult>

    @Multipart
    @POST("/ocr/recognize/batch")
    suspend fun recognizeMultipleTexts(@Part images: List<MultipartBody.Part>): BaseResponse<List<com.kira.learning.modules.ocr.OcrResult>>

    @GET("/ocr/history")
    suspend fun getOcrHistory(): BaseResponse<List<com.kira.learning.modules.ocr.OcrHistoryItem>>

    @DELETE("/ocr/history/{id}")
    suspend fun deleteOcrHistory(@Path("id") historyId: String): BaseResponse<Unit>

    @POST("/ocr/save")
    suspend fun saveOcrResult(@Body body: RequestBody): BaseResponse<String>

    // UI Demo APIs
    @GET("/ui/demo-data")
    suspend fun getUiDemoData(): BaseResponse<com.kira.learning.modules.uidemo.UiDemoData>

    @POST("/ui/feedback")
    suspend fun submitUiFeedback(@Body body: RequestBody): BaseResponse<Unit>

    @GET("/ui/theme")
    suspend fun getThemeConfig(): BaseResponse<com.kira.learning.modules.uidemo.ThemeConfig>

    @PUT("/ui/theme")
    suspend fun saveThemeConfig(@Body body: RequestBody): BaseResponse<Unit>
}
