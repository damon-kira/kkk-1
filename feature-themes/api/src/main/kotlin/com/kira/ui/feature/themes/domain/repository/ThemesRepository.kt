

package com.kira.ui.feature.themes.domain.repository

import android.net.Uri
import com.kira.ui.feature.themes.domain.model.Meta
import com.kira.ui.feature.themes.domain.model.PropertyItem
import com.kira.ui.feature.themes.domain.model.ThemeModel

interface ThemesRepository {

    suspend fun current(): ThemeModel
    suspend fun loadThemes(): List<ThemeModel>
    suspend fun loadThemes(query: String): List<ThemeModel>
    suspend fun loadTheme(uuid: String): ThemeModel

    suspend fun importTheme(fileUri: Uri): ThemeModel
    suspend fun exportTheme(themeModel: ThemeModel, fileUri: Uri)

    suspend fun createTheme(meta: Meta, properties: List<PropertyItem>)
    suspend fun removeTheme(themeModel: ThemeModel)
    suspend fun selectTheme(themeModel: ThemeModel)
}