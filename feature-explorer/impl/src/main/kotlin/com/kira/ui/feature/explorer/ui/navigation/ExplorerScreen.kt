

package com.kira.ui.feature.explorer.ui.navigation

import com.kira.ui.core.extensions.encodeUrl
import com.kira.ui.core.extensions.toJsonEncoded
import com.kira.ui.core.navigation.Screen
import com.kira.ui.feature.explorer.data.utils.Operation
import com.kira.ui.filesystem.base.model.AuthMethod
import com.kira.ui.filesystem.base.model.FileModel
import com.google.gson.Gson

sealed class ExplorerScreen(route: String) : Screen<String>(route) {

    class DeleteDialog(fileName: String, fileCount: Int) : ExplorerScreen(
        route = "blacksquircle://explorer/delete?fileName=${fileName.encodeUrl()}&fileCount=$fileCount",
    )
    class RenameDialog(fileName: String) : ExplorerScreen(
        route = "blacksquircle://explorer/rename?fileName=${fileName.encodeUrl()}",
    )
    class ProgressDialog(totalCount: Int, operation: Operation) : ExplorerScreen(
        route = "blacksquircle://explorer/progress?totalCount=$totalCount&operation=${operation.value}",
    )
    class PropertiesDialog(fileModel: FileModel) : ExplorerScreen(
        route = "blacksquircle://explorer/properties?data=${Gson().toJsonEncoded(fileModel)}",
    )
    class AuthDialog(authMethod: AuthMethod) : ExplorerScreen(
        route = "blacksquircle://explorer/authenticate?authMethod=${authMethod.value}"
    )

    data object CreateDialog : ExplorerScreen("blacksquircle://explorer/create")
    data object CompressDialog : ExplorerScreen("blacksquircle://explorer/compress")

    data object StorageDeniedForever : ExplorerScreen(
        route = "blacksquircle://explorer/storage_denied_forever"
    )
    data object NotificationDeniedForever : ExplorerScreen(
        route = "blacksquircle://explorer/notification_denied_forever"
    )
}