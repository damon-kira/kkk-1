

package com.kira.ui.feature.editor.domain.model

import android.webkit.MimeTypeMap
import com.kira.ui.language.base.Language

data class DocumentModel(
    val uuid: String,
    val fileUri: String,
    val filesystemUuid: String,
    val language: Language,
    val modified: Boolean,
    val position: Int,
    val scrollX: Int,
    val scrollY: Int,
    val selectionStart: Int,
    val selectionEnd: Int,
) {

    val scheme: String
        get() = fileUri.substringBefore("://")
    val path: String
        get() = fileUri.substringAfterLast("://").ifEmpty { "/" }
    val name: String
        get() = fileUri.substringAfterLast("/").ifEmpty { "/" }
    val extension: String
        get() = fileUri.substringAfterLast(".", "")
    val mimeType: String
        get() = MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension)
            ?: "text/*"
}