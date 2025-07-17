

package com.kira.ui.filesystem.base.model

data class FileTree(
    val parent: FileModel,
    val children: List<FileModel>,
)