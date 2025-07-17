package com.kira.ui.language.html.provider

import com.kira.ui.language.base.model.Suggestion
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.utils.WordsManager
import com.kira.ui.language.base.model.TextStructure

class HtmlProvider private constructor() : SuggestionProvider {

    companion object {

        private var htmlProvider: HtmlProvider? = null

        fun getInstance(): HtmlProvider {
            return htmlProvider ?: HtmlProvider().also {
                htmlProvider = it
            }
        }
    }

    private val wordsManager = WordsManager()

    override fun getAll(): Set<Suggestion> {
        return wordsManager.getWords()
    }

    override fun processAllLines(structure: TextStructure) {
        wordsManager.processAllLines(structure)
    }

    override fun processLine(lineNumber: Int, text: CharSequence) {
        wordsManager.processLine(lineNumber, text)
    }

    override fun deleteLine(lineNumber: Int) {
        wordsManager.deleteLine(lineNumber)
    }

    override fun clearLines() {
        wordsManager.clearLines()
    }
}