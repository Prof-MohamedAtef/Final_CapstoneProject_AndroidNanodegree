package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.JobDispatcher;

import android.content.Context;
import android.support.annotation.NonNull;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Services.LatestNewsFireBJobService;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by Prof-Mohamed Atef on 3/1/2019.
 */


public class JobDispatcherReminder {
    private static final int REMINDER_INTERVAL_MINUTES=1;
//    private static final int REMINDER_INTERVAL_SECONDS=(int) (TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL_MINUTES));
    private static final int REMINDER_INTERVAL_SECONDS=2;
    private static final int SYNC_FLEXTIME_SECONDS =REMINDER_INTERVAL_SECONDS;

    private static final String REMINDER_JOB_TAG="NewsAPI_TAG";

    private static boolean sInitialized;

    synchronized public static void scheduleFetchReminder(@NonNull final Context context){
        if (sInitialized) return;
        Driver driver=new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher=new FirebaseJobDispatcher(driver);

        Job constraintReminderJob=dispatcher.newJobBuilder()
                .setService(LatestNewsFireBJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setConstraints(Constraint.ON_UNMETERED_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(
//                        REMINDER_INTERVAL_SECONDS,
//                        REMINDER_INTERVAL_SECONDS+SYNC_FLEXTIME_SECONDS))
                .setTrigger(Trigger.executionWindow(
                        300,
                        301))
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(constraintReminderJob);
        sInitialized=true;
    }
}