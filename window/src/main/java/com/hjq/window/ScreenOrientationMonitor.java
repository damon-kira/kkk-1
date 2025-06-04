package com.hjq.window;

import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * desc   : 屏幕方向旋转监听
 */
final class ScreenOrientationMonitor implements ComponentCallbacks {

    /**
     * 当前屏幕的方向
     */
    private int mScreenOrientation;

    /**
     * 屏幕旋转回调
     */
    @Nullable
    private OnScreenOrientationCallback mCallback;

    public ScreenOrientationMonitor(int screenOrientation) {
        mScreenOrientation = screenOrientation;
    }

    /**
     * 注册监听
     */
    void registerCallback(@Nullable Context context, @Nullable OnScreenOrientationCallback callback) {
        if (context == null) {
            return;
        }
        if (callback == null) {
            unregisterCallback(context);
            return;
        }
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            applicationContext.registerComponentCallbacks(this);
        }
        mCallback = callback;
    }

    /**
     * 取消监听
     */
    void unregisterCallback(@Nullable Context context) {
        if (context == null) {
            return;
        }
        Context applicationContext = context.getApplicationContext();
        if (applicationContext != null) {
            applicationContext.unregisterComponentCallbacks(this);
        }
        mCallback = null;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        if (mScreenOrientation == newConfig.orientation) {
            return;
        }
        mScreenOrientation = newConfig.orientation;

        if (mCallback == null) {
            return;
        }
        mCallback.onScreenOrientationChange(mScreenOrientation);
    }

    @Override
    public void onLowMemory() {
        // default implementation ignored
    }

    /**
     * 屏幕方向监听器
     */
    interface OnScreenOrientationCallback {

        /**
         * 监听屏幕旋转了
         *
         * @param newOrientation 最新的屏幕方向
         */
        default void onScreenOrientationChange(int newOrientation) {
        }
    }
}