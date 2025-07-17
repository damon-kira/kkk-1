

package com.kira.ui.feature.themes.data.utils

import android.content.Context
import java.io.BufferedReader

fun Context.readAssetFileText(assetPath: String): String {
    val inputStream = assets.open(assetPath)
    return inputStream.bufferedReader().use(BufferedReader::readText)
}