

package com.kira.ui.feature.themes.ui.customview

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import com.kira.ui.editorkit.model.ColorScheme
import com.kira.ui.editorkit.model.StyleSpan
import com.kira.ui.editorkit.model.SyntaxHighlightSpan
import com.kira.ui.language.base.Language
import com.kira.ui.language.base.model.TextStructure
import com.kira.ui.language.base.model.TokenType

class CodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun syntaxHighlight(
        text: CharSequence,
        language: Language,
        colorScheme: ColorScheme,
    ) {
        if (layout == null) {
            return
        }
        val structure = TextStructure(SpannableStringBuilder(text))
        val results = language.getStyler().execute(structure)
        val currentText = text.toSpannable()
        for (result in results) {
            currentText.setSpan(
                SyntaxHighlightSpan(
                    StyleSpan(
                        color = when (result.tokenType) {
                            TokenType.NUMBER -> colorScheme.numberColor
                            TokenType.OPERATOR -> colorScheme.operatorColor
                            TokenType.KEYWORD -> colorScheme.keywordColor
                            TokenType.TYPE -> colorScheme.typeColor
                            TokenType.LANG_CONST -> colorScheme.langConstColor
                            TokenType.PREPROCESSOR -> colorScheme.preprocessorColor
                            TokenType.VARIABLE -> colorScheme.variableColor
                            TokenType.METHOD -> colorScheme.methodColor
                            TokenType.STRING -> colorScheme.stringColor
                            TokenType.COMMENT -> colorScheme.commentColor
                            TokenType.TAG -> colorScheme.tagColor
                            TokenType.TAG_NAME -> colorScheme.tagNameColor
                            TokenType.ATTR_NAME -> colorScheme.attrNameColor
                            TokenType.ATTR_VALUE -> colorScheme.attrValueColor
                            TokenType.ENTITY_REF -> colorScheme.entityRefColor
                        }
                    )
                ),
                result.start,
                result.end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        setText(currentText)
    }
}