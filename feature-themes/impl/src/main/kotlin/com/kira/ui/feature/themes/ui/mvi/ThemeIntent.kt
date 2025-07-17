

package com.kira.ui.feature.themes.ui.mvi

import android.net.Uri
import com.kira.ui.core.mvi.ViewIntent
import com.kira.ui.feature.themes.domain.model.Meta
import com.kira.ui.feature.themes.domain.model.Property
import com.kira.ui.feature.themes.domain.model.PropertyItem
import com.kira.ui.feature.themes.domain.model.ThemeModel

sealed class ThemeIntent : ViewIntent() {

    data object LoadThemes : ThemeIntent()

    data class SearchThemes(val query: String) : ThemeIntent()
    data class ImportTheme(val fileUri: Uri) : ThemeIntent()
    data class ExportTheme(val themeModel: ThemeModel, val fileUri: Uri) : ThemeIntent()
    data class SelectTheme(val themeModel: ThemeModel) : ThemeIntent()
    data class RemoveTheme(val themeModel: ThemeModel) : ThemeIntent()

    data class LoadProperties(val uuid: String?) : ThemeIntent()
    data class CreateTheme(val meta: Meta, val properties: List<PropertyItem>) : ThemeIntent()
    data class ChooseColor(val key: Property, val value: String) : ThemeIntent()

    data class ChangeName(val value: String) : ThemeIntent()
    data class ChangeAuthor(val value: String) : ThemeIntent()
    data class ChangeDescription(val value: String) : ThemeIntent()
    data class ChangeColor(val key: String, val value: String) : ThemeIntent()
}