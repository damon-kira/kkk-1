package com.hjq.window;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;

/**
 * desc   : 窗口布局填充回调
 */
public interface OnWindowLayoutInflateListener {

    /**
     * 布局填充完成回调
     *
     * @param easyWindow 当前窗口对象
     * @param view       填充完成的 View
     * @param layoutId   布局 id
     * @param parentView 填充布局所用父布局对象
     */
    void onWindowLayoutInflateFinished(@NonNull EasyWindow<?> easyWindow, @Nullable View view, @LayoutRes int layoutId, @NonNull ViewGroup parentView);
}