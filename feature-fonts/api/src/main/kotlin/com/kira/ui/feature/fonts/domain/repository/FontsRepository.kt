

package com.kira.ui.feature.fonts.domain.repository

import android.net.Uri
import com.kira.ui.feature.fonts.domain.model.FontModel

interface FontsRepository {

    suspend fun current(): FontModel
    suspend fun loadFonts(): List<FontModel>
    suspend fun loadFonts(query: String): List<FontModel>
    suspend fun loadFont(path: String): FontModel

    suspend fun importFont(fileUri: Uri)
    suspend fun selectFont(fontModel: FontModel)
    suspend fun removeFont(fontModel: FontModel)
}