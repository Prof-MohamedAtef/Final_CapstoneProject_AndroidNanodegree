package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.BuildConfig;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NoInternetFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.AppExecutors;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.OptionsEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao.ArticlesDao;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.SessionManagement;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment.KEY_Urgent;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        NewsApiFragment.NewsApiSelectedArticleListener,
NoInternetFragment.onReloadInternetServiceListener,
        GoogleApiClient.OnConnectionFailedListener{

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
    private String LoggedType;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
//    RelativeLayout home_linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTheme(R.style.ArishTheme);
        Config.ActivityNum=Activity_Num;
        apiKey= BuildConfig.ApiKey;
        token= BuildConfig.token;
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        if (verifyConnection.isConnected()){
            mAuth=FirebaseAuth.getInstance();
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
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
        handler = new Handler();
        newsApiFragment=new NewsApiFragment();
        noInternetFragment=new NoInternetFragment();
        snackBarLauncher=new SnackBarClassLauncher();
        webServiceNewsApi=new Bundle();
        webServiceWebHose=new Bundle();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        EmailText=(TextView)header.findViewById(R.id.Email);
        UserNameText=(TextView)header.findViewById(R.id.UserName);
        ProfilePicView=(ImageView)header.findViewById(R.id.profile_image);
        final Bundle bundle=new Bundle();
        sessionManagement=new SessionManagement(getApplicationContext());
        user=sessionManagement.getUserDetails();
        if (user!=null){
            LoggedEmail = user.get(SessionManagement.KEY_EMAIL);
            LoggedUserName=user.get(SessionManagement.KEY_NAME);
            LoggedProfilePic=user.get(SessionManagement.KEY_Profile_Pic);
            TokenID=user.get(SessionManagement.KEY_idToken);
            if (LoggedEmail!=null){
                EmailText.setText(LoggedEmail);
            }
            if (LoggedUserName!=null){
                UserNameText.setText(LoggedUserName);
            }
            if (LoggedProfilePic!=null){
                Picasso.with(getApplicationContext()).load(LoggedProfilePic)
                        .error(R.drawable.news)
                        .into(ProfilePicView);
            }
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    case R.id.publish:
                        Intent intent=new Intent(getApplicationContext(),PublishToNewsFeed.class);
                        startExplodeTransition(intent);
//                        startActivity(intent);
//                        startSharedElementTransition(ProfilePicView, intent);
                        return true;
                    case R.id.urgent:
                        displayUrgent();
                        return true;
                    case R.id.politics:
                        bundle.putString(ArticleType,POLITICS);
                        Intent intent2=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent2.putExtras(bundle);
//                        startActivity(intent2);
                        startExplodeTransition(intent2);
                        return true;
                    case R.id.art_culture:
                        bundle.putString(ArticleType,ARTS);
                        Intent intent3=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent3.putExtras(bundle);
                        startExplodeTransition(intent3);
//                        startActivity(intent3);

                        return true;
                    case R.id.sports:
                        bundle.putString(ArticleType,SPORTS);
                        Intent intent4=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent4.putExtras(bundle);
//                        startActivity(intent4);
                        startExplodeTransition(intent4);
                        return true;
                    case R.id.reports:
                        bundle.putString(ArticleType,REPORTS);
                        Intent intent5=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent5.putExtras(bundle);
//                        startActivity(intent5);
                        startExplodeTransition(intent5);
                        // get data from content provider or firebase
                        return true;
                    case R.id.food:
                        bundle.putString(ArticleType,FOOD);
                        Intent intent1=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent1.putExtras(bundle);
//                        startActivity(intent1);
                        startExplodeTransition(intent1);
                        return true;
                    case R.id.family:
                        bundle.putString(ArticleType,FAMILY);
                        Intent intent6=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent6.putExtras(bundle);
//                        startActivity(intent6);
                        startExplodeTransition(intent6);
                        return true;
                    case R.id.heritage:
                        bundle.putString(ArticleType,HERITAGE);
                        Intent intent7=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent7.putExtras(bundle);
//                        startActivity(intent7);
                        startExplodeTransition(intent7);
                        return true;
                    case R.id.opinions:
                        bundle.putString(ArticleType,OPINIONS);
                        Intent intent8=new Intent(getApplicationContext(),ArticleTypesListActivity.class);
                        intent8.putExtras(bundle);
//                        startActivity(intent8);
                        startExplodeTransition(intent8);
                        return true;
                    case R.id.technology:
                        bundle.putString(ArticleType,TECHNOLOGY);
                        Intent intent9=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent9.putExtras(bundle);
//                        startActivity(intent9);
                        startExplodeTransition(intent9);
                        return true;
                    case R.id.business:
                        bundle.putString(ArticleType,BUSINESS);
                        Intent intent10=new Intent(getApplicationContext(), ArticleTypesListActivity.class);
                        intent10.putExtras(bundle);
//                        startActivity(intent10);
                        startExplodeTransition(intent10);
//                        webServiceNewsApi.putString("business","https://newsapi.org/v2/top-headlines?country=eg&category=business&apiKey="+apiKey);
//                        newsApiFragment.setArguments(webServiceNewsApi);
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container_frame, newsApiFragment, "newsApi")
//                                .commit();
                        return true;
                    case R.id.logout:
                        SignOut();
                        return  true;
                    default:
                        return true;
                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.openDrawer, R.string.closeDrawer){
            //            @Override
//            public boolean onOptionsItemSelected(MenuItem item) {
//                if (item != null && item.getItemId() == android.R.id.home) {
//                    if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
//                        drawerLayout.closeDrawer(Gravity.RIGHT);
//                    }
//                    else {
//                        drawerLayout.openDrawer(Gravity.RIGHT);
//                    }
//                }
//                return false;
//            }
//
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        SnackBasedConnection();
    }

    private void SignOut() {
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        if (verifyConnection.isConnected()){
            user =sessionManagement.getLoginType();
            if (user!=null){
                LoggedType = user.get(SessionManagement.KEY_LoginType);
                if (LoggedType!=null){
                    if (LoggedType.equals("G")){
                        mAuth.signOut();
                        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                if (status.isSuccess()){
                                    sessionManagement.logoutUser();
                                }
                            }
                        });
                    }else if (LoggedType.equals("F")){
                        LoginManager.getInstance().logOut();
                        sessionManagement.logoutUser();
                    }else if (LoggedType.equals("EP")){
                        mAuth = FirebaseAuth.getInstance();
                        if (mAuth.getCurrentUser() != null) {
                            mAuth.signOut();
                            sessionManagement.logoutUser();
                        }
                    }
                }
            }
        }else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    private void SnackBasedConnection() {
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            displayUrgent();
        }
        else {
            UrgentArticlesListLiveData=mDatabase.articlesDao().getArticlesDataByCategory(KEY_Urgent);
            UrgentArticlesListLiveData.observe((LifecycleOwner)this, UrgentList -> {
                if (UrgentList!=null&&!UrgentList.isEmpty()&&UrgentList.size()>0) {
                        displayUrgent();
                    }
                    else {
                    snackbar = NetCut();
                    snackBarLauncher.SnackBarInitializer(snackbar);
                    Config.UrgentURL = UrgentURL;
                    Config.apiKey = apiKey;
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_frame, noInternetFragment, "newsApi")
                            .commit();
                }
            });
        }
    }
//
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startSharedElementTransition(ImageView profileImage, Intent intent){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, profileImage, getResources().getString(R.string.profile_img));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent, options.toBundle());
        }else {
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startExplodeTransition(Intent intent){
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            startActivity(intent, options.toBundle());
        } else {
            // Swap without transition
            startActivity(intent);
        }
        finish();
    }

    private void displayUrgent() {
        if (UrgentURL!=null&&apiKey!=null){
            webServiceNewsApi.putString(Urgent_KEY,UrgentURL+apiKey);
        }else {
            webServiceNewsApi.putString(Urgent_KEY,Config.UrgentURL+Config.apiKey);
        }
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
        Intent intent = new Intent(this, ArticleTypesListActivity.class);
        Config.position=position;
        if (TwoPane){
            intent.putExtra("TwoPane",TwoPane);
            intent.putExtra("optionsEntity", optionsEntity);
            intent.putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }else {
            intent.putExtra("TwoPane",TwoPane);
            intent.putExtra("optionsEntity", optionsEntity);
            intent.putExtra("position", position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}