package com.hjq.window;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import android.view.MotionEvent;
import android.view.View;

/**
 * desc   : {@link View.OnTouchListener} 包装类
 */
@SuppressWarnings("rawtypes")
final class ViewTouchListenerWrapper implements View.OnTouchListener {

    @NonNull
    private final EasyWindow<?> mEasyWindow;
    @NonNull
    private final OnWindowVIewTouchListener mListener;

    ViewTouchListenerWrapper(@NonNull EasyWindow<?> easyWindow, @NonNull OnWindowVIewTouchListener listener) {
        mEasyWindow = easyWindow;
        mListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        return mListener.onTouch(mEasyWindow, view, event);
    }
}