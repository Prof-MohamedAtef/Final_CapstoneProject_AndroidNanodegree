package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;

import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class LiveDataRepo {
    private static LiveDataRepo liveDataRepoInstance;
    private final AppDatabase mDatabase;

    private MediatorLiveData<List<ArticlesEntity>> mObservableArticles;

    public LiveDataRepo(final AppDatabase database) {

        mDatabase = database;
        mObservableArticles = new MediatorLiveData<>();

        mObservableArticles.addSource(mDatabase.articlesDao().getAllArticlesData(),
                ArticlesEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableArticles.postValue(ArticlesEntities);
                    }
                });
    }

    public static LiveDataRepo getLiveDataRepoInstance(final AppDatabase database){
        if(liveDataRepoInstance==null){
            synchronized (LiveDataRepo.class){
                if (liveDataRepoInstance==null){
                    liveDataRepoInstance=new LiveDataRepo(database);
                }
            }
        }
        return liveDataRepoInstance;
    }

    public LiveData<List<ArticlesEntity>> LoadUrgentArticle(String urgentArticles) {
        return mDatabase.articlesDao().getArticlesDataByCategory(urgentArticles);
    }

    public LiveData<List<FirebaseDataHolder>> getAllFirebaseArticlesData(){
        return mDatabase.articlesDao().getAllFirebaseArticles();
    }

    public LiveData<List<ArticlesEntity>> isCategoryExist(String category){
        LiveData<List<ArticlesEntity>> xList=mDatabase.articlesDao().getArticlesDataByCategory(category);
        return xList;
    }
}