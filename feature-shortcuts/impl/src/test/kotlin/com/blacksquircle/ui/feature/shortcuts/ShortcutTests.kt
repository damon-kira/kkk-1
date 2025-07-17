

package com.kira.ui.feature.shortcuts

import com.kira.ui.core.provider.resources.StringProvider
import com.kira.ui.core.tests.MainDispatcherRule
import com.kira.ui.core.tests.TimberConsoleRule
import com.kira.ui.feature.shortcuts.domain.model.Keybinding
import com.kira.ui.feature.shortcuts.domain.model.Shortcut
import com.kira.ui.feature.shortcuts.domain.repository.ShortcutsRepository
import com.kira.ui.feature.shortcuts.ui.mvi.ShortcutIntent
import com.kira.ui.feature.shortcuts.ui.viewmodel.ShortcutsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShortcutTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberConsoleRule = TimberConsoleRule()

    private val stringProvider = mockk<StringProvider>()
    private val shortcutsRepository = mockk<ShortcutsRepository>()

    @Before
    fun setup() {
        coEvery { shortcutsRepository.loadShortcuts() } returns emptyList()
        coEvery { shortcutsRepository.restoreDefaults() } returns Unit

        coEvery { shortcutsRepository.reassign(any()) } returns Unit
        coEvery { shortcutsRepository.disable(any()) } returns Unit
    }

    @Test
    fun `When opening the screen Then display shortcuts`() = runTest {
        // Given
        val shortcuts = listOf(
            Keybinding(Shortcut.CUT, isCtrl = true, key = 'X'),
            Keybinding(Shortcut.COPY, isCtrl = true, key = 'C'),
            Keybinding(Shortcut.PASTE, isCtrl = true, key = 'V'),
        )
        coEvery { shortcutsRepository.loadShortcuts() } returns shortcuts

        // When
        val viewModel = shortcutsViewModel()
        viewModel.obtainEvent(ShortcutIntent.LoadShortcuts)

        // Then
        assertEquals(shortcuts, viewModel.shortcuts.value)
    }

    @Test
    fun `When reassign the keybinding Then update shortcuts`() = runTest {
        // Given
        val shortcuts = listOf(
            Keybinding(Shortcut.CUT, isCtrl = true, key = 'X'),
            Keybinding(Shortcut.COPY, isCtrl = true, key = 'C'),
            Keybinding(Shortcut.PASTE, isCtrl = true, key = 'V'),
        )
        val keybinding = Keybinding(Shortcut.CUT, isCtrl = true, isShift = true, key = 'X')
        val reassigned = shortcuts.map { if (it.shortcut == Shortcut.CUT) keybinding else it }
        coEvery { shortcutsRepository.loadShortcuts() } returns shortcuts andThen reassigned

        // When
        val viewModel = shortcutsViewModel()
        viewModel.obtainEvent(ShortcutIntent.Reassign(keybinding))

        // Then
        coVerify(exactly = 1) { shortcutsRepository.reassign(keybinding) }
        assertEquals(reassigned, viewModel.shortcuts.value)
    }

    @Test
    fun `When keybinding has conflict Then reassign keybinding`() = runTest {
        // Given
        val shortcuts = listOf(
            Keybinding(Shortcut.CUT, isCtrl = true, key = 'X'),
            Keybinding(Shortcut.COPY, isCtrl = true, key = 'C'),
        )
        val reassigned = listOf(
            Keybinding(Shortcut.CUT, isCtrl = true, key = 'C'),
            Keybinding(Shortcut.COPY, isCtrl = true, key = '\u0000'),
        )
        coEvery { shortcutsRepository.loadShortcuts() } returns shortcuts andThen reassigned

        // When
        val viewModel = shortcutsViewModel()
        viewModel.obtainEvent(ShortcutIntent.Reassign(reassigned[0]))

        assertEquals(shortcuts, viewModel.shortcuts.value) // check nothing happened
        viewModel.obtainEvent(ShortcutIntent.ResolveConflict(reassign = true))

        // Then
        coVerify(exactly = 1) { shortcutsRepository.disable(shortcuts[1]) }
        coVerify(exactly = 1) { shortcutsRepository.reassign(reassigned[0]) }
        assertEquals(reassigned, viewModel.shortcuts.value) // check reassigned
    }

    @Test
    fun `When keybinding has conflict Then ignore keybinding`() = runTest {
        // Given
        val shortcuts = listOf(
            Keybinding(Shortcut.CUT, isCtrl = true, key = 'X'),
            Keybinding(Shortcut.COPY, isCtrl = true, key = 'C'),
        )
        val reassigned = Keybinding(Shortcut.CUT, isCtrl = true, key = 'C')
        coEvery { shortcutsRepository.loadShortcuts() } returns shortcuts andThen shortcuts

        // When
        val viewModel = shortcutsViewModel()
        viewModel.obtainEvent(ShortcutIntent.Reassign(reassigned))

        assertEquals(shortcuts, viewModel.shortcuts.value) // check nothing happened
        viewModel.obtainEvent(ShortcutIntent.ResolveConflict(reassign = false))

        // Then
        coVerify(exactly = 0) { shortcutsRepository.disable(any()) }
        coVerify(exactly = 0) { shortcutsRepository.reassign(any()) }
        assertEquals(shortcuts, viewModel.shortcuts.value) // check ignored
    }

    private fun shortcutsViewModel(): ShortcutsViewModel {
        return ShortcutsViewModel(
            stringProvider = stringProvider,
            shortcutsRepository = shortcutsRepository,
        )
    }
}