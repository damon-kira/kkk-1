package com.kira.ui.feature.explorer.data.factory

import android.os.Environment
import com.kira.ui.feature.explorer.domain.factory.FilesystemFactory
import com.kira.ui.feature.servers.domain.repository.ServersRepository
import com.kira.ui.filesystem.base.Filesystem
import com.kira.ui.filesystem.ftp.FTPFilesystem
import com.kira.ui.filesystem.ftpes.FTPESFilesystem
import com.kira.ui.filesystem.ftps.FTPSFilesystem
import com.kira.ui.filesystem.local.utils.LocalFilesystem
import com.kira.ui.filesystem.sftp.SFTPFilesystem
import java.io.File

class FilesystemFactoryImpl(
    private val serversRepository: ServersRepository,
    private val cacheDirectory: File,
) : FilesystemFactory {

    override suspend fun create(uuid: String): Filesystem {
        return when (uuid) {
            LocalFilesystem.LOCAL_UUID -> LocalFilesystem(Environment.getExternalStorageDirectory())
//            RootFilesystem.ROOT_UUID -> RootFilesystem()
            else -> {
                val serverConfig = serversRepository.loadServer(uuid)
                return when (serverConfig.scheme) {
                    FTPFilesystem.FTP_SCHEME -> FTPFilesystem(serverConfig, cacheDirectory)
                    FTPSFilesystem.FTPS_SCHEME -> FTPSFilesystem(serverConfig, cacheDirectory)
                    FTPESFilesystem.FTPES_SCHEME -> FTPESFilesystem(serverConfig, cacheDirectory)
                    SFTPFilesystem.SFTP_SCHEME -> SFTPFilesystem(serverConfig, cacheDirectory)
                    else -> throw IllegalArgumentException("Unsupported file scheme")
                }
            }
        }
    }
}