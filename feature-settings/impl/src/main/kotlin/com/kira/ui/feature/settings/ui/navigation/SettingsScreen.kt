

package com.kira.ui.feature.settings.ui.navigation

import com.kira.ui.core.navigation.Screen

sealed class SettingsScreen(route: String) : Screen<String>(route) {

    data object Application : SettingsScreen("kiralearning://settings/application")
    data object Editor : SettingsScreen("kiralearning://settings/editor")
    data object CodeStyle : SettingsScreen("kiralearning://settings/codestyle")
    data object Files : SettingsScreen("kiralearning://settings/files")
    data object Keybindings : SettingsScreen("kiralearning://settings/keybindings")
    data object Cloud : SettingsScreen("kiralearning://settings/cloud")
    data object About : SettingsScreen("kiralearning://settings/about")
    data object ChangeLog : SettingsScreen("kiralearning://settings/about/changelog")
}