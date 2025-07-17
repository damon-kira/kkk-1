package com.kira.ui.feature.explorer.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.feature.explorer.data.utils.Operation
import com.kira.ui.filesystem.base.model.FileModel

sealed class ToolbarViewState : ViewState() {

    data class ActionBar(
        val breadcrumbs: List<FileModel> = emptyList(),
        val selection: List<FileModel> = emptyList(),
        val operation: Operation = Operation.CREATE,
    ) : ToolbarViewState()
}