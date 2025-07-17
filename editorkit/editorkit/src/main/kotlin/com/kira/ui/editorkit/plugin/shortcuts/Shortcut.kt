

package com.kira.ui.editorkit.plugin.shortcuts

data class Shortcut(
    val ctrl: Boolean,
    val shift: Boolean,
    val alt: Boolean,
    val keyCode: Int,
)