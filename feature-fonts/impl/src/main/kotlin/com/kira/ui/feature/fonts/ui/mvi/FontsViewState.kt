

package com.kira.ui.feature.fonts.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.feature.fonts.domain.model.FontModel

sealed class FontsViewState : ViewState() {

    abstract val query: String

    data object Loading : FontsViewState() {
        override val query: String = ""
    }

    data class Empty(
        override val query: String,
    ) : FontsViewState()

    data class Data(
        override val query: String,
        val fonts: List<FontModel>,
    ) : FontsViewState()
}