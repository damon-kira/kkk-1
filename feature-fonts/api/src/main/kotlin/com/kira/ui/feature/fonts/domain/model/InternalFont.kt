

package com.kira.ui.feature.fonts.domain.model

enum class InternalFont(val font: FontModel) {
    DROID_SANS_MONO(
        font = FontModel(
            fontUuid = "droid_sans_mono",
            fontName = "Droid Sans Mono",
            fontPath = "file:///android_asset/fonts/droid_sans_mono.ttf",
            isExternal = false,
        )
    ),
    JETBRAINS_MONO(
        font = FontModel(
            fontUuid = "jetbrains_mono",
            fontName = "JetBrains Mono",
            fontPath = "file:///android_asset/fonts/jetbrains_mono.ttf",
            isExternal = false,
        )
    ),
    FIRA_CODE(
        font = FontModel(
            fontUuid = "fira_code",
            fontName = "Fira Code",
            fontPath = "file:///android_asset/fonts/fira_code.ttf",
            isExternal = false,
        )
    ),
    SOURCE_CODE_PRO(
        font = FontModel(
            fontUuid = "source_code_pro",
            fontName = "Source Code Pro",
            fontPath = "file:///android_asset/fonts/source_code_pro.ttf",
            isExternal = false,
        )
    ),
    ANONYMOUS_PRO(
        font = FontModel(
            fontUuid = "anonymous_pro",
            fontName = "Anonymous Pro",
            fontPath = "file:///android_asset/fonts/anonymous_pro.ttf",
            isExternal = false,
        )
    ),
    DEJAVU_SANS_MONO(
        font = FontModel(
            fontUuid = "dejavu_sans_mono",
            fontName = "DejaVu Sans Mono",
            fontPath = "file:///android_asset/fonts/dejavu_sans_mono.ttf",
            isExternal = false,
        )
    );

    companion object {

        fun find(path: String): FontModel? {
            return values().find { it.font.fontPath == path }?.font
        }
    }
}