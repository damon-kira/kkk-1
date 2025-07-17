

package com.kira.ui.feature.settings

import android.content.Context
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.settings.data.repository.SettingsRepositoryImpl
import com.kira.ui.feature.settings.domain.model.KeyModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SettingsRepositoryTests {

    @Test
    fun `When loading keyboard preset Then return keys list containing a tab`() {
        // Given
        val repository = SettingsRepositoryImpl(
            settingsManager = mockk<SettingsManager>().apply {
                every { keyboardPreset } returns "ABC"
            },
            context = mockk<Context>().apply {
                every { getString(any()) } returns "Tab"
            },
        )

        // When
        val actual = repository.keyboardPreset()

        // Then
        val expected = listOf(
            KeyModel(display = "Tab", value = '\t'),
            KeyModel(display = "A", value = 'A'),
            KeyModel(display = "B", value = 'B'),
            KeyModel(display = "C", value = 'C'),
        )
        assertEquals(expected, actual)
    }
}