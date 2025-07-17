

package com.kira.ui.core.navigation

interface BackPressedHandler {
    /**
     * Вернёт true если событие было обработано дочерним фрагментом.
     */
    fun handleOnBackPressed(): Boolean
}