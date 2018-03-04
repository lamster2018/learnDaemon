package com.lahm.learndaemon.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Project Name:learnDaemon
 * Package Name:com.lahm.learndaemon.screen
 * Created by lahm on 2018/3/4 下午8:45 .
 */

public class ScreenReceiverUtil {
    private Context mContext;
    // 锁屏广播接收器
    private ScreenBroadcastReceiver mScreenReceiver;
    // 屏幕状态改变回调接口
    private ScreenStateListener mStateReceiverListener;

    public ScreenReceiverUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void setScreenReceiverListener(ScreenStateListener mStateReceiverListener) {
        this.mStateReceiverListener = mStateReceiverListener;
        // 动态启动广播接收器
        this.mScreenReceiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        mContext.registerReceiver(mScreenReceiver, filter);
    }

    public void stopScreenReceiverListener() {
        if (mContext != null && mScreenReceiver != null)
            mContext.unregisterReceiver(mScreenReceiver);
    }

    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mStateReceiverListener == null) return;
            String action = intent.getAction();
            Log.d("KeepAppAlive", "SreenLockReceiver-->监听到系统广播：" + action);
            if (action == null) return;
            switch (action) {
                case Intent.ACTION_SCREEN_ON:
                    mStateReceiverListener.onScreenOn();
                    break;
                case Intent.ACTION_SCREEN_OFF:
                    mStateReceiverListener.onScreenOff();
                    break;
                case Intent.ACTION_USER_PRESENT:
                    mStateReceiverListener.onUserPresent();
                    break;
            }
        }
    }

    // 监听screen状态对外回调接口
    public interface ScreenStateListener {
        void onScreenOn();

        void onScreenOff();

        void onUserPresent();
    }
}
