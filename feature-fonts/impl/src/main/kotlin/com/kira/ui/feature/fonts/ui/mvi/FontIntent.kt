

package com.kira.ui.feature.fonts.ui.mvi

import android.net.Uri
import com.kira.ui.core.mvi.ViewIntent
import com.kira.ui.feature.fonts.domain.model.FontModel

sealed class FontIntent : ViewIntent() {

    data object LoadFonts : FontIntent()

    data class SearchFonts(val query: String) : FontIntent()
    data class ImportFont(val fileUri: Uri) : FontIntent()
    data class SelectFont(val fontModel: FontModel) : FontIntent()
    data class RemoveFont(val fontModel: FontModel) : FontIntent()
}