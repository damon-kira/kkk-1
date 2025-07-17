

package com.kira.ui.editorkit.plugin.shortcuts

fun interface OnShortcutListener {
    fun onShortcut(shortcut: Shortcut): Boolean
}