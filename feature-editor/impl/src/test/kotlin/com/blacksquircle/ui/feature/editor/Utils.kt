

package com.kira.ui.feature.editor

import com.kira.ui.feature.editor.domain.model.DocumentModel
import com.kira.ui.language.base.Language
import io.mockk.mockk

private val language = mockk<Language>()

fun createDocument(
    position: Int,
    fileName: String,
    modified: Boolean = false,
): DocumentModel {
    return DocumentModel(
        uuid = fileName,
        fileUri = "file:///storage/emulated/0/$fileName",
        filesystemUuid = "local",
        language = language,
        modified = modified,
        position = position,
        scrollX = 1,
        scrollY = 2,
        selectionStart = 3,
        selectionEnd = 4,
    )
}