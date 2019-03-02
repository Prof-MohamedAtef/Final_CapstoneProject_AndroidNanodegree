package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.BuildConfig;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.NewsApiFragment;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.NoInternetFragment;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.WebhoseApiFragment;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.AppExecutors;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Network.SnackBarClassLauncher;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Network.VerifyConnection;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.OptionsEntity;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.Dao.ArticlesDao;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.SessionManagement;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments.NewsApiFragment.KEY_Urgent;

public class HomeActivity extends AppCompatActivity implements NewsApiFragment.NewsApiSelectedArticleListener,
NoInternetFragment.onReloadInternetServiceListener{

    private final String LOG_TAG = HomeActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    Snackbar snackbar;

    SnackBarClassLauncher snackBarLauncher;
    private Handler handler;
    private Toolbar toolbar;
    ImageView LogoImage;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    Bundle webServiceNewsApi=null;
    Bundle webServiceWebHose=null;
    String apiKey,token;
    NewsApiFragment newsApiFragment;
    NoInternetFragment noInternetFragment;
    WebhoseApiFragment webhoseApiFragment;
    public static String POLITICS="Politics";
    public static String ARTS="arts";
    public static String SPORTS="Sports";
    public static String REPORTS="reports";
    public static String FOOD="food";
    public static String FAMILY="family";
    public static String HERITAGE="heritage";
    public static String OPINIONS="opinions";
    public static String TECHNOLOGY="technology";
    public static String BUSINESS="business";
    public static String UrgentURL="https://newsapi.org/v2/top-headlines?country=eg&categor=%D8%B9%D8%A7%D8%AC%D9%84&apiKey=";
    public static String POLITICS_URL="http://webhose.io/filterWebContent?token=43939f70-364f-4f3c-9c4f-84ac4f5ece38&format=json&ts=1543864001127&sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
    public static String ArticleType="ArticleType";
    private SessionManagement sessionManagement;
    private HashMap<String, String> user;
    private String TokenID;
    private String LoggedEmail;
    private String LoggedUserName;
    private String LoggedProfilePic;
    private TextView EmailText;
    private TextView UserNameText;
    private ImageView ProfilePicView;
    private int Activity_Num=0;
    View view;
    private String Urgent_KEY="urgent";
    private Cursor cursor;
    private boolean HasSavedData;
    private LiveData<List<ArticlesEntity>> UrgentArticlesListLiveData;
    private AppDatabase mDatabase;
    private AppExecutors mAppExecutors;
//    RelativeLayout home_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
        setContentView(R.layout.activity_home);
        setTheme(R.style.ArishTheme);
//        home_linear=(RelativeLayout)findViewById(R.id.home_linear);
        Config.ActivityNum=Activity_Num;
        apiKey= BuildConfig.ApiKey;
        token= BuildConfig.token;
        mDatabase =new AppDatabase() {
            @Override
            public ArticlesDao articlesDao() {
                return null;
            }
            @Override
            public void clearAllTables() {

            }
        };
        mAppExecutors = new AppExecutors();
        mDatabase= AppDatabase.getAppDatabase(getApplicationContext(),mAppExecutors);
        newsApiFragment=new NewsApiFragment();
        noInternetFragment=new NoInternetFragment();
        snackBarLauncher=new SnackBarClassLauncher();
        webhoseApiFragment=new WebhoseApiFragment();
        webServiceNewsApi=new Bundle();
        webServiceWebHose=new Bundle();
        handler = new Handler();
        final Bundle bundle=new Bundle();
//        sessionManagement=new SessionManagement(getApplicationContext());
//        user=sessionManagement.getUserDetails();
//        if (user!=null){
//            LoggedEmail = user.get(SessionManagement.KEY_EMAIL);
//            LoggedUserName=user.get(SessionManagement.KEY_NAME);
//            LoggedProfilePic=user.get(SessionManagement.KEY_Profile_Pic);
//            TokenID=user.get(SessionManagement.KEY_idToken);
//            if (LoggedEmail!=null){
//                EmailText.setText(LoggedEmail);
//            }
//            if (LoggedUserName!=null){
//                UserNameText.setText(LoggedUserName);
//            }
//            if (LoggedProfilePic!=null){
//                Picasso.with(getApplicationContext()).load(LoggedProfilePic)
//                        .error(R.drawable.news)
//                        .into(ProfilePicView);
//            }
//        }
        SnackBasedConnection();
    }

//    private boolean checkSavedOfflineData(){
//        cursor = managedQuery(CONTENT_URI, null, null, null, NewsProvider.CATEGORY);
//        if (cursor.moveToFirst()) {
//            // THERE ARE SAVED DATA
//            return true;
//        }else {
//            return false;
//        }
//    }
//
    private void SnackBasedConnection() {
//        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
//        verifyConnection.checkConnection();
//        if (verifyConnection.isConnected()){
            displayUrgent();
//        }
//        else {
//            UrgentArticlesListLiveData=mDatabase.articlesDao().getArticlesDataByCategory(KEY_Urgent);
//            UrgentArticlesListLiveData.observe((LifecycleOwner)this, UrgentList -> {
//                if (UrgentList!=null&&!UrgentList.isEmpty()&&UrgentList.size()>0) {
//                        displayUrgent();
//                    }
//                    else {
//                    snackbar = NetCut();
//                    snackBarLauncher.SnackBarInitializer(snackbar);
//                    Config.UrgentURL = UrgentURL;
//                    Config.apiKey = apiKey;
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container_frame, noInternetFragment, "newsApi")
//                            .commit();
//                }
//            });

//            HasSavedData=checkSavedOfflineData();
//            if (HasSavedData){
//                displayUrgent();
//            }else {
//                snackbar=NetCut();
//                snackBarLauncher.SnackBarInitializer(snackbar);
//                Config.UrgentURL=UrgentURL;
//                Config.apiKey=apiKey;
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container_frame, noInternetFragment, "newsApi")
//                        .commit();
//            }
//        }
    }
//
//    private void startSharedElementTransition(ImageView profileImage, Intent intent){
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, profileImage, getResources().getString(R.string.profile_img));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            startActivity(intent, options.toBundle());
//        }else {
//            startActivity(intent);
//        }
//    }

//    private void startExplodeTransition(Intent intent){
//        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this);
////        intent= new Intent(HomeActivity.this,context);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // Apply activity transition
//            startActivity(intent, options.toBundle());
//        } else {
//            // Swap without transition
//            startActivity(intent);
//        }
//        finish();
//    }

    private void displayUrgent() {
        if (UrgentURL!=null&&apiKey!=null){
            webServiceNewsApi.putString(Urgent_KEY,UrgentURL+apiKey);
        }else {
            webServiceNewsApi.putString(Urgent_KEY,Config.UrgentURL+Config.apiKey);
        }
//        webServiceNewsApi.putString("urgent",POLITICS_URL);
        newsApiFragment.setArguments(webServiceNewsApi);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_frame, newsApiFragment, "newsApi")
                .commit();
    }

        private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(drawerLayout, getApplicationContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getApplicationContext().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SnackBasedConnection();
                    }
                });

    }

    @Override
    public void ReloadInternetService() {
        SnackBasedConnection();
    }

    @Override
    public void onNewsApiArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position) {

    }
}