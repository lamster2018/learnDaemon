package com.lahm.learndaemon.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lahm.learndaemon.MainActivity;


/**
 * Project Name:android
 * Package Name:com.lizhiweike.daemon.screen
 * Created by lahm on 2017/10/31 17:10 .
 */
public class SinglePixelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        //避免卡死，允许点击外部消失
        setFinishOnTouchOutside(true);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(this).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        if (!MainActivity.isAPPALive(getApplicationContext(), getPackageName())) {
            Intent intentAlive = new Intent(this, MainActivity.class);
            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAlive);
        }
        super.onDestroy();
    }
}
