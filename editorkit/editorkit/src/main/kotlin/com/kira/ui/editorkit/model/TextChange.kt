

package com.kira.ui.editorkit.model

data class TextChange(
    var newText: String,
    var oldText: String,
    var start: Int,
)