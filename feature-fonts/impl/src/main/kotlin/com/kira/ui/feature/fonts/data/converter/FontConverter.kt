

package com.kira.ui.feature.fonts.data.converter

import com.kira.ui.core.storage.database.entity.font.FontEntity
import com.kira.ui.feature.fonts.domain.model.FontModel

object FontConverter {

    fun toModel(fontEntity: FontEntity): FontModel {
        return FontModel(
            fontUuid = fontEntity.fontUuid,
            fontName = fontEntity.fontName,
            fontPath = fontEntity.fontPath,
            isExternal = true,
        )
    }

    fun toEntity(fontModel: FontModel): FontEntity {
        return FontEntity(
            fontUuid = fontModel.fontUuid,
            fontName = fontModel.fontName,
            fontPath = fontModel.fontPath,
            supportLigatures = false,
        )
    }
}