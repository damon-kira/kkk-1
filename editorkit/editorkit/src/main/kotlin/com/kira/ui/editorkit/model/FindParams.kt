

package com.kira.ui.editorkit.model

data class FindParams(
    val query: String = "",
    val regex: Boolean = false,
    val matchCase: Boolean = false,
    val wordsOnly: Boolean = false,
)