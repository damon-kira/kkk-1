

package com.kira.ui.core.factory

import com.kira.ui.language.actionscript.ActionScriptLanguage
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

object FileAssociation {

    private val associations = HashMap<String, String>(72)

    init {
        associations[".as"] = ActionScriptLanguage.LANGUAGE_NAME
        associations[".c"] = CLanguage.LANGUAGE_NAME
        associations[".h"] = CLanguage.LANGUAGE_NAME
        associations[".cpp"] = CppLanguage.LANGUAGE_NAME
        associations[".hpp"] = CppLanguage.LANGUAGE_NAME
        associations[".ino"] = CppLanguage.LANGUAGE_NAME
        associations[".cs"] = CSharpLanguage.LANGUAGE_NAME
        associations[".css"] = CssLanguage.LANGUAGE_NAME
        associations[".scss"] = CssLanguage.LANGUAGE_NAME
        associations[".f90"] = FortranLanguage.LANGUAGE_NAME
        associations[".f95"] = FortranLanguage.LANGUAGE_NAME
        associations[".f03"] = FortranLanguage.LANGUAGE_NAME
        associations[".f08"] = FortranLanguage.LANGUAGE_NAME
        associations[".f"] = FortranLanguage.LANGUAGE_NAME
        associations[".for"] = FortranLanguage.LANGUAGE_NAME
        associations[".ftn"] = FortranLanguage.LANGUAGE_NAME
        associations[".go"] = GoLanguage.LANGUAGE_NAME
        associations[".groovy"] = GroovyLanguage.LANGUAGE_NAME
        associations[".gvy"] = GroovyLanguage.LANGUAGE_NAME
        associations[".gy"] = GroovyLanguage.LANGUAGE_NAME
        associations[".gsh"] = GroovyLanguage.LANGUAGE_NAME
        associations[".gradle"] = GroovyLanguage.LANGUAGE_NAME
        associations[".htm"] = HtmlLanguage.LANGUAGE_NAME
        associations[".html"] = HtmlLanguage.LANGUAGE_NAME
        associations[".ini"] = IniLanguage.LANGUAGE_NAME
        associations[".java"] = JavaLanguage.LANGUAGE_NAME
        associations[".js"] = JavaScriptLanguage.LANGUAGE_NAME
        associations[".jsx"] = JavaScriptLanguage.LANGUAGE_NAME
        associations[".mjs"] = JavaScriptLanguage.LANGUAGE_NAME
        associations[".cjs"] = JavaScriptLanguage.LANGUAGE_NAME
        associations[".json"] = JsonLanguage.LANGUAGE_NAME
        associations[".jl"] = JuliaLanguage.LANGUAGE_NAME
        associations[".kt"] = KotlinLanguage.LANGUAGE_NAME
        associations[".kts"] = KotlinLanguage.LANGUAGE_NAME
        associations[".tex"] = LatexLanguage.LANGUAGE_NAME
        associations[".lisp"] = LispLanguage.LANGUAGE_NAME
        associations[".lsp"] = LispLanguage.LANGUAGE_NAME
        associations[".cl"] = LispLanguage.LANGUAGE_NAME
        associations[".l"] = LispLanguage.LANGUAGE_NAME
        associations[".lua"] = LuaLanguage.LANGUAGE_NAME
        associations[".md"] = MarkdownLanguage.LANGUAGE_NAME
        associations[".php"] = PhpLanguage.LANGUAGE_NAME
        associations[".php3"] = PhpLanguage.LANGUAGE_NAME
        associations[".php4"] = PhpLanguage.LANGUAGE_NAME
        associations[".php5"] = PhpLanguage.LANGUAGE_NAME
        associations[".phps"] = PhpLanguage.LANGUAGE_NAME
        associations[".phtml"] = PhpLanguage.LANGUAGE_NAME
        associations[".txt"] = PlainTextLanguage.LANGUAGE_NAME
        associations[".log"] = PlainTextLanguage.LANGUAGE_NAME
        associations[".py"] = PythonLanguage.LANGUAGE_NAME
        associations[".pyw"] = PythonLanguage.LANGUAGE_NAME
        associations[".pyi"] = PythonLanguage.LANGUAGE_NAME
        associations[".rb"] = RubyLanguage.LANGUAGE_NAME
        associations[".rs"] = RustLanguage.LANGUAGE_NAME
        associations[".sh"] = ShellLanguage.LANGUAGE_NAME
        associations[".ksh"] = ShellLanguage.LANGUAGE_NAME
        associations[".bsh"] = ShellLanguage.LANGUAGE_NAME
        associations[".csh"] = ShellLanguage.LANGUAGE_NAME
        associations[".tcsh"] = ShellLanguage.LANGUAGE_NAME
        associations[".zsh"] = ShellLanguage.LANGUAGE_NAME
        associations[".bash"] = ShellLanguage.LANGUAGE_NAME
        associations[".smali"] = SmaliLanguage.LANGUAGE_NAME
        associations[".sql"] = SqlLanguage.LANGUAGE_NAME
        associations[".sqlite"] = SqlLanguage.LANGUAGE_NAME
        associations[".sqlite2"] = SqlLanguage.LANGUAGE_NAME
        associations[".sqlite3"] = SqlLanguage.LANGUAGE_NAME
        associations[".toml"] = TomlLanguage.LANGUAGE_NAME
        associations[".ts"] = TypeScriptLanguage.LANGUAGE_NAME
        associations[".tsx"] = TypeScriptLanguage.LANGUAGE_NAME
        associations[".mts"] = TypeScriptLanguage.LANGUAGE_NAME
        associations[".cts"] = TypeScriptLanguage.LANGUAGE_NAME
        associations[".vb"] = VisualBasicLanguage.LANGUAGE_NAME
        associations[".bas"] = VisualBasicLanguage.LANGUAGE_NAME
        associations[".cls"] = VisualBasicLanguage.LANGUAGE_NAME
        associations[".xhtml"] = XmlLanguage.LANGUAGE_NAME
        associations[".xht"] = XmlLanguage.LANGUAGE_NAME
        associations[".xml"] = XmlLanguage.LANGUAGE_NAME
        associations[".xaml"] = XmlLanguage.LANGUAGE_NAME
        associations[".xdf"] = XmlLanguage.LANGUAGE_NAME
        associations[".xmpp"] = XmlLanguage.LANGUAGE_NAME
        associations[".yaml"] = YamlLanguage.LANGUAGE_NAME
        associations[".yml"] = YamlLanguage.LANGUAGE_NAME
    }

    fun guessLanguage(extension: String): String {
        return associations[extension] ?: PlainTextLanguage.LANGUAGE_NAME
    }
}