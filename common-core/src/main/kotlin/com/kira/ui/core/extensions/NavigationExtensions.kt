

package com.kira.ui.core.extensions

import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.kira.ui.core.navigation.Screen

fun NavController.navigate(
    screen: Screen<*>,
    options: NavOptions? = null,
    extras: Navigator.Extras? = null,
) {
    when (screen.route) {
        is String -> navigate(
            deepLink = screen.route.toUri(),
            navOptions = options,
            navigatorExtras = extras
        )
        is NavDirections -> navigate(
            resId = screen.route.actionId,
            args = screen.route.arguments,
            navOptions = options,
            navigatorExtras = extras
        )
        is Int -> navigate(
            resId = screen.route,
            args = null,
            navOptions = options,
            navigatorExtras = extras
        )
        else -> throw IllegalArgumentException("Can't handle route type")
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Fragment> FragmentManager.fragment(@IdRes id: Int): T? {
    return findFragmentById(id) as? T
}