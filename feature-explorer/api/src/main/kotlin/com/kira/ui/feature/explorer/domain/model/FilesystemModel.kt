package com.kira.ui.feature.explorer.domain.model

import com.kira.ui.filesystem.base.model.ServerConfig

data class FilesystemModel(val uuid: String, val title: String) {
    constructor(serverConfig: ServerConfig) : this(serverConfig.uuid, serverConfig.name)
}