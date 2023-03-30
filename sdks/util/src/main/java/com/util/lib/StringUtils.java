package com.util.lib;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.StringRes;

public class StringUtils {

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param textId
     * @param color
     * @return
     */
    public static SpannableString getSpannableString(Context context, int textId, int color) {
        String text = context.getString(textId);
        return StringUtils.getSpannableString(context, text, color);
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param text
     * @param color
     * @return
     */
    public static SpannableString getSpannableString(Context context, String text, int color) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param textId
     * @param color
     * @param object
     * @return
     */
    public static SpannableString getSpannableString(Context context, int textId, @ColorRes int color, String... object) {
        String text = context.getString(textId, (Object[]) object);
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        SpannableString msp = new SpannableString(text);
        try {
            int length = object.length;
            int originIndex = 0;
            for (int i = 0; i < length; i++) {
                int index = text.indexOf(object[i]);
                originIndex+=index;
                text = text.substring(index + object[i].length());
                msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), originIndex, originIndex
                        + object[i].length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                originIndex+=object[i].length();
            }
        } catch (Exception e) {
            return new SpannableString(text);
        }
        return msp;
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param textId
     * @param color
     * @param object
     * @return
     */
    public static SpannableString getSpannableString(Context context,@StringRes int textId,@ColorRes int color, String object) {
        String text = context.getString(textId, object);
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        int index = text.indexOf(object);
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), index, index + object.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param textId
     * @param color
     * @param dimenSize
     * @param object
     * @return
     */
    public static SpannableString getSpannableString(Context context, @StringRes int textId, @ColorRes int color, @DimenRes int dimenSize,
                                                     String object) {
        String text = context.getString(textId, object);
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        int size = context.getResources().getDimensionPixelSize(dimenSize);
        int index = text.indexOf(object);
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new AbsoluteSizeSpan(size), index, index + object.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), index, index + object.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSpannableString(Context context, String target, @ColorRes int color, String format) {
        SpannableString msp = new SpannableString(target);
        if (TextUtils.isEmpty(format)) {
            return msp;
        }
        int start = target.indexOf(format);
        if (start < 0) {
            return msp;
        }
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, start + format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }


    /**
     * 多字符颜色修改
     * @param context
     * @param target 目标字符串
     * @param color 标注的颜色
     * @param objects 需要标注的字符
     * @return
     */
    public static SpannableString getSpannableString(Context context, String target, @ColorRes int color, Object... objects) {
        SpannableString msp = new SpannableString(target);
        if (objects == null || objects.length <= 0) {
            return msp;
        }
        int start;

        for (Object item : objects) {
            if (item instanceof String) {
                String format = (String) item;
                start = target.indexOf(format);
                if (start == -1) continue;
                msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, start + format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return msp;
    }

    public static SpannableString getSpannableStringSize(Context context, String target, @DimenRes int size, int start, int end) {
        return getSpannableStringSize(target, context.getResources().getDimensionPixelSize(size), start, end);
    }


    public static SpannableString getSpannableStringSize(Context context, SpannableString target, @DimenRes int size, int start, int end) {
        if (start < 0) {
            return target;
        }
        target.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(size)), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return target;
    }

     public static SpannableString getSpannableStringSize(String target,  int size, int start, int end) {
        SpannableString spannable = new SpannableString(target);
        if (start < 0) {
          return spannable;
        }
        spannable.setSpan(new AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
     }

    /**
     * 字体粗体
     */
    public static SpannableString getSpannableStringBold(SpannableString target, int start, int end) {
        if (start < 0) {
            return target;
        }
        target.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return target;
    }

    /**
     * 字体粗体&斜体
     */
    public static SpannableString getSpannableStringBoldItalic(SpannableString target, int start, int end) {
        if (start < 0) {
            return target;
        }
        target.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return target;
    }

    public static SpannableString getSpannableStringNormal(SpannableString target, int start, int end) {
        if (start < 0) {
            return target;
        }
        target.setSpan(new StyleSpan(Typeface.NORMAL), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return target;
    }

    /**
     * 字体粗体
     */
    public static SpannableString getSpannableStringBold(Context context, @StringRes int strId) {
        String text = context.getString(strId);
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        return getSpannableStringBold(new SpannableString(text), 0, text.length());
    }

    public static SpannableString getSpannableStringFond(Context context, String target, int color, int size, String format) {
        SpannableString msp = new SpannableString(target);
        if (TextUtils.isEmpty(format)) {
            return msp;
        }
        int start = target.indexOf(format);
        if (start < 0) {
            return msp;
        }
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size, true);
        msp.setSpan(span, start, start + format.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, start + format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSpannableStringFondAndBold(Context context, String target, int color, int size, String format) {
        SpannableString msp = new SpannableString(target);
        if (TextUtils.isEmpty(format)) {
            return msp;
        }
        int start = target.indexOf(format);
        if (start < 0) {
            return msp;
        }
        AbsoluteSizeSpan span = new AbsoluteSizeSpan(size, true);
        msp.setSpan(span, start, start + format.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new StyleSpan(Typeface.BOLD), start, start + format.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, start + format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }


    public static String getColorString(String dexColor, String str) {
        String result = "<font color=\"" + dexColor + "\">" + str + "</font>";
        return result;
    }

    public static String replaceStr(String src, String oldValue, String newValue) {
        if (TextUtils.isEmpty(src) || TextUtils.isEmpty(oldValue) || null == newValue || TextUtils.equals(oldValue, newValue))
            return src;
        return src.replace(oldValue, newValue);
    }

//    public static SpannableString getSpannableStringColorAndBoldAndSize(Context context, int textId, int color, String object) {
//        String text = context.getString(textId, object);
//        if (TextUtils.isEmpty(text)) {
//            return new SpannableString("");
//        }
//        int index = text.indexOf(object);
//        int start = index;
//        int end = index + object.length();
//        SpannableString msp = new SpannableString(text);
//        msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        msp.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        return msp;
//    }

    public static SpannableString getSpannableStringColorAndBold(Context context, int textId, int color, Object... object) {
        String text = context.getString(textId, object);
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        SpannableString msp = new SpannableString(text);
        if (object != null && object.length > 0) {
            for (Object item : object) {
                if (item instanceof String) {
                    String str = (String) item;
                    int index = text.indexOf(str);
                    if (index == -1)
                        continue;
                    int end = index + str.length();
                    msp.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    msp.setSpan(new StyleSpan(Typeface.BOLD), index, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return msp;
    }

    /**
     * 文案增加删除线
     */
    public static SpannableString getSpannableStringStrikethrough(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        SpannableString msp = new SpannableString(text);
        msp.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableStringBuilder getSpannableBuilderBySpannables(Object... object){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (object != null && object.length > 0) {
            for (Object item : object) {
                if (item instanceof SpannableString) {
                    spannableStringBuilder.append((SpannableString) item );
                }
            }
        }
        return spannableStringBuilder;
    }
}
