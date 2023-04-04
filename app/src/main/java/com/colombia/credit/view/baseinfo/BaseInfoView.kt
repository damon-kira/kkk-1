package com.colombia.credit.view.baseinfo

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.colombia.credit.R
import com.colombia.credit.databinding.LayoutInfoViewBinding
import com.common.lib.expand.setBlockingOnClickListener
import com.util.lib.dip2px
import com.util.lib.show
import java.util.*
import kotlin.collections.ArrayList


/**
 * 基本信息和其他信息页面 条目view
 */
class BaseInfoView : AbsBaseInfoView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private lateinit var mBinding: LayoutInfoViewBinding

    override fun init(context: Context?, attrs: AttributeSet?) {
        context ?: return
        mBinding = LayoutInfoViewBinding.inflate(LayoutInflater.from(context), this)
        super.init(context, attrs)
        mBinding.bivEdittext.addTextChangedListener(onTextChanged = { text, start, count, after ->
            clearTextError()
        })
        mBinding.bivEdittext.isLongClickable = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null || mBinding.bivEdittext.isEnabled) {
            return super.onInterceptTouchEvent(ev)
        } else if (!mBinding.bivAivIconRight.isClickable) {
            return true
        }
        val offset = 10
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP, MotionEvent.ACTION_MOVE -> {
                val x = ev.x
                val y = ev.y
                if (x >= mBinding.bivAivIconRight.left - offset && x <= mBinding.bivAivIconRight.right + offset &&
                    y >= mBinding.bivAivIconRight.top - offset && y <= mBinding.bivAivIconRight.bottom + offset
                ) {
                    return super.onInterceptTouchEvent(ev)
                }
            }
        }
        return true
    }

    override fun setInputType(@BaseInfoInputType inputType: Int) {
        mBinding.bivEdittext.inputType = when (inputType) {
            BaseInfoInputType.INPUT_NUMBER -> InputType.TYPE_CLASS_NUMBER
            BaseInfoInputType.INPUT_EMAIL ->
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            BaseInfoInputType.INPUT_PWD ->
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            else ->
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_CLASS_TEXT
        }
    }

    /** 设置editText可以多行显示 */
    private fun addLinesInputType() {
        val originInputType = mBinding.bivEdittext.inputType
        mBinding.bivEdittext.inputType = if (originInputType == InputType.TYPE_NULL) {
            InputType.TYPE_TEXT_FLAG_MULTI_LINE
        } else {
            originInputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
    }

    override fun setMaxLine(maxLine: Int) {
        if (maxLine <= 0) {
            addLinesInputType()
            return
        }
        mBinding.bivEdittext.maxLines = maxLine
        mBinding.bivEdittext.ellipsize = TextUtils.TruncateAt.END
        if (maxLine > 1) {
            addLinesInputType()
        }
    }

    override fun setRightIconVisiblity(@BaseInfoVisibility visible: Int) {
        mBinding.bivAivIconRight.visibility = BaseInfoVisibility.getViewVisible(visible)
    }

    override fun setRightIcon(drawable: Drawable) {
        setRightIconVisiblity(VISIBLE)
        mBinding.bivAivIconRight.setImageDrawable(drawable)
    }

    override fun setRightIcon(@DrawableRes resId: Int) {
        (AppCompatResources.getDrawable(context, resId))?.let {
            setRightIcon(it)
        }
    }

    override fun setLeftIcon(drawable: Drawable) {
        mBinding.bivAivIconLeft.show()
        mBinding.bivAivIconLeft.setImageDrawable(drawable)
    }

    override fun setLeftIcon(@DrawableRes resId: Int) {
        (AppCompatResources.getDrawable(context, resId))?.let {
            setLeftIcon(it)
        }
    }

    override fun setLeftIconVisiblity(@BaseInfoVisibility visible: Int) {
        mBinding.bivAivIconLeft.visibility = BaseInfoVisibility.getViewVisible(visible)
    }


    override fun setEditHint(hint: String) {
        mBinding.bivEdittext.hint = hint
    }

    override fun setTitle(@StringRes titleRes: Int) {
        mBinding.bivTvTitle.show()
        mBinding.bivTvTitle.setText(titleRes)
    }

    override fun setTitle(title: String) {
        mBinding.bivTvTitle.show()
        mBinding.bivTvTitle.text = title
    }

    override fun setTitleVisible(visible: Int) {
        mBinding.bivTvTitle.visibility = BaseInfoVisibility.getViewVisible(visible)
    }

    override fun getTitle(): String {
        return mBinding.bivTvTitle.text.toString()
    }

    override fun setInputMaxLength(maxLength: Int) {
        this.maxLength = maxLength
        mBinding.bivEdittext.filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }


    override fun setFilters(filters: Array<InputFilter>) {
        var filterArray = filters
        if (maxLength > 0) { // 如果设置的有长度限制，需要添加上长度的filter
            var lengthFilterIndex = -1
            // 是否包含长度的filter
            filterArray.forEachIndexed { index, inputFilter ->
                if (inputFilter is InputFilter.LengthFilter) {
                    lengthFilterIndex = index
                    return@forEachIndexed
                }
            }
            if (lengthFilterIndex < 0) {
                filterArray = Arrays.copyOf(filters, filters.size + 1)
                filterArray[filterArray.size - 1] = InputFilter.LengthFilter(maxLength)
            }
        }
        mBinding.bivEdittext.filters = filterArray
    }

    fun setRightIconClickListener(click: (view: View) -> Unit) {
        if (mBinding.bivAivIconRight.drawable == null) return
        mBinding.bivAivIconRight.setBlockingOnClickListener(click)
    }

    fun setRightIconClickListener(l: OnClickListener) {
        if (mBinding.bivAivIconRight.drawable == null) return
        mBinding.bivAivIconRight.setBlockingOnClickListener(l)
    }

    fun setRightClickable(clickable: Boolean) {
        mBinding.bivAivIconRight.isEnabled = clickable
    }

    /**
     * 获取ViewText用来显示PopWindow的Title
     * @return CharSequence 如果getViewText()返回空；那么返回灰色（989898）Title
     */
    override fun getViewTextForShow(): CharSequence {
        var text: CharSequence = getViewText()
        if (text.isEmpty()) {
            text = mBinding.bivEdittext.hint
        }
        if (text.isNotEmpty()) {
            val spannable = SpannableStringBuilder(text)
            spannable.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_FF989898)),
                0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            text = spannable
        }
        return text
    }

    override fun getViewText(): String {
        return mBinding.bivEdittext.getRealText()
    }

    override fun setViewText(text: String) {
        mBinding.bivEdittext.setText(text)
        clearTextError()
    }

    /**
     * @param text
     * @param hideText 隐藏显示的文本
     * */
    override fun setViewText(text: String, hideText: String) {
        mBinding.bivEdittext.setText(text, hideText)
        clearTextError()
    }

    override fun getEditView(): EditText = mBinding.bivEdittext

    override fun getEditViewText(): String {
        return mBinding.bivEdittext.text.toString()
    }

    override fun setCanEdit(canEdit: Boolean, textColor: Int) {
        mBinding.bivEdittext.isEnabled = canEdit
    }

    override fun setError(errorStr: String, hasIcon: Boolean) {
        mIsError = true
        val errorSpannable = SpannableStringBuilder(errorStr)
//        if (hasIcon)
//            errorSpannable.insert(0, errorImageSpan)
        mBinding.bivTvError.show()
        mBinding.bivTvError.text = errorSpannable
        mBinding.bivEdittext.isSelected = true
    }

    override fun setError(errorRes: Int, hasIcon: Boolean) {
        setError(context.resources.getString(errorRes), hasIcon)
    }

    override fun setDesc(desc: String) {
        mBinding.bivTvDesc.show()
        mBinding.bivTvDesc.text = desc
    }


    override fun clearTextError() {
        if (!mIsError) return
        mIsError = false
        mBinding.bivTvError.visibility = View.GONE
        mBinding.bivEdittext.isSelected = false
    }

    override fun isError(): Boolean = mIsError

    override fun setTextArray(array: ArrayList<String>) {
    }

    /**
     * 用户更新BaseInfoView的PaddingTop
     * paddingLeft 16dp; paddingRight 16dp; paddingBottom 8dp, paddingTop 有title10dp, 没有0dp
     * @param paddingTop Int 默认10dp，当需要作为附属控件时（没有Title），paddingTop应设置为0dp
     */
    private fun updatePaddingTop(
        paddingTop: Int = dip2px(
            context,
            10f
        )
    ) {
        val padding = dip2px(context, 16f)
        setPadding(padding, paddingTop, padding, padding / 2)
    }

}