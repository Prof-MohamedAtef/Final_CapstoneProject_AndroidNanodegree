package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;

import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/24/2019.
 */

public class getArticlesViewModel extends ViewModel {

    private LiveData<List<ArticlesEntity>> Category;

    public LiveData<List<ArticlesEntity>> getCategory(){
        return Category;
    }


    public getArticlesViewModel(AppDatabase mDatabase, String category) {
        Category=mDatabase.articlesDao().getArticlesDataByCategory(category);
    }
}
