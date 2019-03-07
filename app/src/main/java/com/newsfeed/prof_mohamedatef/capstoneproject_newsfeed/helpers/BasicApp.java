package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.LiveDataRepo;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class BasicApp extends Application{
    private AppExecutors mAppExecutors;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("data");
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, mAppExecutors);
    }

    public LiveDataRepo getRepository() {
        return LiveDataRepo.getLiveDataRepoInstance(getDatabase());
    }
}
