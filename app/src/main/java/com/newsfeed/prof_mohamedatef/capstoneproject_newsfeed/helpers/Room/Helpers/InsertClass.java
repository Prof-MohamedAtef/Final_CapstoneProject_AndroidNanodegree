package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Helpers;

import android.content.Context;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseImageHelper;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.InsertLocallyFirebaseAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.InsertWebServiceAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.UrgentAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class InsertClass {
    private ArticlesEntity articleEntity;

    public InsertClass(){
        articleEntity=new ArticlesEntity();
    }


    public void TryInsertNewsAPIData(AppDatabase mDatabase, ArrayList<ArticlesEntity> list, NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted, String key) {
        InsertWebServiceAsyncTask insertWebServiceAsyncTask =new InsertWebServiceAsyncTask(mDatabase,list,onNewsTaskCompleted,key);
        insertWebServiceAsyncTask.execute();
    }

    public void TryInsertNewsAPIData(AppDatabase mDatabase, ArrayList<ArticlesEntity> list, UrgentAsyncTask.OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted, String key) {
        InsertWebServiceAsyncTask insertWebServiceAsyncTask =new InsertWebServiceAsyncTask(mDatabase,list,onNewsUrgentTaskCompleted,key);
        insertWebServiceAsyncTask.execute();
    }

    public void TryInsertFirebaseReportData(AppDatabase mDatabase, FirebaseDataHolder firebaseDataHolder, InsertLocallyFirebaseAsyncTask.OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String key) {
        InsertLocallyFirebaseAsyncTask insertLocallyFirebaseAsyncTask=new InsertLocallyFirebaseAsyncTask(mDatabase,firebaseDataHolder,onFirebaseInsertedLocallyCompleted_,key);
        insertLocallyFirebaseAsyncTask.execute();
    }

    public void TryInsertFirebaseReportList(AppDatabase mDatabase, List<FirebaseDataHolder> firebaseDataHolderArrayList, InsertLocallyFirebaseAsyncTask.OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String key) {
        InsertLocallyFirebaseAsyncTask insertLocallyFirebaseAsyncTask=new InsertLocallyFirebaseAsyncTask(mDatabase,firebaseDataHolderArrayList,onFirebaseInsertedLocallyCompleted_,key);
        insertLocallyFirebaseAsyncTask.execute();
    }

    public void TryUpdateFirebaseReportImge(AppDatabase mDatabase, FirebaseImageHelper firebaseImageHelper, InsertLocallyFirebaseAsyncTask.OnFirebaseInsertedLocallyCompleted onFirebaseInsertedLocallyCompleted_, String key) {
        InsertLocallyFirebaseAsyncTask insertLocallyFirebaseAsyncTask=new InsertLocallyFirebaseAsyncTask(mDatabase,firebaseImageHelper,onFirebaseInsertedLocallyCompleted_,key);
        insertLocallyFirebaseAsyncTask.execute();
    }

    public void TryInsertWebHoseAPIData(AppDatabase mDatabase, ArrayList<ArticlesEntity> list, WebHoseApiAsyncTask.OnWebHoseTaskCompleted onNewsTaskCompleted, String key) {
        InsertWebServiceAsyncTask insertWebServiceAsyncTask =new InsertWebServiceAsyncTask(mDatabase,list,onNewsTaskCompleted,key);
        insertWebServiceAsyncTask.execute();
    }
}
