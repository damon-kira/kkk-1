

package com.kira.ui.feature.editor

import com.kira.ui.core.provider.resources.StringProvider
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.core.tests.MainDispatcherRule
import com.kira.ui.core.tests.TimberConsoleRule
import com.kira.ui.feature.editor.domain.repository.DocumentRepository
import com.kira.ui.feature.editor.ui.mvi.EditorIntent
import com.kira.ui.feature.editor.ui.mvi.ToolbarViewState
import com.kira.ui.feature.editor.ui.viewmodel.EditorViewModel
import com.kira.ui.feature.fonts.domain.repository.FontsRepository
import com.kira.ui.feature.settings.domain.repository.SettingsRepository
import com.kira.ui.feature.shortcuts.domain.repository.ShortcutsRepository
import com.kira.ui.feature.themes.domain.repository.ThemesRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CloseTabTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberConsoleRule = TimberConsoleRule()

    private val stringProvider = mockk<StringProvider>()
    private val settingsManager = mockk<SettingsManager>()
    private val documentRepository = mockk<DocumentRepository>()
    private val themesRepository = mockk<ThemesRepository>()
    private val fontsRepository = mockk<FontsRepository>()
    private val shortcutsRepository = mockk<ShortcutsRepository>()
    private val settingsRepository = mockk<SettingsRepository>()

    @Before
    fun setup() {
        every { settingsManager.extendedKeyboard } returns true
        every { settingsManager.autoSaveFiles } returns false
        every { settingsManager.selectedUuid = any() } returns Unit
        every { settingsManager.selectedUuid } returns ""

        coEvery { documentRepository.loadDocuments() } returns emptyList()
        coEvery { documentRepository.updateDocument(any()) } returns Unit
        coEvery { documentRepository.deleteDocument(any()) } returns Unit

        coEvery { documentRepository.loadFile(any()) } returns mockk()
        coEvery { documentRepository.saveFile(any(), any()) } returns Unit
        coEvery { documentRepository.saveFileAs(any(), any()) } returns Unit
    }

    @Test
    fun `When closing selected tab at the first position Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[0] // selected "first.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseTab(0, allowModified = true))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "second.txt"),
            createDocument(position = 1, fileName = "third.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 0)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing selected tab at the last position Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[2] // selected "third.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseTab(2, allowModified = true))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 1)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing selected tab in the middle Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[1] // selected "second.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseTab(1, allowModified = true))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "third.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 0)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing unselected tab at the first position Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[2] // selected "third.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseTab(0, allowModified = true))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "second.txt"),
            createDocument(position = 1, fileName = "third.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 1)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing unselected tab at the last position Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[0] // selected "first.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseTab(2, allowModified = true))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 0)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing all tabs but not selected Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[1] // selected "second.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseOthers(1))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "second.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 0)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing all tabs but not unselected Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[2] // selected "third.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseOthers(1))

        // Then
        val updatedList = listOf(
            createDocument(position = 0, fileName = "second.txt"),
        )
        val toolbarViewState = ToolbarViewState.ActionBar(updatedList, 0)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    @Test
    fun `When closing all tabs Then check documents list`() = runTest {
        // Given
        val documentList = listOf(
            createDocument(position = 0, fileName = "first.txt"),
            createDocument(position = 1, fileName = "second.txt"),
            createDocument(position = 2, fileName = "third.txt"),
        )
        val selected = documentList[2] // selected "third.txt"

        every { settingsManager.selectedUuid } returns selected.uuid
        coEvery { documentRepository.loadDocuments() } returns documentList

        // When
        val viewModel = editorViewModel()
        viewModel.obtainEvent(EditorIntent.CloseAll)

        // Then
        val toolbarViewState = ToolbarViewState.ActionBar(emptyList(), -1)
        assertEquals(toolbarViewState, viewModel.toolbarViewState.value)
    }

    private fun editorViewModel(): EditorViewModel {
        return EditorViewModel(
            stringProvider = stringProvider,
            settingsManager = settingsManager,
            documentRepository = documentRepository,
            themesRepository = themesRepository,
            fontsRepository = fontsRepository,
            shortcutsRepository = shortcutsRepository,
            settingsRepository = settingsRepository,
        )
    }
}