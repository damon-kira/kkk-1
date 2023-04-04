package com.colombia.credit.view.baseinfo

import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.colombia.credit.R
import com.util.lib.show

interface IBaseInfoView {

    /***
     * 设置输入框输入长度
     */
    fun setInputMaxLength(maxLength: Int)
    /**
     * 设置输入框输入类型
     */
    fun setFilters(filters: Array<InputFilter>)

    /**
     * 设置title 输入框中的提示文案
     */
    fun setEditHint(hint: String)

    fun setTitle(title: String)

    fun setTitle(@StringRes strRes: Int)

    fun setTitleVisible(@BaseInfoVisibility visible: Int)

    fun getTitle(): String

    /**
     * 设置是否可以编辑
     * 不可编辑显示TextView（显示or选择）
     * 可编辑EditText
     */
    fun setCanEdit(canEdit: Boolean, @ColorRes textColor: Int = R.color.color_1D1724)

    /**
     * 设置输入框里的内容
     */
    fun setViewText(text: String)

    /**
     * 获取输入框里面的内容
     */
    fun getViewText(): String

    /**
     * 获取输入框
     */
    fun getEditViewText(): String

    /**
     * 获取输入框
     */
    fun getEditView(): EditText?

    /**
     * 设置EditText错误的文案
     * @param errorStr String 错误的提示文案
     * @param hasIcon 是否要错误的提示icon
     */
    fun setError(errorStr: String, hasIcon: Boolean = true)


    /**
     * 设置EditText提示文案
     * @param errorStr String 提示文案
     * @param hasIcon 是否要错误的提示icon
     */
    fun setDesc(desc: String)


    /**
     * 设置EditText错误的文案
     * @param errorStr String 错误的提示文案
     * @param hasIcon 是否要错误的提示icon
     */
    fun setError(@StringRes errorRes: Int, hasIcon: Boolean = true)
    /**
     *  清除TextView的Error提示信息
     */
    fun clearTextError()

    /**
     * 是否有错误
     */
    fun isError(): Boolean

    fun setInputType(@BaseInfoInputType inputType: Int)

    fun setMaxLine(maxLine: Int)

    fun setRightIconVisiblity(@BaseInfoVisibility visible: Int)

    fun setRightIcon(drawable: Drawable)

    fun setRightIcon(@DrawableRes resId: Int)

    fun setLeftIcon(drawable: Drawable)

    fun setLeftIcon(@DrawableRes resId: Int)

    fun setLeftIconVisiblity(@BaseInfoVisibility visible: Int)

    /**
     * 获取ViewText用来显示PopWindow的Title
     * @return CharSequence 如果getViewText()返回空；那么返回灰色（989898）Title
     */
    fun getViewTextForShow(): CharSequence

    /**
     * @param text
     * @param hideText 隐藏显示的文本
     * */
    fun setViewText(text: String, hideText: String)

    fun setTextArray(array: ArrayList<String>)
}