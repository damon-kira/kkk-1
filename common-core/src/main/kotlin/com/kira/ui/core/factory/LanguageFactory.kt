

package com.kira.ui.core.factory

import com.kira.ui.language.actionscript.ActionScriptLanguage
import com.kira.ui.language.base.Language
import com.kira.ui.language.c.CLanguage
import com.kira.ui.language.cpp.CppLanguage
import com.kira.ui.language.csharp.CSharpLanguage
import com.kira.ui.language.css.CssLanguage
import com.kira.ui.language.fortran.FortranLanguage
import com.kira.ui.language.go.GoLanguage
import com.kira.ui.language.groovy.GroovyLanguage
import com.kira.ui.language.html.HtmlLanguage
import com.kira.ui.language.ini.IniLanguage
import com.kira.ui.language.java.JavaLanguage
import com.kira.ui.language.javascript.JavaScriptLanguage
import com.kira.ui.language.json.JsonLanguage
import com.kira.ui.language.julia.JuliaLanguage
import com.kira.ui.language.kotlin.KotlinLanguage
import com.kira.ui.language.latex.LatexLanguage
import com.kira.ui.language.lisp.LispLanguage
import com.kira.ui.language.lua.LuaLanguage
import com.kira.ui.language.markdown.MarkdownLanguage
import com.kira.ui.language.php.PhpLanguage
import com.kira.ui.language.plaintext.PlainTextLanguage
import com.kira.ui.language.python.PythonLanguage
import com.kira.ui.language.ruby.RubyLanguage
import com.kira.ui.language.rust.RustLanguage
import com.kira.ui.language.shell.ShellLanguage
import com.kira.ui.language.smali.SmaliLanguage
import com.kira.ui.language.sql.SqlLanguage
import com.kira.ui.language.toml.TomlLanguage
import com.kira.ui.language.typescript.TypeScriptLanguage
import com.kira.ui.language.visualbasic.VisualBasicLanguage
import com.kira.ui.language.xml.XmlLanguage
import com.kira.ui.language.yaml.YamlLanguage

object LanguageFactory {

    fun create(fileName: String): Language {
        val extension = '.' + fileName.substringAfterLast('.')
        val languageName = FileAssociation.guessLanguage(extension)
        return fromName(languageName)
    }

    fun fromName(languageName: String): Language {
        return when (languageName) {
            ActionScriptLanguage.LANGUAGE_NAME -> ActionScriptLanguage()
            CLanguage.LANGUAGE_NAME -> CLanguage()
            CppLanguage.LANGUAGE_NAME -> CppLanguage()
            CSharpLanguage.LANGUAGE_NAME -> CSharpLanguage()
            CssLanguage.LANGUAGE_NAME -> CssLanguage()
            FortranLanguage.LANGUAGE_NAME -> FortranLanguage()
            GoLanguage.LANGUAGE_NAME -> GoLanguage()
            GroovyLanguage.LANGUAGE_NAME -> GroovyLanguage()
            HtmlLanguage.LANGUAGE_NAME -> HtmlLanguage()
            IniLanguage.LANGUAGE_NAME -> IniLanguage()
            JavaLanguage.LANGUAGE_NAME -> JavaLanguage()
            JavaScriptLanguage.LANGUAGE_NAME -> JavaScriptLanguage()
            JsonLanguage.LANGUAGE_NAME -> JsonLanguage()
            JuliaLanguage.LANGUAGE_NAME -> JuliaLanguage()
            KotlinLanguage.LANGUAGE_NAME -> KotlinLanguage()
            LatexLanguage.LANGUAGE_NAME -> LatexLanguage()
            LispLanguage.LANGUAGE_NAME -> LispLanguage()
            LuaLanguage.LANGUAGE_NAME -> LuaLanguage()
            MarkdownLanguage.LANGUAGE_NAME -> MarkdownLanguage()
            PhpLanguage.LANGUAGE_NAME -> PhpLanguage()
            PlainTextLanguage.LANGUAGE_NAME -> PlainTextLanguage()
            PythonLanguage.LANGUAGE_NAME -> PythonLanguage()
            RubyLanguage.LANGUAGE_NAME -> RubyLanguage()
            RustLanguage.LANGUAGE_NAME -> RustLanguage()
            ShellLanguage.LANGUAGE_NAME -> ShellLanguage()
            SmaliLanguage.LANGUAGE_NAME -> SmaliLanguage()
            SqlLanguage.LANGUAGE_NAME -> SqlLanguage()
            TomlLanguage.LANGUAGE_NAME -> TomlLanguage()
            TypeScriptLanguage.LANGUAGE_NAME -> TypeScriptLanguage()
            VisualBasicLanguage.LANGUAGE_NAME -> VisualBasicLanguage()
            XmlLanguage.LANGUAGE_NAME -> XmlLanguage()
            YamlLanguage.LANGUAGE_NAME -> YamlLanguage()
            else -> PlainTextLanguage()
        }
    }
}