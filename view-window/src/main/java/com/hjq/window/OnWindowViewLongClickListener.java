package com.hjq.window;

import androidx.annotation.NonNull;

import android.view.View;

/**
 * desc   : 窗口 View 的长按事件监听
 */
public interface OnWindowViewLongClickListener<V extends View> {

    /**
     * 长按回调
     */
    boolean onLongClick(@NonNull EasyWindow<?> easyWindow, @NonNull V view);
}