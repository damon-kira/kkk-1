

package com.kira.ui.feature.editor.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.feature.editor.domain.model.DocumentContent

sealed class EditorViewState : ViewState() {

    data object Loading : EditorViewState()

    data class Content(var content: DocumentContent) : EditorViewState()

    data class Error(
        val image: Int,
        val title: String,
        val subtitle: String,
        val action: EditorErrorAction,
    ) : EditorViewState()
}