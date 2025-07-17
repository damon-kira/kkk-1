

package com.kira.ui.feature.editor.data.utils

import com.kira.ui.feature.fonts.domain.model.FontModel
import com.kira.ui.feature.settings.domain.model.KeyModel
import com.kira.ui.feature.shortcuts.domain.model.Keybinding
import com.kira.ui.feature.themes.domain.model.ThemeModel

sealed class SettingsEvent<T>(val value: T) {
    class ColorScheme(value: ThemeModel) : SettingsEvent<ThemeModel>(value)
    class FontSize(value: Float) : SettingsEvent<Float>(value)
    class FontType(value: FontModel) : SettingsEvent<FontModel>(value)

    class WordWrap(value: Boolean) : SettingsEvent<Boolean>(value)
    class CodeCompletion(value: Boolean) : SettingsEvent<Boolean>(value)

    // class ErrorHighlight(value: Boolean) : SettingsEvent<Boolean>(value)
    class PinchZoom(value: Boolean) : SettingsEvent<Boolean>(value)
    class LineNumbers(value: Pair<Boolean, Boolean>) :
        SettingsEvent<Pair<Boolean, Boolean>>(value)
    class Delimiters(value: Boolean) : SettingsEvent<Boolean>(value)
    class ReadOnly(value: Boolean) : SettingsEvent<Boolean>(value)
    class KeyboardPreset(value: List<KeyModel>) : SettingsEvent<List<KeyModel>>(value)
    class SoftKeys(value: Boolean) : SettingsEvent<Boolean>(value)

    class AutoIndentation(value: Triple<Boolean, Boolean, Boolean>) :
        SettingsEvent<Triple<Boolean, Boolean, Boolean>>(value)
    class UseSpacesNotTabs(value: Boolean) : SettingsEvent<Boolean>(value)
    class TabWidth(value: Int) : SettingsEvent<Int>(value)

    class Keybindings(value: List<Keybinding>) : SettingsEvent<List<Keybinding>>(value)
}