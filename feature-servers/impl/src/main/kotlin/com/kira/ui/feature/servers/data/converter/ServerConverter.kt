

package com.kira.ui.feature.servers.data.converter

import com.kira.ui.core.storage.database.entity.server.ServerEntity
import com.kira.ui.filesystem.base.model.AuthMethod
import com.kira.ui.filesystem.base.model.ServerConfig

object ServerConverter {

    fun toModel(serverEntity: ServerEntity): ServerConfig {
        return ServerConfig(
            uuid = serverEntity.uuid,
            scheme = serverEntity.scheme,
            name = serverEntity.name,
            address = serverEntity.address,
            port = serverEntity.port,
            initialDir = serverEntity.initialDir,
            authMethod = AuthMethod.of(serverEntity.authMethod),
            username = serverEntity.username,
            password = serverEntity.password,
            privateKey = serverEntity.privateKey,
            passphrase = serverEntity.passphrase,
        )
    }

    fun toEntity(serverConfig: ServerConfig): ServerEntity {
        return ServerEntity(
            uuid = serverConfig.uuid,
            scheme = serverConfig.scheme,
            name = serverConfig.name,
            address = serverConfig.address,
            port = serverConfig.port,
            initialDir = serverConfig.initialDir,
            authMethod = serverConfig.authMethod.value,
            username = serverConfig.username,
            password = serverConfig.password,
            privateKey = serverConfig.privateKey,
            passphrase = serverConfig.passphrase,
        )
    }
}