package com.lahm.learndaemon.nls;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;

import java.util.List;

import static android.service.notification.NotificationListenerService.requestRebind;

/**
 * Project Name:learnDaemon
 * Package Name:com.lahm.learndaemon.nls
 * Created by lahm on 2018/3/5 上午11:49 .
 */

public class NLSProtectService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ensureCollectorRunning();
        return START_STICKY;
    }

    private void ensureCollectorRunning() {
        ComponentName collectorComponent = new ComponentName(this, com.lahm.learndaemon.nls.NLS.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) return;

        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == Process.myPid()
                    /*&& service.clientCount > 0 && !TextUtils.isEmpty(service.clientPackage)*/) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) return;
        toggleNotificationListenerService();
    }

    private void toggleNotificationListenerService() {
        ComponentName thisComponent = new ComponentName(this, com.lahm.learndaemon.nls.NLS.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestRebind(thisComponent);
        else {
            PackageManager pm = getPackageManager();
            pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}