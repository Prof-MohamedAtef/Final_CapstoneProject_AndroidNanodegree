package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseImageHelper;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 3/6/2019.
 */

public class InsertLocallyFirebaseAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final AppDatabase appDatabase;
    private final List<FirebaseDataHolder> firebaseDataHolderArrayList;
    private FirebaseDataHolder firebaseDataHolder;
    private final String CategoryKey;
    private final FirebaseImageHelper firebaseImageHelper;
    private LiveData<List<FirebaseDataHolder>> reportssRoomList;
    final boolean[] Inserted = {false};
    public OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted;
    private String NULL_KEY="NULL_KEY";
    private long x;


    public InsertLocallyFirebaseAsyncTask(AppDatabase appDatabase, FirebaseDataHolder firebaseDataHolder, OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.firebaseDataHolder = firebaseDataHolder;
        this.onFirebaseInsertedLocallyCompleted=onFirebaseInsertedLocallyCompleted_;
        this.CategoryKey=Key_Type;
        firebaseImageHelper = null;
        firebaseDataHolderArrayList = null;
    }

    public InsertLocallyFirebaseAsyncTask(AppDatabase appDatabase, List<FirebaseDataHolder> firebaseDataHolderArrayList, OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.firebaseDataHolderArrayList= firebaseDataHolderArrayList;
        this.onFirebaseInsertedLocallyCompleted=onFirebaseInsertedLocallyCompleted_;
        this.CategoryKey=Key_Type;
        firebaseImageHelper = null;
    }


    public InsertLocallyFirebaseAsyncTask(AppDatabase appDatabase, FirebaseImageHelper firebaseImageHelper, OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.firebaseImageHelper= firebaseImageHelper;
        this.onFirebaseInsertedLocallyCompleted=onFirebaseInsertedLocallyCompleted_;
        this.CategoryKey=Key_Type;
        firebaseDataHolder = null;
        firebaseDataHolderArrayList = null;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onFirebaseInsertedLocallyCompleted.OnLocalFirebaseInsert(firebaseDataHolder);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        if (firebaseDataHolder!=null){
            if (Config.ActivityNum==3){
                reportssRoomList = appDatabase.articlesDao().getAllFirebaseArticles();
                reportssRoomList.observe((LifecycleOwner) onFirebaseInsertedLocallyCompleted, existingArticles -> {
                    InsertOperations(Inserted, existingArticles);
                });
            }
        } else if (firebaseDataHolderArrayList!=null){
            if (Config.ActivityNum==1){
                reportssRoomList = appDatabase.articlesDao().getAllFirebaseArticles();
                reportssRoomList.observe((LifecycleOwner) onFirebaseInsertedLocallyCompleted, existingArticles -> {
                    DeleteInsertOperations(Inserted, existingArticles);
                });
            }
        }
        return Inserted[0];
    }

    private boolean LoopOverList(List<FirebaseDataHolder> firebaseDataHolderArrayList) {
        for (FirebaseDataHolder firebaseDataHolder:firebaseDataHolderArrayList){
            splitParameteres(firebaseDataHolder);
//            if (CategoryKey.equals(CategoryKey)) {
//                firebaseDataHolder.setCategoryID(CategoryKey);
//            }
            x = appDatabase.articlesDao().InsertFirebaseArticle(firebaseDataHolder);
        }
        if (x>0){
            Inserted[0]=true;
        }else {
            Inserted[0]=false;
        }
        return Inserted[0];
    }

    private void DeleteInsertOperations(boolean[] inserted, List<FirebaseDataHolder> existingArticles) {
        if (existingArticles != null && existingArticles.size() > 0 && !existingArticles.isEmpty()) {
            int deleted = appDatabase.articlesDao().deleteAllFirebaseArticles();
            if (deleted > 0) {
                inserted[0] = LoopOverList(firebaseDataHolderArrayList);
                if (inserted[0]) {
                    inserted[0] = true;
                    reportssRoomList.removeObservers((LifecycleOwner) onFirebaseInsertedLocallyCompleted);
                } else {
                    inserted[0] = false;
                }
            } else {
                inserted[0] = LoopOverList(firebaseDataHolderArrayList);
                if (inserted[0]) {
                    inserted[0] = true;
                    reportssRoomList.removeObservers((LifecycleOwner) onFirebaseInsertedLocallyCompleted);
                } else {
                    inserted[0] = false;
                }
            }
        } else {
            inserted[0] = LoopOverList(firebaseDataHolderArrayList);
            if (inserted[0]) {
                inserted[0] = true;
                reportssRoomList.removeObservers((LifecycleOwner) onFirebaseInsertedLocallyCompleted);
            } else {
                inserted[0] = false;
            }
        }
    }

    private void InsertOperations(boolean[] inserted, List<FirebaseDataHolder> existingArticles) {
        inserted[0] = IsArrayInserted();
        if (inserted[0]) {
            inserted[0] = true;
            reportssRoomList.removeObservers((LifecycleOwner) onFirebaseInsertedLocallyCompleted);
        } else {
            inserted[0] = false;
        }
    }

    @NonNull
    private boolean IsArrayInserted() {
        long x = 0;
        if (CategoryKey.equals(CategoryKey)) {
            firebaseDataHolder.setCategoryID(CategoryKey);
        }

        if (firebaseDataHolder != null && appDatabase != null) {
            splitParameteres(firebaseDataHolder);
            x = appDatabase.articlesDao().InsertFirebaseArticle(firebaseDataHolder);
        }
        if (x > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void splitParameteres(FirebaseDataHolder firebaseDataHolder) {
        if (firebaseDataHolder .getUserName()==null){
            firebaseDataHolder.setUserName(NULL_KEY);
        }
        if (firebaseDataHolder.getKey()==null){
            firebaseDataHolder.setKey(NULL_KEY);
        }
        if (firebaseDataHolder.getDate()==null){
            firebaseDataHolder.setDate(NULL_KEY);
        }
        if (firebaseDataHolder.getImageFileUri()==null){
            firebaseDataHolder.setImageFileUri(NULL_KEY);
        }
        if (firebaseDataHolder.getUserEmail()==null){
            firebaseDataHolder.setUserEmail(NULL_KEY);
        }
        if (firebaseDataHolder.getDESCRIPTION()==null){
            firebaseDataHolder.setDESCRIPTION(NULL_KEY);
        }
        if (firebaseDataHolder.getTITLE()==null){
            firebaseDataHolder.setTITLE(NULL_KEY);
        }
        if (firebaseDataHolder.getAudiourl()==null){
            firebaseDataHolder.setAudiourl(NULL_KEY);
        }
        if (firebaseDataHolder.getKey()==null){
            firebaseDataHolder.setKey(NULL_KEY);
        }
    }

    public interface OnFirebaseInsertedLocallyCompleted{
        void OnLocalFirebaseInsert(FirebaseDataHolder firebaseDataHolder);
    }
}