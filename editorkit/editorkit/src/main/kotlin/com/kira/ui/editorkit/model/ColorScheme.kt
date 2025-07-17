

package com.kira.ui.editorkit.model

import androidx.annotation.ColorInt

data class ColorScheme(
    @ColorInt val textColor: Int,
    @ColorInt val cursorColor: Int,
    @ColorInt val backgroundColor: Int,
    @ColorInt val gutterColor: Int,
    @ColorInt val gutterDividerColor: Int,
    @ColorInt val gutterCurrentLineNumberColor: Int,
    @ColorInt val gutterTextColor: Int,
    @ColorInt val selectedLineColor: Int,
    @ColorInt val selectionColor: Int,
    @ColorInt val suggestionQueryColor: Int,
    @ColorInt val findResultBackgroundColor: Int,
    @ColorInt val delimiterBackgroundColor: Int,
    @ColorInt val numberColor: Int,
    @ColorInt val operatorColor: Int,
    @ColorInt val keywordColor: Int,
    @ColorInt val typeColor: Int,
    @ColorInt val langConstColor: Int,
    @ColorInt val preprocessorColor: Int,
    @ColorInt val variableColor: Int,
    @ColorInt val methodColor: Int,
    @ColorInt val stringColor: Int,
    @ColorInt val commentColor: Int,
    @ColorInt val tagColor: Int,
    @ColorInt val tagNameColor: Int,
    @ColorInt val attrNameColor: Int,
    @ColorInt val attrValueColor: Int,
    @ColorInt val entityRefColor: Int
)