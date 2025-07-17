

package com.kira.ui.language.toml.provider

import com.kira.ui.language.base.model.Suggestion
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.utils.WordsManager

class TomlProvider private constructor() : SuggestionProvider {

    companion object {

        private var tomlProvider: TomlProvider? = null

        fun getInstance(): TomlProvider {
            return tomlProvider ?: TomlProvider().also {
                tomlProvider = it
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