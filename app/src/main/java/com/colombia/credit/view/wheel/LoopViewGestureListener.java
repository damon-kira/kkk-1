package com.colombia.credit.view.wheel;

import android.view.MotionEvent;

public class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

   final WheelView loopView;

  public LoopViewGestureListener(WheelView loopview) {
       loopView = loopview;
   }

   @Override
   public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
       loopView.scrollBy(velocityY);
       return true;
   }
}
