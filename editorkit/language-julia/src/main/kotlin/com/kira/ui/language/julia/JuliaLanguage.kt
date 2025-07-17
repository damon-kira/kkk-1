

package com.kira.ui.language.julia

import com.kira.ui.language.base.Language
import com.kira.ui.language.base.parser.LanguageParser
import com.kira.ui.language.base.provider.SuggestionProvider
import com.kira.ui.language.base.styler.LanguageStyler
import com.kira.ui.language.julia.parser.JuliaParser
import com.kira.ui.language.julia.provider.JuliaProvider
import com.kira.ui.language.julia.styler.JuliaStyler

class JuliaLanguage : Language {

    companion object {
        const val LANGUAGE_NAME = "julia"
    }

    override val languageName = LANGUAGE_NAME

    override fun getParser(): LanguageParser {
        /*try {
            // System.loadLibrary("julia-internal");
            // System.loadLibrary("julia");
            // System.loadLibrary("rustc_driver");  // err
        } catch (ex: Exception){
            ex.printStackTrace();
        }*/
        return JuliaParser.getInstance()
    }

    override fun getProvider(): SuggestionProvider {
        return JuliaProvider.getInstance()
    }

    override fun getStyler(): LanguageStyler {
        return JuliaStyler.getInstance()
    }
}