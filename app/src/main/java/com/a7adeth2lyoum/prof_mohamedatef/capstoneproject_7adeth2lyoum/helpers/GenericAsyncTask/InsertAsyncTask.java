package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;
import java.util.ArrayList;
import java.util.List;

import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.WebHoseAPIKEY;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.NewsApiFragment.KEY_Urgent;


/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class InsertAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseNewsTaskCompleted;
    private String NULL_KEY="Someone";

    private ArticlesEntity articlesEntity;
    private ArrayList<ArticlesEntity> ArticlesEntityList;
    private AppDatabase appDatabase;
    private String CategoryKey;
    NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted;
    private ArrayList<ArticlesEntity> ArticlesEntityList_x;
    LiveData<List<ArticlesEntity>> articlesRoomList = null;

    @Override
    protected Boolean doInBackground(Void... voids) {
        final boolean[] Inserted = {false};
        articlesRoomList = appDatabase.articlesDao().getArticlesDataByCategory(CategoryKey);
        if (Config.Listener.equals(NEWSAPI_KEY)) {
            articlesRoomList.observe((LifecycleOwner) onNewsTaskCompleted, existingArticles -> {
                DeleteInsertOperations(Inserted, existingArticles, onNewsTaskCompleted, null);
            });
        } else if (Config.Listener.equals(WebHoseAPIKEY)) {
            articlesRoomList.observe((LifecycleOwner) onWebHoseNewsTaskCompleted, existingArticles -> {
                DeleteInsertOperations(Inserted, existingArticles, null, onWebHoseNewsTaskCompleted);
            });
        }
        return Inserted[0];
    }

    private void DeleteInsertOperations(boolean[] inserted, List<ArticlesEntity> existingArticles, NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted, WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseTaskCompleted) {
        if (onNewsTaskCompleted!=null){
            if (existingArticles!=null&&existingArticles.size()>0&&!existingArticles.isEmpty()){
                int deleted=appDatabase.articlesDao().deleteByCATEGORY(CategoryKey);
                if (deleted>0){
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onNewsTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }else {
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onNewsTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }
            }else {
                inserted[0] =IsArrayInserted();
                if (inserted[0]){
                    inserted[0] =true;
                    articlesRoomList.removeObservers((LifecycleOwner) onWebHoseTaskCompleted);
                }else {
                    inserted[0] =false;
                }
            }
        }else if (onWebHoseTaskCompleted!=null){
            if (existingArticles!=null&&existingArticles.size()>0&&!existingArticles.isEmpty()){
                int deleted=appDatabase.articlesDao().deleteByCATEGORY(CategoryKey);
                if (deleted>0){
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onWebHoseTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }else {
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onWebHoseTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }
            }else {
                inserted[0] =IsArrayInserted();
                if (inserted[0]){
                    inserted[0] =true;
                    articlesRoomList.removeObservers((LifecycleOwner) onWebHoseTaskCompleted);
                }else {
                    inserted[0] =false;
                }
            }
        }
    }

    @NonNull
    private boolean IsArrayInserted() {
        long x = 0;
        for (ArticlesEntity OneArticleEntity : this.ArticlesEntityList){
            if (OneArticleEntity.getAUTHOR()!=null){
                articlesEntity.setAUTHOR(OneArticleEntity.getAUTHOR());
            }else {
                articlesEntity.setAUTHOR(NULL_KEY);
            }
            if (OneArticleEntity.getTITLE()!=null){
                articlesEntity.setTITLE(OneArticleEntity.getTITLE());
            }else {
                articlesEntity.setTITLE(NULL_KEY);
            }
            if (OneArticleEntity.getDESCRIPTION()!=null){
                articlesEntity.setDESCRIPTION(OneArticleEntity.getDESCRIPTION());
            }else {
                articlesEntity.setDESCRIPTION(NULL_KEY);
            }
            if (OneArticleEntity.getARTICLE_URL()!=null){
                articlesEntity.setARTICLE_URL(OneArticleEntity.getARTICLE_URL());
            }else {
                articlesEntity.setARTICLE_URL(NULL_KEY);
            }
            if (OneArticleEntity.getIMAGE_URL()!=null){
                articlesEntity.setIMAGE_URL(OneArticleEntity.getIMAGE_URL());
            }else {
                articlesEntity.setIMAGE_URL(NULL_KEY);
            }
            if (OneArticleEntity.getPUBLISHED_AT()!=null){
                articlesEntity.setPUBLISHED_AT(OneArticleEntity.getPUBLISHED_AT());
            }else {
                articlesEntity.setPUBLISHED_AT(NULL_KEY);
            }
            if (OneArticleEntity.getSOURCE_NAME()!=null){
                articlesEntity.setSOURCE_NAME(OneArticleEntity.getSOURCE_NAME());
            }else {
                articlesEntity.setSOURCE_NAME(NULL_KEY);
            }
            if (OneArticleEntity.getAUDIO_URL()!=null){
                articlesEntity.setAUDIO_URL(OneArticleEntity.getAUDIO_URL());
            }else {
                articlesEntity.setAUDIO_URL(NULL_KEY);
            }
            if (CategoryKey.equals(KEY_Urgent)){
                articlesEntity.setCATEGORY(KEY_Urgent);
            }
            else if (CategoryKey.equals(CategoryKey)){
                articlesEntity.setCATEGORY(CategoryKey);
            }

            if (ArticlesEntityList!=null&&appDatabase!=null) {
                x = appDatabase.articlesDao().InsertArticle(articlesEntity);
            }
        }
        if (x>0){
            ArticlesEntityList_x=ArticlesEntityList;
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean != null) {
            if (aBoolean == true && Config.Listener.equals(NEWSAPI_KEY)) {
                onNewsTaskCompleted.onNewsApiTaskCompleted(ArticlesEntityList_x);
            }else if (aBoolean==true && Config.Listener.equals(WebHoseAPIKEY)){
                onWebHoseNewsTaskCompleted.onWebHoseTaskCompleted(ArticlesEntityList_x);
            }
//            else {
//                onNewsTaskCompleted.onNewsApiTaskCompleted(ArticlesEntityList);
//            }
        }
    }

    public InsertAsyncTask(AppDatabase appDatabase, ArrayList<ArticlesEntity> articlesRoomEntityList, NewsApiAsyncTask.OnNewsTaskCompleted onTaskCompletes, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.ArticlesEntityList = articlesRoomEntityList;
        this.onNewsTaskCompleted=onTaskCompletes;
        this.CategoryKey=Key_Type;
        articlesEntity=new ArticlesEntity();
        this.onWebHoseNewsTaskCompleted = null;
        Config.Listener=NEWSAPI_KEY;
    }

    public InsertAsyncTask(AppDatabase appDatabase, ArrayList<ArticlesEntity> articlesRoomEntityList, WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseNewsTaskCompleted, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.ArticlesEntityList = articlesRoomEntityList;
        this.onWebHoseNewsTaskCompleted=onWebHoseNewsTaskCompleted;
        this.onNewsTaskCompleted=null;
        this.CategoryKey=Key_Type;
        articlesEntity=new ArticlesEntity();
        Config.Listener=WebHoseAPIKEY;
    }
}