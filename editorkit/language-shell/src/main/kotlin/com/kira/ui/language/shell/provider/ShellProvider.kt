

package com.kira.ui.language.shell.provider

import com.kira.ui.language.base.model.Suggestion
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.utils.WordsManager

class ShellProvider private constructor() : SuggestionProvider {

    companion object {

        private var shellProvider: ShellProvider? = null

        fun getInstance(): ShellProvider {
            return shellProvider ?: ShellProvider().also {
                shellProvider = it
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