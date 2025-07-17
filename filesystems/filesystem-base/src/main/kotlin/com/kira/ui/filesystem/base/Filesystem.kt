

package com.kira.ui.filesystem.base

import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.FileParams
import com.kira.ui.filesystem.base.model.FileTree
import kotlinx.coroutines.flow.Flow

interface Filesystem {

    fun defaultLocation(): FileModel
    fun provideDirectory(parent: FileModel): FileTree
    fun exists(fileModel: FileModel): Boolean

    fun createFile(fileModel: FileModel)
    fun renameFile(source: FileModel, dest: FileModel)
    fun deleteFile(fileModel: FileModel)
    fun copyFile(source: FileModel, dest: FileModel)

    fun compressFiles(source: List<FileModel>, dest: FileModel): Flow<FileModel>
    fun extractFiles(source: FileModel, dest: FileModel): Flow<FileModel>

    fun loadFile(fileModel: FileModel, fileParams: FileParams): String
    fun saveFile(fileModel: FileModel, text: String, fileParams: FileParams)

    interface Mapper<T> {
        fun toFileModel(fileObject: T): FileModel
        fun toFileObject(fileModel: FileModel): T
    }
}