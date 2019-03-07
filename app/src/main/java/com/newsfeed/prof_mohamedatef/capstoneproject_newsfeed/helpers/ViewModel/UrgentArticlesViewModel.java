package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.BasicApp;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.util.List;


/**
 * Created by Prof-Mohamed Atef on 2/24/2019.
 */

public class UrgentArticlesViewModel extends AndroidViewModel {

    String Category;

    private final MediatorLiveData<List<ArticlesEntity>> mObserverMediatorLiveDataListUrgentArticles;

    public UrgentArticlesViewModel(@NonNull Application application) {
        super(application);
        Config.application=application;
        this.mObserverMediatorLiveDataListUrgentArticles = new MediatorLiveData<>();
        this.mObserverMediatorLiveDataListUrgentArticles.setValue(null);
        LiveData<List<ArticlesEntity>> UrgentArticlesList=((BasicApp)application).getRepository().LoadUrgentArticle(this.Category);
        mObserverMediatorLiveDataListUrgentArticles.addSource(UrgentArticlesList, mObserverMediatorLiveDataListUrgentArticles::setValue);
    }

    public void setCategory(String category){
        this.Category=category;
        LiveData<List<ArticlesEntity>> LoadedArticlesList=((BasicApp) Config.application).getRepository().LoadUrgentArticle(category);
        mObserverMediatorLiveDataListUrgentArticles.addSource(LoadedArticlesList, mObserverMediatorLiveDataListUrgentArticles::setValue);
    }

    public String getCategory() {
        return Category;
    }

    public MediatorLiveData<List<ArticlesEntity>> getmObserverMediatorLiveDataListUrgentArticles() {
        return mObserverMediatorLiveDataListUrgentArticles;
    }

}
