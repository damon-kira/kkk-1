

package com.kira.ui.core.mvi

import android.content.Intent
import com.kira.ui.core.navigation.Screen

abstract class ViewEvent {

    data class Toast(val message: String) : ViewEvent()
    data class NewIntent(val intent: Intent) : ViewEvent()
    data class Navigation(val screen: Screen<*>) : ViewEvent()
    data class PopBackStack(val data: Any? = null) : ViewEvent()
}