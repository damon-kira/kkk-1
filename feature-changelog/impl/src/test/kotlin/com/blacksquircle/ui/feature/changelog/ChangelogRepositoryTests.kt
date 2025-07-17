

package com.kira.ui.feature.changelog

import android.content.Context
import android.content.res.Resources
import com.kira.ui.core.tests.TestDispatcherProvider
import com.kira.ui.feature.changelog.data.repository.ChangelogRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import java.io.InputStream

class ChangelogRepositoryTests {

    @Test
    @Ignore("Unstable on CI")
    fun `When loading changelog Then read data from resource file`() = runTest {
        // Given
        val resources = mockk<Resources>()
        val context = mockk<Context>()
        every { context.resources } returns resources
        every { resources.openRawResource(any()) } returns InputStream.nullInputStream()

        val repository = ChangelogRepositoryImpl(
            dispatcherProvider = TestDispatcherProvider(),
            context = context
        )

        // When
        repository.loadChangelog()

        // Then
        verify(exactly = 1) { resources.openRawResource(R.raw.changelog) }
    }
}