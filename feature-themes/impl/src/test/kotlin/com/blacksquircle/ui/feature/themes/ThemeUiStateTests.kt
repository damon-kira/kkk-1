

package com.kira.ui.feature.themes

import com.kira.ui.core.provider.resources.StringProvider
import com.kira.ui.core.tests.MainDispatcherRule
import com.kira.ui.core.tests.TimberConsoleRule
import com.kira.ui.feature.themes.domain.model.ThemeModel
import com.kira.ui.feature.themes.domain.repository.ThemesRepository
import com.kira.ui.feature.themes.ui.mvi.ThemeIntent
import com.kira.ui.feature.themes.ui.mvi.ThemesViewState
import com.kira.ui.feature.themes.ui.viewmodel.ThemesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ThemeUiStateTests {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberConsoleRule = TimberConsoleRule()

    private val stringProvider = mockk<StringProvider>()
    private val themesRepository = mockk<ThemesRepository>()

    @Test
    fun `When opening the screen Then display loading state`() = runTest {
        // Given
        coEvery { themesRepository.loadThemes() } coAnswers { delay(200); emptyList() }

        // When
        val viewModel = themesViewModel()
        viewModel.obtainEvent(ThemeIntent.LoadThemes)

        // Then
        val themesViewState = ThemesViewState.Loading
        assertEquals(themesViewState, viewModel.themesState.value)
    }

    @Test
    fun `When user has no themes in database Then display empty state`() = runTest {
        // Given
        coEvery { themesRepository.loadThemes() } returns emptyList()

        // When
        val viewModel = themesViewModel()
        viewModel.obtainEvent(ThemeIntent.LoadThemes)

        // Then
        val themesViewState = ThemesViewState.Empty("")
        assertEquals(themesViewState, viewModel.themesState.value)
    }

    @Test
    fun `When user has themes in database Then display theme list`() = runTest {
        val themeList = listOf(
            ThemeModel(
                uuid = "1",
                name = "Darcula",
                author = "Squircle CE",
                description = "Default color scheme",
                isExternal = true,
                colorScheme = mockk()
            ),
        )

        // Given
        coEvery { themesRepository.loadThemes() } returns themeList

        // When
        val viewModel = themesViewModel()
        viewModel.obtainEvent(ThemeIntent.LoadThemes)

        // Then
        val themesViewState = ThemesViewState.Data("", themeList)
        assertEquals(themesViewState, viewModel.themesState.value)
    }

    @Test
    fun `When user types in search bar Then update theme list`() = runTest {
        val themeList = listOf(
            ThemeModel(
                uuid = "1",
                name = "Darcula",
                author = "Squircle CE",
                description = "Default color scheme",
                isExternal = true,
                colorScheme = mockk()
            ),
            ThemeModel(
                uuid = "2",
                name = "Eclipse",
                author = "Squircle CE",
                description = "Default color scheme",
                isExternal = true,
                colorScheme = mockk()
            ),
        )

        // Given
        coEvery { themesRepository.loadThemes() } returns themeList
        coEvery { themesRepository.loadThemes(any()) } coAnswers {
            themeList.filter { it.name.contains(firstArg<String>()) }
        }

        // When
        val viewModel = themesViewModel()
        viewModel.obtainEvent(ThemeIntent.SearchThemes("Eclipse"))

        // Then
        val themesViewState = ThemesViewState.Data("Eclipse", themeList.drop(1))
        assertEquals(themesViewState, viewModel.themesState.value)
    }

    private fun themesViewModel(): ThemesViewModel {
        return ThemesViewModel(
            stringProvider = stringProvider,
            themesRepository = themesRepository,
        )
    }
}