package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.Helpers;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask.InsertAsyncTask;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;

import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class InsertClass {
    private ArticlesEntity articleEntity;

    public InsertClass(){
        articleEntity=new ArticlesEntity();
    }


    public void TryInsert1(AppDatabase mDatabase, ArrayList<ArticlesEntity> list, NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted, String key) {
        InsertAsyncTask insertAsyncTask=new InsertAsyncTask(mDatabase,list,onNewsTaskCompleted,key);
        insertAsyncTask.execute();
    }
}
