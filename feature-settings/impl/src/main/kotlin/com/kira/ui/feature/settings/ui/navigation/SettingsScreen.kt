

package com.kira.ui.feature.settings.ui.navigation

import com.kira.ui.core.navigation.Screen

sealed class SettingsScreen(route: String) : Screen<String>(route) {

    data object Application : SettingsScreen("blacksquircle://settings/application")
    data object Editor : SettingsScreen("blacksquircle://settings/editor")
    data object CodeStyle : SettingsScreen("blacksquircle://settings/codestyle")
    data object Files : SettingsScreen("blacksquircle://settings/files")
    data object Keybindings : SettingsScreen("blacksquircle://settings/keybindings")
    data object Cloud : SettingsScreen("blacksquircle://settings/cloud")
    data object About : SettingsScreen("blacksquircle://settings/about")
    data object ChangeLog : SettingsScreen("blacksquircle://settings/about/changelog")
}