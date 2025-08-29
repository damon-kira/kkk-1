package com.kira.learning.compose.module.profile

import com.kira.learning.compose.network.ApiResult
import com.kira.learning.compose.network.safeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: ProfileApi
) {
    suspend fun load(): ApiResult<UserProfile> = safeApiCall { api.getProfile().toDomain() }
    suspend fun update(name: String, email: String, bio: String): ApiResult<UserProfile> = safeApiCall { api.updateProfile(UpdateProfileRequest(name, email, bio)).toDomain() }
    suspend fun updateAvatar(url: String): ApiResult<UserProfile> = safeApiCall { api.updateAvatar(url).toDomain() }
    suspend fun updateSettings(settings: AppSettings): ApiResult<AppSettings> = safeApiCall { api.updateSettings(settings.toDto()).toDomain() }
}
