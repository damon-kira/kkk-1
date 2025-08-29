package com.kira.learning.compose.model

data class ColorScheme(
    val type: ThemeType,
    val colorPrimary: Int?,
    val colorOutline: Int?,
    val colorBackgroundPrimary: Int?,
    val colorBackgroundSecondary: Int?,
    val colorBackgroundTertiary: Int?,
    val colorTextAndIconPrimary: Int?,
    val colorTextAndIconPrimaryInverse: Int?,
    val colorTextAndIconSecondary: Int?,
    val colorTextAndIconDisabled: Int?,
    val colorTextAndIconAdditional: Int?,
    val colorTextAndIconSuccess: Int?,
    val colorTextAndIconError: Int?,
)