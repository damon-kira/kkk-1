package com.hjq.window;

import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

/**
 *    desc   : 窗口 View 的触摸事件监听
 */
public interface OnWindowVIewTouchListener<V extends View> {

    /**
     * 触摸回调
     */
    boolean onTouch(@NonNull EasyWindow<?> easyWindow, @NonNull V view, @NonNull MotionEvent event);
}