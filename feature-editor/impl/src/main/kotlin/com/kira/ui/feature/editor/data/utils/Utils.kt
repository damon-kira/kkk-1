package com.kira.ui.feature.editor.data.utils

import com.kira.ui.filesystem.base.model.FileModel

fun createFile(fileName: String): FileModel {
    return FileModel(
        fileUri = "file:///storage/emulated/0/$fileName",
        filesystemUuid = "local",
        directory = false,
    )
}

fun createFolder(fileName: String): FileModel {
    return FileModel(
        fileUri = "file:///storage/emulated/0/$fileName",
        filesystemUuid = "local",
        directory = true,
    )
}