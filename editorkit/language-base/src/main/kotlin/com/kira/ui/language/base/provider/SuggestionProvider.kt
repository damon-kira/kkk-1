package com.kira.ui.language.base.provider

import com.kira.ui.language.base.model.Suggestion
import com.kira.ui.language.base.model.TextStructure

interface SuggestionProvider {

    fun getAll(): Set<Suggestion>

    fun processAllLines(structure: TextStructure)
    fun processLine(lineNumber: Int, text: CharSequence)
    fun deleteLine(lineNumber: Int)
    fun clearLines()
}