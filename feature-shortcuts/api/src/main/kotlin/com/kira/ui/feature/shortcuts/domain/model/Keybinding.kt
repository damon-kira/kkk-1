

package com.kira.ui.feature.shortcuts.domain.model

data class Keybinding(
    val shortcut: Shortcut,
    val isCtrl: Boolean = false,
    val isShift: Boolean = false,
    val isAlt: Boolean = false,
    val key: Char = '\u0000',
)