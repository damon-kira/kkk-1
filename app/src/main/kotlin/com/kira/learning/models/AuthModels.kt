package com.kira.learning.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("preferredLocale") val preferredLocale: String = "en-US"
)

data class LoginData(
    val loginDate: String?,
    val token: String?,
    val currentVersion: String?,
    val refreshToken: String?,
    val currentRole: String?,
    val prove: String?,
    val preferredLocale: String?
)

