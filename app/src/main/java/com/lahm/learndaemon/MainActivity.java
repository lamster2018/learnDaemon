package com.lahm.learndaemon;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lahm.learndaemon.bg.BgPlayService;
import com.lahm.learndaemon.nls.NLS;
import com.lahm.learndaemon.nls.NLSProtectService;
import com.lahm.learndaemon.scheduler.JobSchedulerManager;
import com.lahm.learndaemon.screen.ScreenManager;
import com.lahm.learndaemon.screen.ScreenReceiverUtil;
import com.lahm.learndaemon.service.ForegroundDaemonService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDaemon();
    }

    private void startDaemon() {
        initForegroundDaemonService();
        initScreenBroadcastReceiver();
        initBgMusicService();
        initJobScheduler();
        initNLS();
    }

    //-------------前台----------------
    private Intent foregroundDaemonServiceIntent;

    private void initForegroundDaemonService() {
        foregroundDaemonServiceIntent = new Intent(this, ForegroundDaemonService.class);
        startService(foregroundDaemonServiceIntent);
    }

    //--------------1像素--------------
    private ScreenReceiverUtil screenReceiverUtil;// 动态注册锁屏等广播

    private ScreenManager screenManager;// 1像素Activity管理类

    private void initScreenBroadcastReceiver() {
        screenReceiverUtil = new ScreenReceiverUtil(this);
        screenReceiverUtil.setScreenReceiverListener(mScreenListener);
        screenManager = ScreenManager.getScreenManagerInstance(this);
    }

    private ScreenReceiverUtil.ScreenStateListener mScreenListener = new ScreenReceiverUtil.ScreenStateListener() {
        @Override
        public void onScreenOn() {
            // 亮屏，移除"1像素"
            screenManager.finishActivity();
        }

        @Override
        public void onScreenOff() {
            screenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };

    //------------后台音乐播放
    private Intent bgService;

    private void initBgMusicService() {
        bgService = new Intent(this, BgPlayService.class);
        startService(bgService);
    }

    //------------JobService
    private JobSchedulerManager jobManager;

    private void initJobScheduler() {
        jobManager = JobSchedulerManager.getJobSchedulerInstance(this);
        jobManager.startJobScheduler();
    }

    //------------NLS
    private Intent nlsIntent;
    private Intent nlsProtectIntent;

    private void initNLS() {
        if (NLS.isNotificationListenerEnabled(this)) {
            nlsIntent = new Intent(this, NLS.class);
            startService(nlsIntent);
            nlsProtectIntent = new Intent(this, NLSProtectService.class);
            startService(nlsProtectIntent);
        } else startActivity(NLS.go2NLSSettingIntent());//这里可以加一个startActivityForResult去做回调开启
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (foregroundDaemonServiceIntent != null) stopService(foregroundDaemonServiceIntent);
        if (screenReceiverUtil != null) screenReceiverUtil.stopScreenReceiverListener();
        if (bgService != null) stopService(bgService);
        if (jobManager != null) jobManager.stopJobScheduler();
        if (nlsIntent != null) stopService(nlsIntent);
        if (nlsProtectIntent != null) stopService(nlsProtectIntent);
    }

    public static boolean isAPPALive(Context mContext, String packageName) {
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for (ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList) {
            if (packageName.equals(appInfo.processName)) {
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }
}
