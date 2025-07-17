

package com.kira.ui.feature.changelog

import com.kira.ui.feature.changelog.data.converter.ReleaseConverter
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ReleaseConverterTest {

    @Test
    fun `When converting changelog Then check list size`() {
        // Given
        val testData = """
            <b>v2018.1.2, 11 Feb. 2018</b><br>
            • <b>Added:</b> Line №1<br>
            • <b>Fixed:</b> Line №2<br>
            • <b>Fixed:</b> Line №3<br>
            • Line №4<br>
            <br>
            <b>v2018.1.1, 28 Jan. 2018</b><br>
            • <b>Added:</b> Line №1<br>
            • <b>Added:</b> Line №2<br>
            • Line №3<br>
            <br>
            <b>v2018.1.0, 23 Jan. 2018</b><br>
            • Line №1<br>
            <br>
        """.trimIndent()

        // When
        val releaseList = ReleaseConverter.toReleaseModels(testData)

        // Then
        assertEquals(3, releaseList.size)
    }

    @Test
    fun `When converting changelog Then verify release info`() {
        // Given
        val testData = """
            <b>v2018.1.2, 11 Feb. 2018</b><br>
            • <b>Added:</b> Line №1<br>
            • <b>Fixed:</b> Line №2<br>
            • <b>Fixed:</b> Line №3<br>
            • Line №4<br>
            <br>
        """.trimIndent()

        // When
        val releaseModel = ReleaseConverter.toReleaseModels(testData).first()

        // Then
        assertEquals("v2018.1.2", releaseModel.versionName)
        assertEquals("11 Feb. 2018", releaseModel.releaseDate)
    }
}