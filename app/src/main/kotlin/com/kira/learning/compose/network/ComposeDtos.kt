package com.kira.learning.compose.network

import com.kira.learning.compose.module.profile.AppSettings
import com.kira.learning.compose.module.profile.UserProfile
import com.kira.learning.compose.module.sample.SampleImage

// Sample photos
@DataTransferObject
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

// Profile related DTOs
@DataTransferObject
data class UserProfileDTO(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val updatedAt: Long,
)

@DataTransferObject
data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val bio: String,
)

@DataTransferObject
data class UpdateAvatarRequest(
    val avatarUrl: String,
)

@DataTransferObject
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

// 标记注解（无实际功能，只作为语义标记，可选）
@Target(AnnotationTarget.CLASS)
annotation class DataTransferObject
