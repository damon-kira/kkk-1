package com.colombia.credit.view.wheel;

import android.os.Handler;
import java.util.TimerTask;


public class InertiaTimerTask extends TimerTask {

    float a;
    final float velocityY;
    final WheelView loopView;

    public InertiaTimerTask(WheelView loopview, float velocityY) {
        super();
        loopView = loopview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        Handler messageHandler = loopView.getMessageHandler();
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            loopView.cancelFuture();
            messageHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        float totalScrollY = loopView.getTotalScrollY();
        totalScrollY = totalScrollY - i;
        loopView.setTotalScrollY(totalScrollY);
        if (!loopView.isLoop()) {
            float itemHeight = loopView.getItemHeight();
            float top = (-loopView.getInitPosition()) * itemHeight;
            float bottom = (loopView.getItemsCount() - 1 - loopView.getInitPosition()) * itemHeight;
            if(totalScrollY - itemHeight*0.25 < top){
                top = totalScrollY + i;
            }
            else if(totalScrollY + itemHeight*0.25 > bottom){
                bottom = totalScrollY + i;
            }

            if (totalScrollY <= top){
                a = 40F;
                totalScrollY = (int)top;
            } else if (totalScrollY >= bottom) {
                totalScrollY = (int)bottom;
                a = -40F;
            }
            loopView.setTotalScrollY(totalScrollY);
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        messageHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }

}
