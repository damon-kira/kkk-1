package com.kira.learning.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("preferredLocale") val preferredLocale: String = "en-US"
)

data class LoginData(
    @SerializedName("token") val token: String,
    @SerializedName("currentRole") val currentRole: String,
    @SerializedName("prove") val prove: String,
    @SerializedName("loginDate") val loginDate: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("currentVersion") val currentVersion: String,
    @SerializedName("preferredLocale") val preferredLocale: String
) {
    // 解析用户角色
    val userRoles: List<UserRole>
        get() = when (currentRole) {
            "1" -> listOf(UserRole.TEACHER, UserRole.STUDENT)
            "0" -> listOf(UserRole.STUDENT)
            else -> listOf(UserRole.STUDENT)
        }

    // 当前主要角色
    val primaryRole: UserRole
        get() = when (currentRole) {
            "1" -> UserRole.TEACHER
            "0" -> UserRole.STUDENT
            else -> UserRole.STUDENT
        }

    // 检查是否有特定角色权限
    fun hasRole(role: UserRole): Boolean = userRoles.contains(role)

    // 检查token是否可能过期（简单检查）
    val isTokenValid: Boolean
        get() = token.isNotEmpty() && !token.contains("expired")
}

// 用户角色枚举
enum class UserRole(val code: String, val displayName: String) {
    TEACHER("ROLE_TEACHER", "教师"),
    STUDENT("ROLE_STUDENT", "学生");

    companion object {
        fun fromCode(code: String): UserRole? = entries.find { it.code == code }
    }
}

// 刷新token请求
data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)

// 注册请求
data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("email") val email: String,
    @SerializedName("preferredLocale") val preferredLocale: String = "en-US",
    @SerializedName("role") val role: String = "ROLE_STUDENT"
)

// 用户会话信息
data class UserSession(
    val loginData: LoginData,
    val loginTimestamp: Long = System.currentTimeMillis(),
    val isRememberMe: Boolean = false,
    val ipAddress: String = "", // 登录时的IP地址
    val deviceInfo: String = "", // 设备信息
    val userAgent: String = "" // 用户代理信息
) {
    // 会话是否过期（24小时）
    val isExpired: Boolean
        get() = System.currentTimeMillis() - loginTimestamp > 24 * 60 * 60 * 1000

    // 格式化的登录时间
    val lastLoginTime: String
        get() = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            .format(java.util.Date(loginTimestamp))
}

// 验证响应
data class ValidationResponse(
    @SerializedName("isValid") val isValid: Boolean,
    @SerializedName("expiresAt") val expiresAt: Long? = null,
    @SerializedName("message") val message: String? = null
)

// 角色切换请求
data class RoleSwitchRequest(
    @SerializedName("newRole") val newRole: String
)

// 密码重置请求
data class PasswordResetRequest(
    @SerializedName("email") val email: String,
    @SerializedName("newPassword") val newPassword: String? = null,
    @SerializedName("resetToken") val resetToken: String? = null
)
