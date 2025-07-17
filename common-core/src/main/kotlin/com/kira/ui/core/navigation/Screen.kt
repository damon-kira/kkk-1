

package com.kira.ui.core.navigation

abstract class Screen<T>(val route: T) {
    object Settings : Screen<String>("blacksquircle://settings")
    object Fonts : Screen<String>("blacksquircle://fonts")
    object Themes : Screen<String>("blacksquircle://themes")
    object AddServer : Screen<String>("blacksquircle://settings/cloud/add")
}