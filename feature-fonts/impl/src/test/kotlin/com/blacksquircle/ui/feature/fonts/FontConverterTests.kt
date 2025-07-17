

package com.kira.ui.feature.fonts

import com.kira.ui.core.storage.database.entity.font.FontEntity
import com.kira.ui.feature.fonts.data.converter.FontConverter
import com.kira.ui.feature.fonts.domain.model.FontModel
import org.junit.Assert.assertEquals
import org.junit.Test

class FontConverterTests {

    @Test
    fun `convert FontEntity to FontModel`() {
        val fontEntity = FontEntity(
            fontUuid = "droid_sans_mono.ttf",
            fontName = "Droid Sans Mono",
            fontPath = "/storage/emulated/0/font.ttf",
            supportLigatures = false,
        )
        val fontModel = FontModel(
            fontUuid = "droid_sans_mono.ttf",
            fontName = "Droid Sans Mono",
            fontPath = "/storage/emulated/0/font.ttf",
            isExternal = true,
        )

        assertEquals(fontModel, FontConverter.toModel(fontEntity))
    }

    @Test
    fun `convert FontModel to FontEntity`() {
        val fontEntity = FontEntity(
            fontUuid = "droid_sans_mono.ttf",
            fontName = "Droid Sans Mono",
            fontPath = "/storage/emulated/0/font.ttf",
            supportLigatures = false,
        )
        val fontModel = FontModel(
            fontUuid = "droid_sans_mono.ttf",
            fontName = "Droid Sans Mono",
            fontPath = "/storage/emulated/0/font.ttf",
            isExternal = true,
        )

        assertEquals(fontEntity, FontConverter.toEntity(fontModel))
    }
}