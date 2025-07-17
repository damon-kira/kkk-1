

package com.kira.ui.feature.themes.domain.model

import com.kira.ui.editorkit.model.ColorScheme

data class ThemeModel(
    val uuid: String,
    val name: String,
    val author: String,
    val description: String,
    val isExternal: Boolean,
    val colorScheme: ColorScheme,
)