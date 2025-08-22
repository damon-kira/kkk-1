//import android.content.Context
//import android.graphics.Color
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.style.ForegroundColorSpan
//import android.util.AttributeSet
//import androidx.appcompat.widget.AppCompatEditText
//import com.steve28.stedit.highlighter.HighlightOption
//import com.steve28.stedit.highlighter.Highlighter
//import com.steve28.stedit.highlighter.Keyword
//
//class CodingField @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = android.R.attr.editTextStyle
//) : AppCompatEditText(context, attrs, defStyleAttr) {
//
//    // 高亮器配置
//    var highlighter: Highlighter? = null
//        set(value) {
//            field = value?.let {
//                object : Highlighter {
//                    override val keyword: List<Keyword> = it.keyword
//                    // 转换Compose颜色到Android颜色
//                    private fun convertColor(color: androidx.compose.ui.graphics.Color): Int {
//                        return Color.argb(
//                            (color.alpha * 255).toInt(),
//                            (color.red * 255).toInt(),
//                            (color.green * 255).toInt(),
//                            (color.blue * 255).toInt()
//                        )
//                    }
//
//                    override fun process(text: String, option: List<HighlightOption>): SpannableString {
//                        val spannable = SpannableString(text)
//                        var index = 0
//
//                        option.forEach { (start, end, color) ->
//                            if (index > start) return@forEach
//                            // 普通文本区间
//                            if (index < start) {
//                                spannable.setSpan(
//                                    ForegroundColorSpan(Color.WHITE),
//                                    index,
//                                    start,
//                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                                )
//                            }
//                            // 高亮区间
//                            spannable.setSpan(
//                                ForegroundColorSpan(convertColor(color)),
//                                start,
//                                end,
//                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                            )
//                            index = end
//                        }
//                        // 剩余文本
//                        if (index < text.length) {
//                            spannable.setSpan(
//                                ForegroundColorSpan(Color.WHITE),
//                                index,
//                                text.length,
//                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//                            )
//                        }
//                        return spannable
//                    }
//                }
//            }
//            applyHighlighting()
//        }
//
//    init {
//        // 初始化样式
//        setBackgroundColor(Color.parseColor("#002240"))
//        setTextColor(Color.WHITE)
//
//        // 文本变化监听
//        addTextChangedListener(object : android.text.TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: android.text.Editable?) {
//                applyHighlighting()
//            }
//        })
//    }
//
//    private fun applyHighlighting() {
//        highlighter?.let { hl ->
//            val text = text.toString()
//            val processed = hl.process(text, hl.getHighlightOption(text, hl.keyword))
//            if (text != processed.toString()) {
//                val selection = selectionEnd
//                setText(processed)
//                setSelection(selection.coerceIn(0, processed.length))
//            }
//        }
//    }
//}