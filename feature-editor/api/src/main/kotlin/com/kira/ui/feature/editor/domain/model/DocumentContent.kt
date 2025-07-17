

package com.kira.ui.feature.editor.domain.model

import com.kira.ui.editorkit.model.UndoStack

data class DocumentContent(
    val documentModel: DocumentModel,
    val undoStack: UndoStack,
    val redoStack: UndoStack,
    val text: String,
)