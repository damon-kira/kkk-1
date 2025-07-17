

package com.kira.ui.feature.servers.ui.mvi

import com.kira.ui.core.mvi.ViewIntent
import com.kira.ui.filesystem.base.model.ServerConfig

sealed class ServerIntent : ViewIntent() {

    data object LoadServers : ServerIntent()
    data class UpsertServer(val serverConfig: ServerConfig) : ServerIntent()
    data class DeleteServer(val serverConfig: ServerConfig) : ServerIntent()
}