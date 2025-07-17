

package com.kira.ui.feature.editor.ui.mvi

sealed class EditorErrorAction {
    data object Undefined : EditorErrorAction()
    data object CloseDocument : EditorErrorAction()
}