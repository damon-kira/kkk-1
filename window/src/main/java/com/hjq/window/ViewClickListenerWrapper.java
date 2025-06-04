package com.hjq.window;

import androidx.annotation.NonNull;

import android.view.View;

/**
 * desc   : {@link View.OnClickListener} 包装类
 */
@SuppressWarnings("rawtypes")
final class ViewClickListenerWrapper implements View.OnClickListener {

    @NonNull
    private final EasyWindow<?> mEasyWindow;
    @NonNull
    private final OnWindowViewClickListener mListener;

    ViewClickListenerWrapper(@NonNull EasyWindow<?> easyWindow, @NonNull OnWindowViewClickListener listener) {
        mEasyWindow = easyWindow;
        mListener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View view) {
        mListener.onClick(mEasyWindow, view);
    }
}