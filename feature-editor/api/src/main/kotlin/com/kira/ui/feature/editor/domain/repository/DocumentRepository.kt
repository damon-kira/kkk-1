

package com.kira.ui.feature.editor.domain.repository

import android.net.Uri
import com.kira.ui.editorkit.model.FindParams
import com.kira.ui.editorkit.model.FindResult
import com.kira.ui.feature.editor.domain.model.DocumentContent
import com.kira.ui.feature.editor.domain.model.DocumentModel
import com.kira.ui.feature.editor.domain.model.DocumentParams

interface DocumentRepository {

    suspend fun loadDocuments(): List<DocumentModel>
    suspend fun updateDocument(documentModel: DocumentModel)
    suspend fun deleteDocument(documentModel: DocumentModel)

    suspend fun openFile(fileUri: Uri): DocumentModel
    suspend fun loadFile(documentModel: DocumentModel): DocumentContent
    suspend fun saveFile(content: DocumentContent, params: DocumentParams)
    suspend fun saveFileAs(documentModel: DocumentModel, fileUri: Uri)

    suspend fun find(text: CharSequence, params: FindParams): List<FindResult>
}