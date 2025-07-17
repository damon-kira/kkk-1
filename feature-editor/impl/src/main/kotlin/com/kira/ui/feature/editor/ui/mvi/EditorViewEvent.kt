

package com.kira.ui.feature.editor.ui.mvi

import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.editorkit.model.FindResult

sealed class EditorViewEvent : ViewEvent() {

    data class FindResults(val results: List<FindResult>) : EditorViewEvent()
    data class InsertColor(val color: String) : EditorViewEvent()
    data class GotoLine(val line: Int) : EditorViewEvent()
}