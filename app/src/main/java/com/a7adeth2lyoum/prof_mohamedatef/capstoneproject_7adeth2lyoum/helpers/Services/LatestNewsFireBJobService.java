package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Services;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.SecurityManager.GenericConnect;

import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config.CategoryName;

/**
 * Created by Prof-Mohamed Atef on 3/1/2019.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LatestNewsFireBJobService extends com.firebase.jobdispatcher.JobService{


    private final String LOG_TAG = LatestNewsFireBJobService.class.getSimpleName();

    private AppDatabase mDatabase;
    private String KEY;
    private ProgressDialog dialog;
    public NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted;
    NewsApiAsyncTask.OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted;
    Context mContext;
    private AsyncTask mBackgroundTask;

    public LatestNewsFireBJobService(){
    }

    @Override
    public boolean onStartJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        mBackgroundTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                GenericConnect genericConnect=new GenericConnect(Config.mDatabase,Config.Listener,Config.mContext,params,CategoryName);
                return genericConnect.executeConnection();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                jobFinished(job,false);
            }
        };
        mBackgroundTask.execute(Config.URL);
        return true;
    }

    @Override
    public boolean onStopJob(@NonNull com.firebase.jobdispatcher.JobParameters job) {
        if (mBackgroundTask!=null) mBackgroundTask.cancel(true);
        return true;
    }

    public LatestNewsFireBJobService(AppDatabase database, NewsApiAsyncTask.OnNewsTaskCompleted onTaskCompleted, Context context, String Category){
        this.onNewsTaskCompleted=onTaskCompleted;
        dialog = new ProgressDialog(context);
        mContext=context;
        this.mDatabase=database;
        this.KEY=Category;
    }
}