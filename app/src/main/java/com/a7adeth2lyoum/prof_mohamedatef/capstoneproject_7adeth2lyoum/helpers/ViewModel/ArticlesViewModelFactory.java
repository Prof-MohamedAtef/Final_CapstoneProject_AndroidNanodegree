package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;


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
        return (T) new CheckUrgentArticlesStatusViewModel( mDatabase, Category);
    }
}