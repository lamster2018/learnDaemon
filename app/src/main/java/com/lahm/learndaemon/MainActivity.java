package com.lahm.learndaemon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Intent foregroundDaemonServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startDaemon();
    }

    private void startDaemon() {
        initForegroundDaemonService();
    }

    private void initForegroundDaemonService() {
        foregroundDaemonServiceIntent = new Intent(this, ForegroundDaemonService.class);
        startService(foregroundDaemonServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (foregroundDaemonServiceIntent != null) stopService(foregroundDaemonServiceIntent);
    }
}
