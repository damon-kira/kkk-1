package com.kira.ui.feature.explorer.data.repository

import android.content.Context
import com.kira.ui.core.extensions.checkStorageAccess
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.explorer.R
import com.kira.ui.feature.explorer.data.utils.fileComparator
import com.kira.ui.feature.explorer.domain.factory.FilesystemFactory
import com.kira.ui.feature.explorer.domain.model.FilesystemModel
import com.kira.ui.feature.explorer.domain.repository.ExplorerRepository
import com.kira.ui.feature.explorer.ui.worker.*
import com.kira.ui.filesystem.base.exception.PermissionException
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.FileTree
import com.kira.ui.filesystem.local.utils.LocalFilesystem
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ExplorerRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val settingsManager: SettingsManager,
    private val filesystemFactory: FilesystemFactory,
    private val context: Context,
) : ExplorerRepository {

    private var currentFilesystem = settingsManager.filesystem

    override suspend fun loadFilesystems(): List<FilesystemModel> {
        return withContext(dispatcherProvider.io()) {
            listOf(
                FilesystemModel(
                    uuid = LocalFilesystem.LOCAL_UUID,
                    title = context.getString(R.string.storage_local),
                ),
//                FilesystemModel(
//                    uuid = RootFilesystem.ROOT_UUID,
//                    title = context.getString(R.string.storage_root),
//                ),
            )
        }
    }

    override suspend fun selectFilesystem(filesystemUuid: String) {
        withContext(dispatcherProvider.io()) {
            settingsManager.filesystem = filesystemUuid
            currentFilesystem = filesystemUuid
        }
    }

    override suspend fun listFiles(parent: FileModel?): FileTree {
        return withContext(dispatcherProvider.io()) {
            suspendCoroutine { cont ->
                context.checkStorageAccess(
                    onSuccess = { cont.resume(Unit) },
                    onFailure = { cont.resumeWithException(PermissionException()) },
                )
            }
            val filesystem = filesystemFactory.create(currentFilesystem)
            val fileTree = filesystem.provideDirectory(parent ?: filesystem.defaultLocation())
            fileTree.copy(
                children = fileTree.children
                    .filter { if (it.isHidden) settingsManager.showHidden else true }
                    .sortedWith(fileComparator(settingsManager.sortMode.toInt()))
                    .sortedBy { it.directory != settingsManager.foldersOnTop },
            )
        }
    }

    override suspend fun createFile(fileModel: FileModel) {
        withContext(dispatcherProvider.io()) {
            context.checkStorageAccess(
                onSuccess = { CreateFileWorker.scheduleJob(context, listOf(fileModel)) },
                onFailure = { throw PermissionException() },
            )
        }
    }

    override suspend fun renameFile(source: FileModel, dest: FileModel) {
        withContext(dispatcherProvider.io()) {
            context.checkStorageAccess(
                onSuccess = { RenameFileWorker.scheduleJob(context, listOf(source, dest)) },
                onFailure = { throw PermissionException() },
            )
        }
    }

    override suspend fun deleteFiles(source: List<FileModel>) {
        context.checkStorageAccess(
            onSuccess = { DeleteFileWorker.scheduleJob(context, source) },
            onFailure = { throw PermissionException() },
        )
    }

    override suspend fun copyFiles(source: List<FileModel>, dest: FileModel) {
        context.checkStorageAccess(
            onSuccess = { CopyFileWorker.scheduleJob(context, source + dest) },
            onFailure = { throw PermissionException() },
        )
    }

    override suspend fun cutFiles(source: List<FileModel>, dest: FileModel) {
        context.checkStorageAccess(
            onSuccess = { CutFileWorker.scheduleJob(context, source + dest) },
            onFailure = { throw PermissionException() },
        )
    }

    override suspend fun compressFiles(source: List<FileModel>, dest: FileModel) {
        context.checkStorageAccess(
            onSuccess = { CompressFileWorker.scheduleJob(context, source + dest) },
            onFailure = { throw PermissionException() },
        )
    }

    override suspend fun extractFiles(source: FileModel, dest: FileModel) {
        context.checkStorageAccess(
            onSuccess = { ExtractFileWorker.scheduleJob(context, listOf(source, dest)) },
            onFailure = { throw PermissionException() },
        )
    }
}