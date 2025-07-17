

package com.kira.ui.core.extensions

import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

fun String.encodeUrl(encoding: String = "UTF-8"): String = URLEncoder.encode(this, encoding)
fun String.decodeUrl(encoding: String = "UTF-8"): String = URLDecoder.decode(this, encoding)

fun Gson.toJsonEncoded(any: Any): String = toJson(any).encodeUrl()