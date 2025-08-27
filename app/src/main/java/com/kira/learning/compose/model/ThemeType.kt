package com.kira.learning.compose.model

enum class ThemeType(val value: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {

        fun of(value: String): ThemeType {
            return entries.find { it.value == value } ?: DARK
        }
    }
}