

package com.kira.ui.feature.explorer.ui.mvi

import com.kira.ui.filesystem.base.model.AuthMethod

sealed class ExplorerErrorAction {
    data object Undefined : ExplorerErrorAction()
    data object RequestPermission : ExplorerErrorAction()
    data class EnterCredentials(val authMethod: AuthMethod) : ExplorerErrorAction()
}