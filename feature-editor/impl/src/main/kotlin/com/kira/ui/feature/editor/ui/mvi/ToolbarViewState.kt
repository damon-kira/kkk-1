

package com.kira.ui.feature.editor.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.editorkit.model.FindParams
import com.kira.ui.feature.editor.domain.model.DocumentModel
import com.kira.ui.feature.editor.ui.manager.ToolbarManager

sealed class ToolbarViewState : ViewState() {

    data class ActionBar(
        val documents: List<DocumentModel> = emptyList(),
        val position: Int = -1,
        val mode: ToolbarManager.Mode = ToolbarManager.Mode.DEFAULT,
        val findParams: FindParams = FindParams()
    ) : ToolbarViewState()
}