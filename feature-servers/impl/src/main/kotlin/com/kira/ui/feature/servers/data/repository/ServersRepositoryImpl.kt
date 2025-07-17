

package com.kira.ui.feature.servers.data.repository

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.feature.servers.data.converter.ServerConverter
import com.kira.ui.feature.servers.domain.repository.ServersRepository
import com.kira.ui.filesystem.base.model.AuthMethod
import com.kira.ui.filesystem.base.model.ServerConfig
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ServersRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val appDatabase: AppDatabase,
) : ServersRepository {

    override val serverFlow = appDatabase.serverDao().flow()
        .map { it.map(ServerConverter::toModel) }
        .flowOn(dispatcherProvider.io())

    private val credentials = HashMap<String, String>()

    override suspend fun authenticate(uuid: String, password: String) {
        withContext(dispatcherProvider.io()) {
            credentials[uuid] = password
        }
    }

    override suspend fun loadServers(): List<ServerConfig> {
        return withContext(dispatcherProvider.io()) {
            appDatabase.serverDao().loadAll()
                .map(ServerConverter::toModel)
        }
    }

    override suspend fun loadServer(uuid: String): ServerConfig {
        return withContext(dispatcherProvider.io()) {
            val serverEntity = appDatabase.serverDao().load(uuid)
            val serverConfig = ServerConverter.toModel(serverEntity)
            when (serverConfig.authMethod) {
                AuthMethod.PASSWORD -> serverConfig.copy(
                    password = credentials[uuid] ?: serverConfig.password
                )
                AuthMethod.KEY -> serverConfig.copy(
                    passphrase = credentials[uuid] ?: serverConfig.passphrase
                )
            }
        }
    }

    override suspend fun upsertServer(serverConfig: ServerConfig) {
        withContext(dispatcherProvider.io()) {
            credentials.remove(serverConfig.uuid)
            val entity = ServerConverter.toEntity(serverConfig)
            appDatabase.serverDao().insert(entity)
        }
    }

    override suspend fun deleteServer(serverConfig: ServerConfig) {
        withContext(dispatcherProvider.io()) {
            credentials.remove(serverConfig.uuid)
            val entity = ServerConverter.toEntity(serverConfig)
            appDatabase.serverDao().delete(entity)
        }
    }
}