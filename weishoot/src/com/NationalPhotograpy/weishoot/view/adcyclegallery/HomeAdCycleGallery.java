
package com.NationalPhotograpy.weishoot.view.adcyclegallery;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 首页广告无限循环View
 * 
 * @author wuchx
 * @date 2013-12-5 上午11:21:22
 */
public class HomeAdCycleGallery extends Gallery {
    // handler
    private final int SWITCH_NEXT = 1;

    private int switchDuration = 5000;// 切换间隔时间

    private boolean dragMode = false;// 拖动模式

    private boolean goSwitch = true;// 是否继续切换

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SWITCH_NEXT:
                    if (!dragMode) {
                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
                    }
                    break;
            }
        }
    };

    public HomeAdCycleGallery(Context context) {
        super(context);
    }

    public HomeAdCycleGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeAdCycleGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX,
            float velocityY) {
        int kEvent;
        if (isScrollingLeft(downEvent, moveEvent)) {
            // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            // Otherwise scrolling right
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);

        return false;
    }

    public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX,
            float distanceY) {
        return super.onScroll(downEvent, moveEvent, distanceX, distanceY);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragMode = true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            dragMode = false;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 是否向左滚动
     * 
     * @author wuchx
     * @param downEvent
     * @param moveEvent
     * @return
     */
    private boolean isScrollingLeft(MotionEvent downEvent, MotionEvent moveEvent) {
        Log.i("", "<d>downEvent " + downEvent + " moveEvent " + moveEvent);
        return moveEvent.getX() > downEvent.getX();
    }

    /**
     * 启动切换线程
     * 
     * @author wuchx
     */
    public void startSwitchTimer() {
        if (!sWitchTimer.isAlive()) {
            sWitchTimer.start();
        }
    }

    /**
     * 停止切换线程
     * 
     * @author wuchx
     */
    public void stopSwitchTimer() {
        goSwitch = false;
    }

    /**
     * 图片切换线程
     */
    private Thread sWitchTimer = new Thread() {
        public void run() {
            while (goSwitch) {
                handler.sendEmptyMessage(SWITCH_NEXT);
                try {
                    Thread.sleep(switchDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    // getter & setter
    /**
     * 取得图片切换间隔时间
     * 
     * @author wuchx
     * @return
     */
    public int getSwitchDuration() {
        return switchDuration;
    }

    /**
     * 设置图片切换间隔时间
     * 
     * @author wuchx
     * @param switchDuration
     */
    public void setSwitchDuration(int switchDuration) {
        this.switchDuration = switchDuration;
    }

}
