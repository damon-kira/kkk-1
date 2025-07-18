

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
        route = "kiralearning://explorer/delete?fileName=${fileName.encodeUrl()}&fileCount=$fileCount",
    )
    class RenameDialog(fileName: String) : ExplorerScreen(
        route = "kiralearning://explorer/rename?fileName=${fileName.encodeUrl()}",
    )
    class ProgressDialog(totalCount: Int, operation: Operation) : ExplorerScreen(
        route = "kiralearning://explorer/progress?totalCount=$totalCount&operation=${operation.value}",
    )
    class PropertiesDialog(fileModel: FileModel) : ExplorerScreen(
        route = "kiralearning://explorer/properties?data=${Gson().toJsonEncoded(fileModel)}",
    )
    class AuthDialog(authMethod: AuthMethod) : ExplorerScreen(
        route = "kiralearning://explorer/authenticate?authMethod=${authMethod.value}"
    )

    data object CreateDialog : ExplorerScreen("kiralearning://explorer/create")
    data object CompressDialog : ExplorerScreen("kiralearning://explorer/compress")

    data object StorageDeniedForever : ExplorerScreen(
        route = "kiralearning://explorer/storage_denied_forever"
    )
    data object NotificationDeniedForever : ExplorerScreen(
        route = "kiralearning://explorer/notification_denied_forever"
    )
}