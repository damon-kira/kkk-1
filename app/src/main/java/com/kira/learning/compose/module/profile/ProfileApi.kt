package com.kira.learning.compose.module.profile

import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/** 模拟网络层接口 (可替换为 Retrofit) */
interface ProfileApi {
    suspend fun getProfile(): UserProfileDTO
    suspend fun updateProfile(req: UpdateProfileRequest): UserProfileDTO
    suspend fun updateAvatar(url: String): UserProfileDTO
    suspend fun updateSettings(settings: AppSettingsDTO): AppSettingsDTO
}

// DTO 定义
data class UserProfileDTO(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val updatedAt: Long,
)

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val bio: String,
)

data class AppSettingsDTO(
    val darkMode: Boolean,
    val notificationsEnabled: Boolean,
    val autoPlayVideo: Boolean,
    val analyticsEnabled: Boolean,
    val crashReportEnabled: Boolean,
)

fun UserProfileDTO.toDomain() = UserProfile(id, name, email, avatarUrl, bio, updatedAt)
fun AppSettingsDTO.toDomain() = AppSettings(darkMode, notificationsEnabled, autoPlayVideo, analyticsEnabled, crashReportEnabled)
fun AppSettings.toDto() = AppSettingsDTO(darkMode, notificationsEnabled, autoPlayVideo, analyticsEnabled, crashReportEnabled)

@Singleton
class FakeProfileApi @Inject constructor(): ProfileApi {
    private var profile = UserProfileDTO(
        id = "u001",
        name = "演示用户",
        email = "demo@example.com",
        avatarUrl = "https://placekitten.com/200/200",
        bio = "一句话简介",
        updatedAt = System.currentTimeMillis()
    )
    private var settings = AppSettingsDTO(false, true, false, true, true)

    override suspend fun getProfile(): UserProfileDTO {
        delay(300)
        return profile
    }

    override suspend fun updateProfile(req: UpdateProfileRequest): UserProfileDTO {
        delay(400)
        profile = profile.copy(name = req.name, email = req.email, bio = req.bio, updatedAt = System.currentTimeMillis())
        return profile
    }

    override suspend fun updateAvatar(url: String): UserProfileDTO {
        delay(500)
        profile = profile.copy(avatarUrl = url, updatedAt = System.currentTimeMillis())
        return profile
    }

    override suspend fun updateSettings(settings: AppSettingsDTO): AppSettingsDTO {
        delay(250)
        this.settings = settings
        return this.settings
    }
}

