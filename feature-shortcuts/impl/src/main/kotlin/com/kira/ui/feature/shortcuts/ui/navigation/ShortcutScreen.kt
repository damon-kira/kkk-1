

package com.kira.ui.feature.shortcuts.ui.navigation

import com.kira.ui.core.navigation.Screen

sealed class ShortcutScreen(route: String) : Screen<String>(route) {

    class Edit(key: String) : ShortcutScreen("blacksquircle://settings/keybindings/edit?key=$key")
    class Conflict : ShortcutScreen("blacksquircle://settings/keybindings/conflict")
}