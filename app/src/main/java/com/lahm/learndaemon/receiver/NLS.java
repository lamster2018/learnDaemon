package com.lahm.learndaemon.receiver;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Set;

/**
 * Project Name:learnDaemon
 * Package Name:com.lahm.learndaemon.receiver
 * Created by lahm on 2018/3/5 上午11:33 .
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLS extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    //打开通知监听授权
    public static Intent go2NLSSettingIntent() {
        Intent intent = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }

    //检查授权情况
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

}
