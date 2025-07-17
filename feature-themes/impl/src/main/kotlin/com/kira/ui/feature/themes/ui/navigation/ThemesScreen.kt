

package com.kira.ui.feature.themes.ui.navigation

import com.kira.ui.core.extensions.encodeUrl
import com.kira.ui.core.navigation.Screen

sealed class ThemesScreen(route: String) : Screen<String>(route) {

    data object Create : ThemesScreen("blacksquircle://themes/create")

    class Update(uuid: String?) : ThemesScreen("blacksquircle://themes/update?uuid=$uuid")

    class ChooseColor(key: String, value: String) : ThemesScreen(
        route = "blacksquircle://themes/choosecolor?key=$key&value=${value.encodeUrl()}"
    )
}