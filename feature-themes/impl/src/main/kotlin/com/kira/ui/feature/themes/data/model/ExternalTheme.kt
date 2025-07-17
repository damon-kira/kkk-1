

package com.kira.ui.feature.themes.data.model

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class ExternalTheme(
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("author")
    val author: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("color_scheme")
    val externalScheme: ExternalScheme?,
) {

    companion object {

        private val GSON_SERIALIZER = GsonBuilder()
            .setPrettyPrinting()
            .create()

        fun serialize(externalTheme: ExternalTheme): String {
            return GSON_SERIALIZER.toJson(externalTheme)
        }

        fun deserialize(themeJson: String): ExternalTheme {
            return GSON_SERIALIZER.fromJson(themeJson, ExternalTheme::class.java)
        }
    }
}