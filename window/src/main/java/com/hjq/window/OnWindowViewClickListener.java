package com.hjq.window;

import androidx.annotation.NonNull;

import android.view.View;

/**
 * desc   : 窗口 View 的点击事件监听
 */
public interface OnWindowViewClickListener<V extends View> {

    /**
     * 点击回调
     */
    void onClick(@NonNull EasyWindow<?> easyWindow, @NonNull V view);
}