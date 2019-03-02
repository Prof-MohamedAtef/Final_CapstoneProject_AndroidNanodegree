package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;

import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */
@Dao
public interface ArticlesDao {
    @Insert
    long InsertArticle(ArticlesEntity articleEntity);

    @Query("DELETE FROM Articles WHERE CATEGORY LIKE :CATEGORY")
    abstract int deleteByCATEGORY(String CATEGORY);

    @Query("SELECT * From Articles where CATEGORY LIKE :CATEGORY")
    LiveData<List<ArticlesEntity>> getArticlesDataByCategory(String CATEGORY);

    @Query("SELECT * From Articles")
    LiveData<List<ArticlesEntity>> getAllArticlesData();
}
