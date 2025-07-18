package com.kira.learning.module.supereditor.navigation

import com.kira.ui.core.navigation.Screen

sealed class AppScreen(route: String) : Screen<String>(route) {

    data object ConfirmExit : AppScreen("blacksquircle://exit")
}