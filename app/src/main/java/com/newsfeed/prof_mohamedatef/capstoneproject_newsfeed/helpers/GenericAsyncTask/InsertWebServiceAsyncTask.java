package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import java.util.ArrayList;
import java.util.List;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.WebHoseAPIKEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment.KEY_Urgent;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment.NewsApiUrgentListenet_KEY;


/**
 * Created by Prof-Mohamed Atef on 2/26/2019.
 */

public class InsertWebServiceAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseNewsTaskCompleted;
    private UrgentAsyncTask.OnNewsUrgentTaskCompleted onUrgentNewsTaskCompleted;
    public static String NULL_KEY="Someone";

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
        if (onUrgentNewsTaskCompleted!=null) {
            articlesRoomList.observe((LifecycleOwner) onUrgentNewsTaskCompleted, existingArticles -> {
                DeleteInsertOperations(Inserted, existingArticles, null, null, onUrgentNewsTaskCompleted);
            });
        } else if (onNewsTaskCompleted!=null) {
            articlesRoomList.observe((LifecycleOwner) onNewsTaskCompleted, existingArticles -> {
                DeleteInsertOperations(Inserted, existingArticles, onNewsTaskCompleted, null, null);
            });
        } else if (onWebHoseNewsTaskCompleted!=null) {
            articlesRoomList.observe((LifecycleOwner) onWebHoseNewsTaskCompleted, existingArticles -> {
                DeleteInsertOperations(Inserted, existingArticles, null, onWebHoseNewsTaskCompleted, null);
            });
        }
        return Inserted[0];
    }

    private void DeleteInsertOperations(boolean[] inserted, List<ArticlesEntity> existingArticles, NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted, WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseTaskCompleted, UrgentAsyncTask.OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted) {
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
                    articlesRoomList.removeObservers((LifecycleOwner) onNewsTaskCompleted);
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
        }else if (onNewsUrgentTaskCompleted!=null){
            if (existingArticles!=null&&existingArticles.size()>0&&!existingArticles.isEmpty()){
                int deleted=appDatabase.articlesDao().deleteByCATEGORY(CategoryKey);
                if (deleted>0){
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onNewsUrgentTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }else {
                    inserted[0] =IsArrayInserted();
                    if (inserted[0]){
                        inserted[0] =true;
                        articlesRoomList.removeObservers((LifecycleOwner) onNewsUrgentTaskCompleted);
                    }else {
                        inserted[0] =false;
                    }
                }
            }else {
                inserted[0] =IsArrayInserted();
                if (inserted[0]){
                    inserted[0] =true;
                    articlesRoomList.removeObservers((LifecycleOwner) onNewsUrgentTaskCompleted);
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
            if (onNewsTaskCompleted!=null) {
                onNewsTaskCompleted.onNewsApiTaskCompleted(ArticlesEntityList_x);
            }else if (onWebHoseNewsTaskCompleted!=null){
                onWebHoseNewsTaskCompleted.onWebHoseTaskCompleted(ArticlesEntityList_x);
            }else if (onUrgentNewsTaskCompleted!=null){
                onUrgentNewsTaskCompleted.onNewsUrgentApiTaskCompleted(ArticlesEntityList_x);
            }
//            else {
//                onNewsTaskCompleted.onNewsApiTaskCompleted(ArticlesEntityList);
//            }
        }
    }

    public InsertWebServiceAsyncTask(AppDatabase appDatabase, ArrayList<ArticlesEntity> articlesRoomEntityList, NewsApiAsyncTask.OnNewsTaskCompleted onTaskCompletes, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.ArticlesEntityList = articlesRoomEntityList;
        this.onNewsTaskCompleted=onTaskCompletes;
        this.CategoryKey=Key_Type;
        articlesEntity=new ArticlesEntity();
        Config.NewsAPIListener =NEWSAPI_KEY;
    }

    public InsertWebServiceAsyncTask(AppDatabase appDatabase, ArrayList<ArticlesEntity> articlesRoomEntityList, UrgentAsyncTask.OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.ArticlesEntityList = articlesRoomEntityList;
        this.onUrgentNewsTaskCompleted=onNewsUrgentTaskCompleted;
        this.CategoryKey=Key_Type;
        articlesEntity=new ArticlesEntity();
        Config.NewsApiUrgentListener =NewsApiUrgentListenet_KEY;
    }

    public InsertWebServiceAsyncTask(AppDatabase appDatabase, ArrayList<ArticlesEntity> articlesRoomEntityList, WebHoseApiAsyncTask.OnWebHoseTaskCompleted onWebHoseNewsTaskCompleted, String Key_Type) {
        super();
        this.appDatabase=appDatabase;
        this.ArticlesEntityList = articlesRoomEntityList;
        this.onWebHoseNewsTaskCompleted=onWebHoseNewsTaskCompleted;
        this.CategoryKey=Key_Type;
        articlesEntity=new ArticlesEntity();
        Config.WebHoseListener =WebHoseAPIKEY;
    }
}