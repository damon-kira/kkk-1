package com.kira.ui.filesystem.ftps

import com.kira.ui.filesystem.base.Filesystem
import com.kira.ui.filesystem.base.exception.AuthenticationException
import com.kira.ui.filesystem.base.exception.ConnectionException
import com.kira.ui.filesystem.base.exception.FileNotFoundException
import com.kira.ui.filesystem.base.model.*
import com.kira.ui.filesystem.base.utils.isValidFileName
import com.kira.ui.filesystem.base.utils.plusFlag
import kotlinx.coroutines.flow.Flow
import org.apache.commons.net.ftp.FTPFile
import org.apache.commons.net.ftp.FTPReply
import org.apache.commons.net.ftp.FTPSClient
import java.io.File
import java.util.*

class FTPSFilesystem(
    private val serverConfig: ServerConfig,
    private val cacheLocation: File,
) : Filesystem {

    private val ftpsClient = FTPSClient(true)
    private val ftpsMapper = FTPSMapper()

    init {
        ftpsClient.connectTimeout = 10000
    }

    override fun defaultLocation(): FileModel {
        return FileModel(FTPS_SCHEME + serverConfig.initialDir, serverConfig.uuid)
    }

    override fun provideDirectory(parent: FileModel): FileTree {
        try {
            connect()
            ftpsClient.changeWorkingDirectory(parent.path)
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(parent.path)
            }
            return FileTree(
                parent = ftpsMapper.parent(parent),
                children = ftpsClient.listFiles(parent.path)
                    .filter { it.name.isValidFileName() }
                    .map(ftpsMapper::toFileModel),
            )
        } finally {
            disconnect()
        }
    }

    override fun exists(fileModel: FileModel): Boolean {
        throw UnsupportedOperationException()
    }

    override fun createFile(fileModel: FileModel) {
        try {
            connect()
            if (fileModel.directory) {
                ftpsClient.makeDirectory(fileModel.path)
            } else {
                ftpsClient.storeFile(fileModel.path, "".byteInputStream())
            }
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(fileModel.path)
            }
        } finally {
            disconnect()
        }
    }

    override fun renameFile(source: FileModel, dest: FileModel) {
        try {
            connect()
            ftpsClient.rename(source.path, dest.path)
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(source.path)
            }
        } finally {
            disconnect()
        }
    }

    override fun deleteFile(fileModel: FileModel) {
        try {
            connect()
            if (fileModel.directory) {
                ftpsClient.removeDirectory(fileModel.path)
            } else {
                ftpsClient.deleteFile(fileModel.path)
            }
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(fileModel.path)
            }
        } finally {
            disconnect()
        }
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
        val tempFile = File(cacheLocation, UUID.randomUUID().toString())
        try {
            connect()

            tempFile.createNewFile()
            tempFile.outputStream().use {
                ftpsClient.retrieveFile(fileModel.path, it)
            }
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(fileModel.path)
            }
            return tempFile.readText(fileParams.charset)
        } finally {
            tempFile.deleteRecursively()
            disconnect()
        }
    }

    override fun saveFile(fileModel: FileModel, text: String, fileParams: FileParams) {
        val tempFile = File(cacheLocation, UUID.randomUUID().toString())
        try {
            connect()

            tempFile.createNewFile()
            tempFile.writeText(text, fileParams.charset)
            tempFile.inputStream().use {
                ftpsClient.storeFile(fileModel.path, it)
            }
            if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
                throw FileNotFoundException(fileModel.path)
            }
        } finally {
            tempFile.deleteRecursively()
            disconnect()
        }
    }

    private fun connect() {
        if (serverConfig.password == null) {
            throw AuthenticationException(AuthMethod.PASSWORD, false)
        }
        if (ftpsClient.isConnected) {
            return
        }
        ftpsClient.connect(serverConfig.address, serverConfig.port)
        if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
            throw ConnectionException()
        }
        if (serverConfig.authMethod != AuthMethod.PASSWORD) {
            throw UnsupportedOperationException()
        }
        ftpsClient.enterLocalPassiveMode()
        ftpsClient.login(serverConfig.username, serverConfig.password)
        if (!FTPReply.isPositiveCompletion(ftpsClient.replyCode)) {
            throw AuthenticationException(AuthMethod.PASSWORD, true)
        }
    }

    private fun disconnect() {
        if (ftpsClient.isConnected) {
            ftpsClient.logout()
            ftpsClient.disconnect()
        }
    }

    inner class FTPSMapper : Filesystem.Mapper<FTPFile> {

        private var parent: FileModel? = null

        override fun toFileModel(fileObject: FTPFile): FileModel {
            return FileModel(
                fileUri = parent?.fileUri + "/" + fileObject.name,
                filesystemUuid = serverConfig.uuid,
                size = fileObject.size,
                lastModified = fileObject.timestamp.timeInMillis,
                directory = fileObject.isDirectory,
                permission = with(fileObject) {
                    var permission = Permission.EMPTY
                    if (hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION)) {
                        permission = permission plusFlag Permission.OWNER_READ
                    }
                    if (hasPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION)) {
                        permission = permission plusFlag Permission.OWNER_WRITE
                    }
                    if (hasPermission(FTPFile.USER_ACCESS, FTPFile.EXECUTE_PERMISSION)) {
                        permission = permission plusFlag Permission.OWNER_EXECUTE
                    }
                    if (hasPermission(FTPFile.GROUP_ACCESS, FTPFile.READ_PERMISSION)) {
                        permission = permission plusFlag Permission.GROUP_READ
                    }
                    if (hasPermission(FTPFile.GROUP_ACCESS, FTPFile.WRITE_PERMISSION)) {
                        permission = permission plusFlag Permission.GROUP_WRITE
                    }
                    if (hasPermission(FTPFile.GROUP_ACCESS, FTPFile.EXECUTE_PERMISSION)) {
                        permission = permission plusFlag Permission.GROUP_EXECUTE
                    }
                    if (hasPermission(FTPFile.WORLD_ACCESS, FTPFile.READ_PERMISSION)) {
                        permission = permission plusFlag Permission.OTHERS_READ
                    }
                    if (hasPermission(FTPFile.WORLD_ACCESS, FTPFile.WRITE_PERMISSION)) {
                        permission = permission plusFlag Permission.OTHERS_WRITE
                    }
                    if (hasPermission(FTPFile.WORLD_ACCESS, FTPFile.EXECUTE_PERMISSION)) {
                        permission = permission plusFlag Permission.OTHERS_EXECUTE
                    }
                    permission
                },
            )
        }

        override fun toFileObject(fileModel: FileModel): FTPFile {
            throw UnsupportedOperationException()
        }

        fun parent(parent: FileModel): FileModel {
            this.parent = parent
            return parent
        }
    }

    companion object {
        const val FTPS_SCHEME = "ftps://"
    }
}