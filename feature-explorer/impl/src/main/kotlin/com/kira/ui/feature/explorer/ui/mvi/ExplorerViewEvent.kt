package com.kira.ui.feature.explorer.ui.mvi

import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.filesystem.base.model.FileModel

sealed class ExplorerViewEvent : ViewEvent() {

    data class OpenFile(val fileModel: FileModel) : ExplorerViewEvent()
    data class OpenFileWith(val fileModel: FileModel) : ExplorerViewEvent()
    data class CopyPath(val fileModel: FileModel) : ExplorerViewEvent()
    data object SelectAll : ExplorerViewEvent()
}