package com.kira.learning.view.wheel;


public class OnItemSelectedRunnable implements Runnable {
    final WheelView loopView;

    public OnItemSelectedRunnable(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public final void run() {
        OnItemSelectedListener onItemSelectedListener = loopView.getOnItemSelectedListener();
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
        }
    }
}
