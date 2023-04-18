package com.colombia.credit.view.baseinfo

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.colombia.credit.R


/**
 * 基本信息和其他信息页面 条目view
 */
abstract class AbsBaseInfoView : LinearLayout, IBaseInfoView {

    companion object {
        const val VISIBLE = BaseInfoVisibility.VISIBLE
        const val GONE = BaseInfoVisibility.GONE
        const val INVISIBLE = BaseInfoVisibility.INVISIBLE
    }

    protected var mIsError = false

    protected var maxLength = -1

    protected var mWaringHasIcon: Boolean = true
    protected var mWaringColor = ContextCompat.getColor(context, R.color.color_999999)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    open fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        orientation = VERTICAL
        getAttrs(context, attrs)
        isSaveFromParentEnabled = false
    }

    @SuppressLint("CustomViewStyleable", "Recycle")
    private fun getAttrs(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.BaseInfoView)?.also { ta ->
            val indexCount = ta.indexCount
            var attr: Int
            for (i in 0 until indexCount) {
                attr = ta.getIndex(i)
                when (attr) {
                    R.styleable.BaseInfoView_title -> {
                        val title = ta.getString(attr).orEmpty()
                        setTitle(title)
                    }
                    R.styleable.BaseInfoView_title_visiblity -> {
                        val visible = ta.getInt(attr, VISIBLE)
                        setTitleVisible(visible)
                    }
                    R.styleable.BaseInfoView_hint -> {
                        val hint = ta.getString(attr)
                        setEditHint(hint.orEmpty())
                    }

                    R.styleable.BaseInfoView_left_icon -> {
                        val icon = ta.getResourceId(attr, -1)
                        if (icon != -1) {
                            setLeftIcon(icon)
                        }
                    }
                    R.styleable.BaseInfoView_right_icon -> {
                        val icon = ta.getResourceId(attr, -1)
                        if (icon != -1) {
                            setRightIcon(icon)
                        }
                    }
                    R.styleable.BaseInfoView_max_length -> {
                        val maxLength = ta.getInt(attr, -1)
                        if (maxLength <= 0) return
                        setInputMaxLength(maxLength)
                    }
                    R.styleable.BaseInfoView_max_lines -> {
                        val maxLine = ta.getInt(attr, 1)
                        setMaxLine(maxLine)
                    }
                    R.styleable.BaseInfoView_if_can_edit -> {
                        val canEdit = ta.getBoolean(attr, true)
                        setCanEdit(canEdit)
                    }
                    R.styleable.BaseInfoView_inputType -> {
                        val inputType = ta.getInt(attr, BaseInfoInputType.INPUT_NORMAL)
                        setInputType(inputType)
                    }
                    R.styleable.BaseInfoView_leftIcon_visiblity -> {
                        val visible = ta.getInt(attr, GONE)
                        setLeftIconVisiblity(visible)
                    }
                    R.styleable.BaseInfoView_rightIcon_visiblity -> {
                        val visible = ta.getInt(attr, GONE)
                        setRightIconVisiblity(visible)
                    }
                    R.styleable.BaseInfoView_desc_color -> {
                        mWaringColor = ta.getColor(attr, mWaringColor)
                        setDescColor(mWaringColor)
                    }
                    R.styleable.BaseInfoView_desc_has_icon -> {
                        mWaringHasIcon = ta.getBoolean(attr, true)
                    }
                    R.styleable.BaseInfoView_desc_text -> {
                        val desc = ta.getString(attr).orEmpty()
                        if (desc.isNotEmpty()) {
                            setDesc(desc)
                        }
                    }
                    R.styleable.BaseInfoView_stringArray -> {
                        val textArray = ta.getTextArray(attr)
                        val arr = ArrayList<String>()
                        textArray.forEach {
                            arr.add(it.toString())
                        }
                        setTextArray(arr)
                    }
                    R.styleable.BaseInfoView_digits -> {
                        ta.getString(attr)?.let {digits ->
                            setDigits(digits)
                        }
                    }
                }
            }
            ta.recycle()
        }
    }
}