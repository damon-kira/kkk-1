package com.kira.ui.language.base.styler

import com.kira.ui.language.base.model.SyntaxHighlightResult
import com.kira.ui.language.base.model.TextStructure


interface LanguageStyler {
    fun execute(structure: TextStructure): List<SyntaxHighlightResult>
}