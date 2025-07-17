

package com.kira.ui.feature.themes.domain.model

import com.kira.ui.editorkit.utils.EditorTheme

enum class InternalTheme(val theme: ThemeModel) {
    THEME_DARCULA(
        theme = ThemeModel(
            uuid = "DARCULA",
            name = "Darcula",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.DARCULA,
        ),
    ),
    THEME_ECLIPSE(
        theme = ThemeModel(
            uuid = "ECLIPSE",
            name = "Eclipse",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.ECLIPSE,
        ),
    ),
    THEME_MONOKAI(
        theme = ThemeModel(
            uuid = "MONOKAI",
            name = "Monokai",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.MONOKAI,
        ),
    ),
    THEME_OBSIDIAN(
        theme = ThemeModel(
            uuid = "OBSIDIAN",
            name = "Obsidian",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.OBSIDIAN,
        ),
    ),
    THEME_INTELLIJ_LIGHT(
        theme = ThemeModel(
            uuid = "INTELLIJ_LIGHT",
            name = "IntelliJ Light",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.INTELLIJ_LIGHT,
        ),
    ),
    THEME_LADIES_NIGHT(
        theme = ThemeModel(
            uuid = "LADIES_NIGHT",
            name = "Ladies Night",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.LADIES_NIGHT,
        ),
    ),
    THEME_TOMORROW_NIGHT(
        theme = ThemeModel(
            uuid = "TOMORROW_NIGHT",
            name = "Tomorrow Night",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.TOMORROW_NIGHT,
        ),
    ),
    THEME_SOLARIZED_LIGHT(
        theme = ThemeModel(
            uuid = "SOLARIZED_LIGHT",
            name = "Solarized Light",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.SOLARIZED_LIGHT,
        ),
    ),
    THEME_VISUAL_STUDIO(
        theme = ThemeModel(
            uuid = "VISUAL_STUDIO_2013",
            name = "Visual Studio",
            author = "Squircle CE",
            description = "Default color scheme",
            isExternal = false,
            colorScheme = EditorTheme.VISUAL_STUDIO,
        ),
    );

    companion object {

        fun find(id: String): ThemeModel? {
            return values().find { it.theme.uuid == id }?.theme
        }
    }
}