package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers;

import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.NewsApiFragment;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Prof-Mohamed Atef on 1/1/2019.
 */

public class Config {
    private static String TAG = Config.class.toString();
    public static Context mContext;
    public static CoordinatorLayout mCoordinatorLayout;
    public static LinearLayout activityLinearHome;
    public static boolean TwoPane;
    public static int position;
    public static String currentImagePAth;
    public static ImageView imageViewPlay;
    public static String imageBitmap;
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
    public static NewsApiFragment Listener;
    public static AppDatabase mDatabase;
    public static LinearLayout LinearUiIdentifier;
}
