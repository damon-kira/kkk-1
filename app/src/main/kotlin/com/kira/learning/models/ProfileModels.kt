package com.kira.learning.models

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val avatarUrl: String,
    val bio: String,
    val updatedAt: Long,
)

@Immutable
data class AppSettings(
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val autoPlayVideo: Boolean = false,
    val analyticsEnabled: Boolean = true,
    val crashReportEnabled: Boolean = true,
)

sealed interface ProfileUiState {
    object Loading: ProfileUiState
    data class Data(
        val profile: UserProfile,
        val editing: Boolean = false,
        val editName: String = profile.name,
        val editEmail: String = profile.email,
        val editBio: String = profile.bio,
        val saving: Boolean = false,
        val settings: AppSettings = AppSettings(),
        val message: String? = null,
    ): ProfileUiState
    data class Error(val message: String): ProfileUiState
}

