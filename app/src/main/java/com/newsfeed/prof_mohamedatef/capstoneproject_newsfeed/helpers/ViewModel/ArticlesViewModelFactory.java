package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;


/**
 * Created by Prof-Mohamed Atef on 2/24/2019.
 */

public class ArticlesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDatabase;
    private final String Category;

    public ArticlesViewModelFactory(AppDatabase mDatabase, String category){
        this.mDatabase=mDatabase;
        this.Category=category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new getArticlesViewModel( mDatabase, Category);
    }
}