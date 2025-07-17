

package com.kira.ui.feature.themes.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.feature.themes.domain.model.ThemeModel

sealed class ThemesViewState : ViewState() {

    abstract val query: String

    data object Loading : ThemesViewState() {
        override val query: String = ""
    }

    data class Empty(
        override val query: String,
    ) : ThemesViewState()

    data class Data(
        override val query: String,
        val themes: List<ThemeModel>,
    ) : ThemesViewState()
}