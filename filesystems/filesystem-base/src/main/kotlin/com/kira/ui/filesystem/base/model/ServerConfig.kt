

package com.kira.ui.filesystem.base.model

data class ServerConfig(
    val uuid: String,
    val scheme: String,
    val name: String,
    val address: String,
    val port: Int,
    val initialDir: String,
    val authMethod: AuthMethod,
    val username: String,
    val password: String?,
    val privateKey: String?,
    val passphrase: String?,
)