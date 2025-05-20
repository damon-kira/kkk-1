package com.kira.learning.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Configuration
import java.util.Locale
import androidx.core.content.edit

object LanguageUtils {

    fun applyLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        val resources = context.resources
        val config = Configuration(resources.configuration)

        // 1. 设置语言和布局方向
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        Locale.setDefault(locale)

        // 2. 应用新配置
        resources.updateConfiguration(config, resources.displayMetrics)

        // 3. 保存设置到SharedPreferences（可选）
        context.getSharedPreferences("AppSettings", MODE_PRIVATE)
            .edit() {
                putString("app_language", languageCode)
            }
    }

    fun attachBaseContext(context: Context): Context {
        val prefs = context.getSharedPreferences("AppSettings", MODE_PRIVATE)
        val langCode = prefs.getString("app_language", Locale.getDefault().language) ?: "en"
        return updateContextLocale(context, langCode)
    }

    private fun updateContextLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}