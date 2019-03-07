package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.BasicApp;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 3/6/2019.
 */

public class FirebaseViewModel extends AndroidViewModel {

    String Category;

    private final MediatorLiveData<List<ArticlesEntity>> mObserverMediatorLiveDataListFirebaseArticlesByCategory;

//    private final MediatorLiveData<List<FirebaseDataHolder>> mObserverMediatorLiveDataListFirebaseArticles;


    public FirebaseViewModel(@NonNull Application application) {
        super(application);
        Config.application = application;
        this.mObserverMediatorLiveDataListFirebaseArticlesByCategory = new MediatorLiveData<>();
//        this.mObserverMediatorLiveDataListFirebaseArticles=new MediatorLiveData<>();
//        mObserverMediatorLiveDataListFirebaseArticles.setValue(null);
        this.mObserverMediatorLiveDataListFirebaseArticlesByCategory.setValue(null);
        LiveData<List<ArticlesEntity>> UrgentArticlesList = ((BasicApp) application).getRepository().LoadUrgentArticle(this.Category);
        mObserverMediatorLiveDataListFirebaseArticlesByCategory.addSource(UrgentArticlesList, mObserverMediatorLiveDataListFirebaseArticlesByCategory::setValue);
        LiveData<List<FirebaseDataHolder>> AllFirebaseArticles=((BasicApp) application).getRepository().getAllFirebaseArticlesData();
//        mObserverMediatorLiveDataListFirebaseArticles.addSource(AllFirebaseArticles,);
//        mObserverMediatorLiveDataListFirebaseArticles.
    }

    public void setCategory(String category) {
        this.Category = category;
        LiveData<List<ArticlesEntity>> LoadedArticlesList = ((BasicApp) Config.application).getRepository().LoadUrgentArticle(category);
        mObserverMediatorLiveDataListFirebaseArticlesByCategory.addSource(LoadedArticlesList, mObserverMediatorLiveDataListFirebaseArticlesByCategory::setValue);
    }


    public LiveData<List<FirebaseDataHolder>> returnAllFirebaseData(){
        LiveData<List<FirebaseDataHolder>> LoadedArticlesList = ((BasicApp) Config.application).getRepository().getAllFirebaseArticlesData();
        return LoadedArticlesList;
    }


    public String getCategory() {
        return Category;
    }

    public MediatorLiveData<List<ArticlesEntity>> getmObserverMediatorLiveDataListFirebaseArticlesByCategory() {
        return mObserverMediatorLiveDataListFirebaseArticlesByCategory;
    }
}