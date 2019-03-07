package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */
@Dao
public interface ArticlesDao {
    @Insert
    long InsertArticle(ArticlesEntity articleEntity);

    @Insert
    long InsertFirebaseArticle(FirebaseDataHolder firebaseDataHolder);

    @Query("DELETE FROM Articles WHERE CATEGORY LIKE :CATEGORY")
    abstract int deleteByCATEGORY(String CATEGORY);

    @Query("SELECT * From Articles where CATEGORY LIKE :CATEGORY")
    LiveData<List<ArticlesEntity>> getArticlesDataByCategory(String CATEGORY);

    @Query("SELECT * From Reports")
    LiveData<List<FirebaseDataHolder>> getAllFirebaseArticles();

    @Query("SELECT * From Articles")
    LiveData<List<ArticlesEntity>> getAllArticlesData();
}
