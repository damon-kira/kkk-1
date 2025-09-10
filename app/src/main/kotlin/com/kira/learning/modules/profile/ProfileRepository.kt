package com.kira.learning.modules.profile

import com.kira.learning.models.UserProfile
import com.kira.learning.models.AppSettings
import com.kira.learning.network.ApiResult
import com.kira.learning.network.ComposeApiService
import com.kira.learning.models.dao.toDomain
import com.kira.learning.models.dao.UpdateProfileRequest
import com.kira.learning.models.dao.UpdateAvatarRequest
import com.kira.learning.models.dao.toDto
import com.kira.learning.base.repository.BaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ComposeApiService
) : BaseRepository() {
    // 是否启用本地模拟（后端未就绪时开启）
    private val useMock = true

    // 模拟数据存储（简单内存实现，可换成 DataStore / Room）
    @Volatile private var mockProfile: UserProfile = UserProfile(
        id = "mock-user-001",
        name = "演示用户",
        email = "demo@example.com",
        avatarUrl = "https://placekitten.com/200/200",
        bio = "这是一段本地模拟简介 (mock)",
        updatedAt = System.currentTimeMillis()
    )
    @Volatile private var mockSettings: AppSettings = AppSettings(
        darkMode = false,
        notificationsEnabled = true,
        autoPlayVideo = false,
        analyticsEnabled = true,
        crashReportEnabled = true
    )

    suspend fun load(): ApiResult<UserProfile> = if (useMock) {
        ApiResult.Success(mockProfile.copy())
    } else {
        safeApiCall { api.getProfile().toDomain() }
    }

    suspend fun update(name: String, email: String, bio: String): ApiResult<UserProfile> = if (useMock) {
        mockProfile = mockProfile.copy(
            name = name,
            email = email,
            bio = bio,
            updatedAt = System.currentTimeMillis()
        )
        ApiResult.Success(mockProfile.copy())
    } else {
        safeApiCall {
            api.updateProfile(UpdateProfileRequest(name, email, bio)).toDomain()
        }
    }

    suspend fun updateAvatar(avatarUrl: String): ApiResult<UserProfile> = if (useMock) {
        mockProfile = mockProfile.copy(
            avatarUrl = avatarUrl,
            updatedAt = System.currentTimeMillis()
        )
        ApiResult.Success(mockProfile.copy())
    } else {
        safeApiCall {
            api.updateAvatar(UpdateAvatarRequest(avatarUrl)).toDomain()
        }
    }

    suspend fun updateSettings(settings: AppSettings): ApiResult<Unit> = if (useMock) {
        mockSettings = settings
        ApiResult.Success(Unit)
    } else {
        safeApiCall {
            api.updateSettings(settings.toDto())
        }
    }

    fun getSettings(): AppSettings = mockSettings.copy()
}
