

package com.kira.ui.feature.servers.domain.repository

import com.kira.ui.filesystem.base.model.ServerConfig
import kotlinx.coroutines.flow.Flow

interface ServersRepository {

    val serverFlow: Flow<List<ServerConfig>>

    suspend fun authenticate(uuid: String, password: String)

    suspend fun loadServers(): List<ServerConfig>
    suspend fun loadServer(uuid: String): ServerConfig
    suspend fun upsertServer(serverConfig: ServerConfig)
    suspend fun deleteServer(serverConfig: ServerConfig)
}