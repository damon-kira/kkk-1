

package com.kira.ui.feature.explorer.ui.mvi

import com.kira.ui.core.mvi.ViewState
import com.kira.ui.filesystem.base.model.FileModel

sealed class ExplorerViewState : ViewState() {

    data object Loading : ExplorerViewState()

    data class Files(
        val data: List<FileModel>,
    ) : ExplorerViewState()

    data class Error(
        val image: Int,
        val title: String,
        val subtitle: String,
        val action: ExplorerErrorAction,
    ) : ExplorerViewState()
}