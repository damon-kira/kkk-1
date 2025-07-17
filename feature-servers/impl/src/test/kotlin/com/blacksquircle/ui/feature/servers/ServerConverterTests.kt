

package com.kira.ui.feature.servers

import com.kira.ui.core.storage.database.entity.server.ServerEntity
import com.kira.ui.feature.servers.data.converter.ServerConverter
import com.kira.ui.filesystem.base.model.AuthMethod
import com.kira.ui.filesystem.base.model.ServerConfig
import org.junit.Assert.assertEquals
import org.junit.Test

class ServerConverterTests {

    @Test
    fun `convert ServerEntity to ServerConfig`() {
        val serverEntity = ServerEntity(
            uuid = "1234567890",
            scheme = "ftp",
            name = "test",
            address = "192.168.21.97",
            port = 21,
            initialDir = "",
            authMethod = AuthMethod.PASSWORD.value,
            username = "username",
            password = "password",
            privateKey = null,
            passphrase = "test",
        )
        val serverConfig = ServerConfig(
            uuid = "1234567890",
            scheme = "ftp",
            name = "test",
            address = "192.168.21.97",
            port = 21,
            initialDir = "",
            authMethod = AuthMethod.PASSWORD,
            username = "username",
            password = "password",
            privateKey = null,
            passphrase = "test",
        )

        assertEquals(serverConfig, ServerConverter.toModel(serverEntity))
    }

    @Test
    fun `convert ServerConfig to ServerEntity`() {
        val serverConfig = ServerConfig(
            uuid = "1234567890",
            scheme = "ftp",
            name = "test",
            address = "192.168.21.97",
            port = 21,
            initialDir = "",
            authMethod = AuthMethod.PASSWORD,
            username = "username",
            password = "password",
            privateKey = null,
            passphrase = "test",
        )
        val serverEntity = ServerEntity(
            uuid = "1234567890",
            scheme = "ftp",
            name = "test",
            address = "192.168.21.97",
            port = 21,
            initialDir = "",
            authMethod = AuthMethod.PASSWORD.value,
            username = "username",
            password = "password",
            privateKey = null,
            passphrase = "test",
        )

        assertEquals(serverEntity, ServerConverter.toEntity(serverConfig))
    }
}