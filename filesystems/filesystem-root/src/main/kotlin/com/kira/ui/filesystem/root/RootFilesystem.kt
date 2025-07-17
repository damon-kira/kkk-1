package com.kira.ui.filesystem.root

import com.ibm.icu.text.CharsetDetector
import com.kira.ui.filesystem.base.Filesystem
import com.kira.ui.filesystem.base.exception.DirectoryExpectedException
import com.kira.ui.filesystem.base.exception.FileAlreadyExistsException
import com.kira.ui.filesystem.base.exception.FileNotFoundException
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.filesystem.base.model.FileParams
import com.kira.ui.filesystem.base.model.FileTree
import com.kira.ui.filesystem.base.model.Permission
import com.kira.ui.filesystem.base.utils.plusFlag
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.io.SuFile
import kotlinx.coroutines.flow.Flow
import java.io.BufferedReader

class RootFilesystem : Filesystem {

    init {
        requestRootAccess()
    }

    override fun defaultLocation(): FileModel {
        val defaultLocation = SuFile("/")
        val fileModel = toFileModel(defaultLocation)
        if (!defaultLocation.isDirectory) {
            throw DirectoryExpectedException()
        }
        return fileModel
    }

    override fun provideDirectory(parent: FileModel): FileTree {
        val file = toFileObject(parent)
        if (!file.isDirectory) {
            throw DirectoryExpectedException()
        }
        return FileTree(
            parent = parent,
            children = file.listFiles().orEmpty()
                .map(::toFileModel).toList(),
        )
    }

    override fun exists(fileModel: FileModel): Boolean {
        return SuFile(fileModel.path).exists()
    }

    override fun createFile(fileModel: FileModel) {
        val file = toFileObject(fileModel)
        if (file.exists()) {
            throw FileAlreadyExistsException(fileModel.path)
        }
        if (fileModel.directory) {
            file.mkdirs()
        } else {
            val parentFile = file.parentFile!!
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            file.createNewFile()
        }
    }

    override fun renameFile(source: FileModel, dest: FileModel) {
        val originalFile = toFileObject(source)
        val renamedFile = toFileObject(dest)
        if (!originalFile.exists()) {
            throw FileNotFoundException(source.path)
        }
        if (renamedFile.exists()) {
            throw FileAlreadyExistsException(renamedFile.absolutePath)
        }
        originalFile.renameTo(renamedFile)
    }

    override fun deleteFile(fileModel: FileModel) {
        val file = toFileObject(fileModel)
        if (!file.exists()) {
            throw FileNotFoundException(fileModel.path)
        }
        file.deleteRecursive()
    }

    override fun copyFile(source: FileModel, dest: FileModel) {
        throw UnsupportedOperationException()
    }

    override fun compressFiles(source: List<FileModel>, dest: FileModel): Flow<FileModel> {
        throw UnsupportedOperationException()
    }

    override fun extractFiles(source: FileModel, dest: FileModel): Flow<FileModel> {
        throw UnsupportedOperationException()
    }

    override fun loadFile(fileModel: FileModel, fileParams: FileParams): String {
        val file = toFileObject(fileModel)
        if (!file.exists()) {
            throw FileNotFoundException(fileModel.path)
        }
        val charset = if (fileParams.chardet) {
            try {
                val charsetMatch = CharsetDetector()
                    .setText(file.newInputStream().readBytes())
                    .detect()
                charset(charsetMatch.name)
            } catch (e: Exception) {
                Charsets.UTF_8
            }
        } else {
            fileParams.charset
        }
        return file.newInputStream().bufferedReader(charset)
            .use(BufferedReader::readText)
    }

    override fun saveFile(fileModel: FileModel, text: String, fileParams: FileParams) {
        val file = toFileObject(fileModel)
        if (!file.exists()) {
            val parentFile = file.parentFile!!
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            file.createNewFile()
        }
        file.newOutputStream().bufferedWriter(fileParams.charset)
            .use { it.write(fileParams.linebreak.replace(text)) }
    }

    companion object : Filesystem.Mapper<SuFile> {

        const val ROOT_UUID = "root"
        const val ROOT_SCHEME = "sufile://"

        init {
            Shell.enableVerboseLogging = BuildConfig.DEBUG
            Shell.setDefaultBuilder(
                Shell.Builder.create()
                    .setFlags(Shell.FLAG_MOUNT_MASTER or Shell.FLAG_REDIRECT_STDERR),
            )
        }

        override fun toFileModel(fileObject: SuFile): FileModel {
            return FileModel(
                fileUri = ROOT_SCHEME + fileObject.path,
                filesystemUuid = ROOT_UUID,
                size = fileObject.length(),
                lastModified = fileObject.lastModified(),
                directory = fileObject.isDirectory,
                permission = with(fileObject) {
                    var permission = Permission.EMPTY
                    if (fileObject.canRead()) {
                        permission = permission plusFlag Permission.OWNER_READ
                    }
                    if (fileObject.canWrite()) {
                        permission = permission plusFlag Permission.OWNER_WRITE
                    }
                    if (fileObject.canExecute()) {
                        permission = permission plusFlag Permission.OWNER_EXECUTE
                    }
                    permission
                },
            )
        }

        override fun toFileObject(fileModel: FileModel): SuFile {
            return SuFile(fileModel.path)
        }
    }
}