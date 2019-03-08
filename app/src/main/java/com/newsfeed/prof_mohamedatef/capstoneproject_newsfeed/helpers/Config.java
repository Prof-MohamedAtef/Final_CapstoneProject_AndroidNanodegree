package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PostToNewsFeedActivity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.ArticlesMasterListFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseImageHelper;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 1/1/2019.
 */

public class Config {
    private static String TAG = Config.class.toString();
    public static Context mContext;
    public static CoordinatorLayout mCoordinatorLayout;
    public static LinearLayout activityLinearHome;
    public static boolean TwoPane;
    public static int position=0;
    public static String currentImagePAth;
    public static ImageView imageViewPlay;
    public static String imageBitmap;
    public static Bitmap Bitmap_;
    public static String image_name;
    public static String selectedImagePath;
    public static File StorageDir;
    public static ArrayList<ArticlesEntity> ArrArticle;
    public static int ActivityNum;
    public static int FragmentNewsApiNum;
    public static int FragmentWebHoseApiNum;
    public static int FragmentFirebaseApiNum;
    public static String UrgentURL;
    public static String URL;
    public static String apiKey;
    public static ArrayList<String> CategoriesList;
    public static int Category_id;
    public static boolean RetrieveFirebaseData=false;
    public static int RecyclerPosition;
    public static String CategoryName;
    public static Application application;
    public static NewsApiFragment NewsApiFragment;
    public static ArticlesMasterListFragment ArticlesMasterListFragment;
    public static AppDatabase mDatabase;
    public static LinearLayout LinearUiIdentifier;
    public static NewsApiAsyncTask.OnNewsTaskCompleted onNewsTaskCompleted;
    public static String Listener;
    public static PublishToNewsFeed POSTActivity;
    public static AccessToken FBAccessToken;
    public static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder FirebaseDataHolder;
    public static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseImageHelper FirebaseImageHelper;
    public static List<FirebaseDataHolder> FirebaseArticlesList;
    public static Uri AudioUri;
    public static ArticlesEntity ArticlesEntity;
    public static Uri ImageFileUri;
    public static String AudioFilePath;
    public static int PlayedNum=0;
    public static boolean Playing;
    public static int lastProgress;
    public static int Pos;
}
