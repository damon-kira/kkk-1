package com.kira.ui.feature.editor.ui.navigation

import com.kira.ui.core.extensions.encodeUrl
import com.kira.ui.core.navigation.Screen

sealed class EditorScreen(route: String) : Screen<String>(route) {

    class ForceSyntaxDialog(languageName: String) : EditorScreen(
        route = "kiralearning://editor/syntax?languageName=${languageName.encodeUrl()}",
    )

    class CloseModifiedDialog(position: Int, fileName: String) : EditorScreen(
        route = "kiralearning://editor/close?position=$position&fileName=${fileName.encodeUrl()}",
    )

    data object GotoLine : EditorScreen("kiralearning://editor/goto")
    data object InsertColor : EditorScreen("kiralearning://editor/insertcolor")
}