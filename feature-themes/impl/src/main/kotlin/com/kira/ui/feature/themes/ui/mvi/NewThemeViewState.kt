

package com.kira.ui.feature.themes.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.feature.themes.domain.model.Meta
import com.kira.ui.feature.themes.domain.model.PropertyItem

sealed class NewThemeViewState : ViewState() {

    data class MetaData(
        val meta: Meta,
        val properties: List<PropertyItem>,
    ) : NewThemeViewState()
}