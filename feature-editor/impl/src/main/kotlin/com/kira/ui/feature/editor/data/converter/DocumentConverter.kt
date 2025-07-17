

package com.kira.ui.feature.editor.data.converter

import com.kira.ui.core.factory.LanguageFactory
import com.kira.ui.core.storage.database.entity.document.DocumentEntity
import com.kira.ui.feature.editor.domain.model.DocumentModel
import com.kira.ui.filesystem.base.model.FileModel
import java.util.*

object DocumentConverter {

    fun toModel(documentModel: DocumentModel): FileModel {
        return FileModel(
            fileUri = documentModel.fileUri,
            filesystemUuid = documentModel.filesystemUuid,
            size = 0,
            lastModified = 0,
            directory = false,
        )
    }

    fun toModel(fileModel: FileModel): DocumentModel {
        return DocumentModel(
            uuid = UUID.randomUUID().toString(),
            fileUri = fileModel.fileUri,
            filesystemUuid = fileModel.filesystemUuid,
            language = LanguageFactory.create(fileModel.name),
            modified = false,
            position = 0,
            scrollX = 0,
            scrollY = 0,
            selectionStart = 0,
            selectionEnd = 0,
        )
    }

    fun toModel(documentEntity: DocumentEntity): DocumentModel {
        return DocumentModel(
            uuid = documentEntity.uuid,
            fileUri = documentEntity.fileUri,
            filesystemUuid = documentEntity.filesystemUuid,
            language = LanguageFactory.fromName(documentEntity.language),
            modified = documentEntity.modified,
            position = documentEntity.position,
            scrollX = documentEntity.scrollX,
            scrollY = documentEntity.scrollY,
            selectionStart = documentEntity.selectionStart,
            selectionEnd = documentEntity.selectionEnd,
        )
    }

    fun toEntity(documentModel: DocumentModel): DocumentEntity {
        return DocumentEntity(
            uuid = documentModel.uuid,
            fileUri = documentModel.fileUri,
            filesystemUuid = documentModel.filesystemUuid,
            language = documentModel.language.languageName,
            modified = documentModel.modified,
            position = documentModel.position,
            scrollX = documentModel.scrollX,
            scrollY = documentModel.scrollY,
            selectionStart = documentModel.selectionStart,
            selectionEnd = documentModel.selectionEnd,
        )
    }
}