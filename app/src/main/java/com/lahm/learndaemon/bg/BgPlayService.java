package com.lahm.learndaemon.bg;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lahm.learndaemon.R;

/**
 * Project Name:learnDaemon
 * Package Name:com.lahm.learndaemon.bgmusic
 * Created by lahm on 2018/3/5 上午9:54 .
 * <p>
 * Copyright (c) 2016—2017 https://www.lizhiweike.com all rights reserved.
 */

public class BgPlayService extends Service {
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),
                R.raw.silent);
        mMediaPlayer.setLooping(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return START_STICKY;
    }


    private void startPlayMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }


    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        // 重启
        Intent intent = new Intent(getApplicationContext(), BgPlayService.class);
        startService(intent);
    }
}
