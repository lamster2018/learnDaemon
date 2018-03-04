package com.lahm.learndaemon.scheduler;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

/**
 * Project Name:learnDaemon
 * Package Name:com.lahm.learndaemon.scheduler
 * Created by lahm on 2018/3/4 下午9:17 .
 */

public class JobSchedulerManager {
    private static final int JOB_ID = 1;
    private static JobSchedulerManager mJobManager;
    private JobScheduler mJobScheduler;
    private static Context mContext;

    private JobSchedulerManager(Context ctxt) {
        this.mContext = ctxt;
        mJobScheduler = (JobScheduler) ctxt.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public final static JobSchedulerManager getJobSchedulerInstance(Context ctxt) {
        if (mJobManager == null) {
            mJobManager = new JobSchedulerManager(ctxt);
        }
        return mJobManager;
    }

    @TargetApi(21)
    public void startJobScheduler() {
        // 如果JobService已经启动或API<21，返回
        if (AliveJobService.isJobServiceAlive()) return;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        // 构建JobInfo对象，传递给JobSchedulerService
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(mContext, AliveJobService.class));
        // 设置每3秒执行一下任务
        builder.setPeriodic(3000);
        // 设置设备重启时，执行该任务
        builder.setPersisted(true);
        // 当插入充电器，执行该任务
        builder.setRequiresCharging(true);
        JobInfo info = builder.build();
        //开始定时执行该系统任务
        mJobScheduler.schedule(info);
    }

    @TargetApi(21)
    public void stopJobScheduler() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;
        mJobScheduler.cancelAll();
    }
}