package com.kira.ui.feature.explorer.domain.repository

import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.FileTree
import com.kira.ui.feature.explorer.domain.model.FilesystemModel

interface ExplorerRepository {

    suspend fun loadFilesystems(): List<FilesystemModel>
    suspend fun selectFilesystem(filesystemUuid: String)

    suspend fun listFiles(parent: FileModel?): FileTree

    suspend fun createFile(fileModel: FileModel)
    suspend fun renameFile(source: FileModel, dest: FileModel)
    suspend fun deleteFiles(source: List<FileModel>)

    suspend fun copyFiles(source: List<FileModel>, dest: FileModel)
    suspend fun cutFiles(source: List<FileModel>, dest: FileModel)

    suspend fun compressFiles(source: List<FileModel>, dest: FileModel)
    suspend fun extractFiles(source: FileModel, dest: FileModel)
}