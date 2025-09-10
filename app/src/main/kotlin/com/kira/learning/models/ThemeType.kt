package com.kira.learning.models

enum class ThemeType(val value: String) {
    LIGHT("light"),
    DARK("dark");

    companion object {

        fun of(value: String): ThemeType {
            return entries.find { it.value == value } ?: DARK
        }
    }
}