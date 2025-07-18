

package com.kira.ui.feature.shortcuts.ui.navigation

import com.kira.ui.core.navigation.Screen

sealed class ShortcutScreen(route: String) : Screen<String>(route) {

    class Edit(key: String) : ShortcutScreen("kiralearning://settings/keybindings/edit?key=$key")
    class Conflict : ShortcutScreen("kiralearning://settings/keybindings/conflict")
}