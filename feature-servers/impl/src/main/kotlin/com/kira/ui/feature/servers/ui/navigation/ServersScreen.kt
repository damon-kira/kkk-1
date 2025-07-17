

package com.kira.ui.feature.servers.ui.navigation

import com.kira.ui.core.extensions.toJsonEncoded
import com.kira.ui.core.navigation.Screen
import com.kira.ui.filesystem.base.model.ServerConfig
import com.google.gson.Gson

sealed class ServersScreen(route: String) : Screen<String>(route) {

    class EditServer(serverConfig: ServerConfig) : ServersScreen(
        route = "blacksquircle://settings/cloud/edit?data=${Gson().toJsonEncoded(serverConfig)}",
    )
}