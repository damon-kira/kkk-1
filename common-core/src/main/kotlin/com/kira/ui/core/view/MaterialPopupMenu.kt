

package com.kira.ui.core.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.MenuCompat
import com.kira.ui.core.extensions.makeRightPaddingRecursively
import com.google.android.material.R

@SuppressLint("RestrictedApi")
class MaterialPopupMenu(private val context: Context) {

    val menu: Menu
        get() = menuBuilder

    private val menuBuilder: MenuBuilder
    private var menuListener: PopupMenu.OnMenuItemClickListener? = null

    init {
        menuBuilder = MenuBuilder(context).apply {
            setCallback(object : MenuBuilder.Callback {
                override fun onMenuModeChange(menu: MenuBuilder) = Unit
                override fun onMenuItemSelected(menu: MenuBuilder, item: MenuItem): Boolean {
                    return menuListener?.onMenuItemClick(item) ?: false
                }
            })
        }
    }

    fun setOnMenuItemClickListener(listener: PopupMenu.OnMenuItemClickListener) {
        this.menuListener = listener
    }

    fun inflate(@MenuRes menuRes: Int) {
        SupportMenuInflater(context).inflate(menuRes, menuBuilder)
        MenuCompat.setGroupDividerEnabled(menuBuilder, true)
        menuBuilder.makeRightPaddingRecursively()
    }

    fun show(anchorView: View) {
        val popupMenu = MenuPopupHelper(
            context, menuBuilder, anchorView, true, R.attr.actionOverflowMenuStyle
        )
        popupMenu.show()
    }
}