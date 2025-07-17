package com.kira.ui.feature.explorer.ui.mvi

import com.kira.ui.core.mvi.ViewIntent
import com.kira.ui.filesystem.base.model.FileModel

sealed class ExplorerIntent : ViewIntent() {

    data class SearchFiles(val query: String) : ExplorerIntent()
    data class SelectFiles(val selection: List<FileModel>) : ExplorerIntent()
    data class SelectTab(val position: Int) : ExplorerIntent()
    data class SelectFilesystem(val filesystemUuid: String) : ExplorerIntent()
    data class Authenticate(val password: String) : ExplorerIntent()
    data object Refresh : ExplorerIntent()

    data object Cut : ExplorerIntent()
    data object Copy : ExplorerIntent()
    data object Create : ExplorerIntent()
    data object Rename : ExplorerIntent()
    data object Delete : ExplorerIntent()
    data object SelectAll : ExplorerIntent()
    data object UnselectAll : ExplorerIntent()
    data object Properties : ExplorerIntent()
    data object CopyPath : ExplorerIntent()
    data object Compress : ExplorerIntent()

    data class OpenFolder(val fileModel: FileModel? = null) : ExplorerIntent()
    data class OpenFileWith(val fileModel: FileModel? = null) : ExplorerIntent()
    data class OpenFile(val fileModel: FileModel) : ExplorerIntent()
    data class CreateFile(val fileName: String, val directory: Boolean) : ExplorerIntent()
    data class RenameFile(val fileName: String) : ExplorerIntent()
    data class CompressFile(val fileName: String) : ExplorerIntent()
    data class ExtractFile(val fileModel: FileModel) : ExplorerIntent()
    data object DeleteFile : ExplorerIntent()
    data object CutFile : ExplorerIntent()
    data object CopyFile : ExplorerIntent()

    data object ShowHidden : ExplorerIntent()
    data object HideHidden : ExplorerIntent()
    data object SortByName : ExplorerIntent()
    data object SortBySize : ExplorerIntent()
    data object SortByDate : ExplorerIntent()
}