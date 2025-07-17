

package com.kira.ui.editorkit.model

import android.graphics.Color
import androidx.annotation.ColorInt

data class StyleSpan(
    @ColorInt
    val color: Int = Color.WHITE,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val strikethrough: Boolean = false
)