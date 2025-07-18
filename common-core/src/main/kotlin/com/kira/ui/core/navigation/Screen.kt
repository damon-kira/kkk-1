

package com.kira.ui.core.navigation

abstract class Screen<T>(val route: T) {
    object Settings : Screen<String>("kiralearning://settings")
    object Fonts : Screen<String>("kiralearning://fonts")
    object Themes : Screen<String>("kiralearning://themes")
    object AddServer : Screen<String>("kiralearning://settings/cloud/add")
}