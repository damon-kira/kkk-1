package com.colombia.credit.view.wheel;

import android.os.Handler;
import java.util.TimerTask;

public class SmoothScrollTimerTask extends TimerTask {

    int realTotalOffset;
    int realOffset;
    int offset;
    final WheelView loopView;

    public SmoothScrollTimerTask(WheelView loopview, int offset) {
        this.loopView = loopview;
        this.offset = offset;
        realTotalOffset = Integer.MAX_VALUE;
        realOffset = 0;
    }

    @Override
    public final void run() {
        Handler messageHandler = loopView.getMessageHandler();
        if (realTotalOffset == Integer.MAX_VALUE) {
            realTotalOffset = offset;
        }
        //把要滚动的范围细分成10小份，按10小份单位来重绘
        realOffset = (int) ((float) realTotalOffset * 0.1F);

        if (realOffset == 0) {
            if (realTotalOffset < 0) {
                realOffset = -1;
            } else {
                realOffset = 1;
            }
        }

        if (Math.abs(realTotalOffset) <= 1) {
            loopView.cancelFuture();
            messageHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
        } else {
            float totalScrollY = loopView.getTotalScrollY();
            totalScrollY = totalScrollY + realOffset;
            loopView.setTotalScrollY(totalScrollY);
            //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
            if (!loopView.isLoop()) {
                float itemHeight = loopView.getItemHeight();
                float top = (float) (-loopView.getInitPosition()) * itemHeight;
                float bottom = (float) (loopView.getItemsCount() - 1 - loopView.getInitPosition()) * itemHeight;
                if (totalScrollY <= top || totalScrollY >= bottom) {
                    loopView.setTotalScrollY(totalScrollY - realOffset);
                    loopView.cancelFuture();
                    messageHandler.sendEmptyMessage(MessageHandler.WHAT_ITEM_SELECTED);
                    return;
                }
            }
            messageHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
            realTotalOffset = realTotalOffset - realOffset;
        }
    }
}
