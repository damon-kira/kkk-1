

package com.kira.ui.feature.editor

import com.kira.ui.core.storage.database.entity.document.DocumentEntity
import com.kira.ui.feature.editor.data.converter.DocumentConverter
import com.kira.ui.feature.editor.domain.model.DocumentModel
import com.kira.ui.filesystem.base.model.FileModel
import com.kira.ui.language.base.Language
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DocumentConverterTests {

    private val plainTextLanguage = mockk<Language>()

    @Before
    fun setup() {
        every { plainTextLanguage.languageName } returns "plaintext"
    }

    @Test
    fun `convert FileModel to DocumentModel`() {
        val fileModel = FileModel(
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            size = 0L,
            lastModified = 1L,
            directory = false,
        )
        val documentModel = DocumentModel(
            uuid = "0",
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            language = plainTextLanguage,
            modified = false,
            position = 0,
            scrollX = 0,
            scrollY = 0,
            selectionStart = 0,
            selectionEnd = 0,
        )
        val convert = DocumentConverter.toModel(fileModel)

        assertEquals(documentModel.fileUri, convert.fileUri)
        assertEquals(documentModel.filesystemUuid, convert.filesystemUuid)
        assertEquals(documentModel.language.languageName, convert.language.languageName)
        assertEquals(documentModel.modified, convert.modified)
        assertEquals(documentModel.position, convert.position)
        assertEquals(documentModel.scrollX, convert.scrollX)
        assertEquals(documentModel.scrollY, convert.scrollY)
        assertEquals(documentModel.selectionStart, convert.selectionStart)
        assertEquals(documentModel.selectionEnd, convert.selectionEnd)
    }

    @Test
    fun `convert DocumentEntity to DocumentModel`() {
        val documentEntity = DocumentEntity(
            uuid = "0",
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            language = "plaintext",
            modified = true,
            position = 10,
            scrollX = 0,
            scrollY = 50,
            selectionStart = 8,
            selectionEnd = 10,
        )
        val documentModel = DocumentModel(
            uuid = "0",
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            language = plainTextLanguage,
            modified = true,
            position = 10,
            scrollX = 0,
            scrollY = 50,
            selectionStart = 8,
            selectionEnd = 10,
        )
        val convert = DocumentConverter.toModel(documentEntity)

        assertEquals(documentModel.fileUri, convert.fileUri)
        assertEquals(documentModel.filesystemUuid, convert.filesystemUuid)
        assertEquals(documentModel.language.languageName, convert.language.languageName)
        assertEquals(documentModel.modified, convert.modified)
        assertEquals(documentModel.position, convert.position)
        assertEquals(documentModel.scrollX, convert.scrollX)
        assertEquals(documentModel.scrollY, convert.scrollY)
        assertEquals(documentModel.selectionStart, convert.selectionStart)
        assertEquals(documentModel.selectionEnd, convert.selectionEnd)
    }

    @Test
    fun `convert DocumentModel to DocumentEntity`() {
        val documentModel = DocumentModel(
            uuid = "0",
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            language = plainTextLanguage,
            modified = false,
            position = 10,
            scrollX = 0,
            scrollY = 50,
            selectionStart = 8,
            selectionEnd = 10,
        )
        val documentEntity = DocumentEntity(
            uuid = "0",
            fileUri = "file:///storage/emulated/0/Test.txt",
            filesystemUuid = "local",
            language = "plaintext",
            modified = false,
            position = 10,
            scrollX = 0,
            scrollY = 50,
            selectionStart = 8,
            selectionEnd = 10,
        )

        assertEquals(documentEntity, DocumentConverter.toEntity(documentModel))
    }
}