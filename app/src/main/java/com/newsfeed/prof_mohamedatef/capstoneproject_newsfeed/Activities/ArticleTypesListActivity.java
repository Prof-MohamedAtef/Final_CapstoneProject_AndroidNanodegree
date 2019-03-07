package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.app.ActivityOptions;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.BuildConfig;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.ArticlesMasterListFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.FragmentArticleViewer;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.FragmentUrgentArticles;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NoInternetFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.AppExecutors;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao.ArticlesDao;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.UrgentArticlesViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.ARTS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.ArticleType;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.BUSINESS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.FAMILY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.FOOD;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.HERITAGE;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.OPINIONS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.POLITICS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.REPORTS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.SPORTS;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.TECHNOLOGY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

public class ArticleTypesListActivity extends AppCompatActivity implements
        ArticlesMasterListFragment.OnSelectedArticleListener,
        ArticlesMasterListFragment.OnFirebaseArticleSelectedListener,
        SwipeRefreshLayout.OnRefreshListener{

    public static String WebHoseVerifier="null", NewsApiVerifier="null";
    private String URL;
    private boolean mTwoPaneUi=false;
    private static final String WEBHOSE = "http://webhose.io/filterWebContent?token=";
    private String NEWSAPI="https://newsapi.org/v2/top-headlines?country=eg&category=";
    private String WEBHOSEDETAILS="sort=crawled&q=thread.country%3AEG%20language%3Aarabic%20site_type%3Anews%20thread.";
    private String token;
    private String apiKey;
    public static String URL_KEY="URL_KEY";
    public static String NEWSAPI_KEY="NEWSAPIKEY";
    public static String WebHoseAPIKEY="WenHoseAPIKEY";
    public static String CategoryName;
    private String Arts;
    private String ArticleType_;
    public static String TwoPANEExtras_KEY="twoPaneExtras";
    private String Position_KEY="position";
    private String ArticleInfo_KEY="ArticleInfo";
    public static final String Frags_KEY="frags";
    private String SoundFrag_KEY="Sound";
    private String ArticleFrag_KEY="Article";

    SwipeRefreshLayout mSwipeRefreshLayout;
    private int Activity_Num=1;
    NoInternetFragment noInternetFragment;
    @BindView(R.id.master_list_fragment)
    FrameLayout master_list_fragment;
    private boolean ContentProviderHasData;
    public static String Firebase_KEY="Firebase_KEY";
    public static String Flag_KEY="flag";
    public static String CATEGORY_NAME="CATEGORY_NAME";
    private Toolbar mToolbar;
    private FragmentSoundPlayer fragmentSoundPlayer;
    private FragmentArticleViewer fragmentArticleViewer;
    private FragmentUrgentArticles fragmentUrgentArticles;
    private UrgentArticlesViewModel urgentArticlesViewModel;
    private AppDatabase mDatabase;
    private AppExecutors mAppExecutors;
    private LiveData<List<ArticlesEntity>> UrgentArticlesListLiveData;
    public static String OtherTypes_KEY="OtherTypes_KEY";

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_types_list);
        ButterKnife.bind(this);
        Config.RetrieveFirebaseData=false;
        noInternetFragment=new NoInternetFragment();
        fragmentSoundPlayer=new FragmentSoundPlayer();
        fragmentArticleViewer=new FragmentArticleViewer();
        fragmentUrgentArticles=new FragmentUrgentArticles();
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public Intent intent;
            @Override
            public void onClick(View v) {
                Config.ActivityNum = 0;
                onBackPressed();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ArticleTypesListActivity.this);
                    intent = new Intent(ArticleTypesListActivity.this, HomeActivity.class);
                    // Apply activity transition
                    startActivity(intent, options.toBundle());
                } else {
                    // Swap without transition
                    startActivity(intent);
                }
                finish();
            }
        });
        if (findViewById(R.id.coordinator_layout_twoPane)!=null) {
            mTwoPaneUi = true;
        } else {
            mTwoPaneUi = false;
        }
        Config.ActivityNum=Activity_Num;
        token= BuildConfig.token;
        apiKey= BuildConfig.ApiKey;
        Bundle bundle=getIntent().getExtras();
        ArticleType_=bundle.getString(ArticleType);
        if (ArticleType_.equals(ARTS)){
            URL=WEBHOSE+token+"&format=json&ts=1543864086443&"+WEBHOSEDETAILS+"title%3A%D9%81%D9%86%D9%88%D9%86";
            WebHoseVerifier=URL;
            CategoryName=ARTS;
        }else if (ArticleType_.equals(POLITICS)){
            URL=WEBHOSE+token+"&format=json&ts=1543864001127&"+WEBHOSEDETAILS+"title%3A%D8%B3%D9%8A%D8%A7%D8%B3%D8%A9";
            WebHoseVerifier=URL;
            CategoryName=POLITICS;
        }else if (ArticleType_.equals(SPORTS)){
            URL=NEWSAPI+"sports&apiKey="+apiKey;
            NewsApiVerifier=URL;
            CategoryName=SPORTS;
        }else if (ArticleType_.equals(REPORTS)){
            // get data from Content Provider or Firebase
//            VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
//            verifyConnection.checkConnection();
//            if (verifyConnection.isConnected()){
                // get data from firebase
                Config.RetrieveFirebaseData=true;
            CategoryName=REPORTS;
//            }else {
                //if no data in content provider // redirect to add article activity
                // Show Snack
//                ContentProviderHasData=false;
//                if (!ContentProviderHasData){
//                    snackbar=NetCut();
//                    snackBarLauncher.SnackBarInitializer(snackbar);
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container_frame, noInternetFragment, "newsApi")
//                            .commit();
//                }
//            }
        }else if (ArticleType_.equals(FOOD)){
            URL=WEBHOSE+token+"&format=json&ts=1543863885301&"+WEBHOSEDETAILS+"title%3A";
            WebHoseVerifier=URL;
            CategoryName=FOOD;
        }else if (ArticleType_.equals(FAMILY)){
            URL=WEBHOSE+token+"&format=json&ts=1545130799659&"+WEBHOSEDETAILS+"title%3A%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9";
            WebHoseVerifier=URL;
            CategoryName=FAMILY;
        }else if (ArticleType_.equals(HERITAGE)){
            URL=WEBHOSE+token+"&format=json&ts=1543863771070&"+WEBHOSEDETAILS+"title%3A%D8%AA%D8%B1%D8%A7%D8%AB";
            WebHoseVerifier=URL;
            CategoryName=HERITAGE;
        }else if (ArticleType_.equals(OPINIONS)){
            URL=WEBHOSE+token+"&format=json&ts=1543852898977&"+WEBHOSEDETAILS+"title%3A%D8%A2%D8%B1%D8%A7%D8%A1";
            WebHoseVerifier=URL;
            CategoryName=OPINIONS;
        }else if (ArticleType_.equals(TECHNOLOGY)){
            URL=NEWSAPI+"technology&apiKey="+apiKey;
            NewsApiVerifier=URL;
            CategoryName=TECHNOLOGY;
        }else if (ArticleType_.equals(BUSINESS)){
            URL=NEWSAPI+"business&apiKey="+apiKey;
            NewsApiVerifier=URL;
            CategoryName=BUSINESS;
        }

        Bundle bundle2=new Bundle();
        bundle2.putString(URL_KEY,URL);
        bundle2.putString(NEWSAPI_KEY,NewsApiVerifier);
        bundle2.putString(WebHoseAPIKEY,WebHoseVerifier);
        bundle2.putBoolean(TwoPANEExtras_KEY,mTwoPaneUi);
        bundle2.putString(CATEGORY_NAME,CategoryName);
        ArticlesMasterListFragment articlesMasterListFragment= new ArticlesMasterListFragment();
        articlesMasterListFragment.setArguments(bundle2);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.master_list_fragment, articlesMasterListFragment, Frags_KEY)
                .commit();

        fragmentUrgentArticles.setArguments(bundle2);
        if (findViewById(R.id.coordinator_layout_twoPane)!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, fragmentSoundPlayer, Frags_KEY)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, fragmentArticleViewer, Frags_KEY)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_urgent, fragmentUrgentArticles, Frags_KEY)
                        .commit();
        }
    }

    @Override
    public void onArticleSelected(ArticlesEntity articlesEntity, boolean TwoPane, int position) {
        if (mTwoPaneUi) {
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putBoolean(TwoPANEExtras_KEY,TwoPane);
            twoPaneExtras.putSerializable(OtherTypes_KEY, articlesEntity);
            twoPaneExtras.putInt(Position_KEY,position);
            fragmentSoundPlayer=new FragmentSoundPlayer();
            fragmentArticleViewer=new FragmentArticleViewer();
            fragmentSoundPlayer.setArguments(twoPaneExtras);
            fragmentArticleViewer.setArguments(twoPaneExtras);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, fragmentSoundPlayer, SoundFrag_KEY)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, fragmentArticleViewer, ArticleFrag_KEY)
                    .commit();
        }else if (!mTwoPaneUi){
            Intent intent = new Intent(this, ArticleDetailsActivity.class)
                    .putExtra(ArticleInfo_KEY, articlesEntity)
                    .putExtra(Position_KEY, position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getApplicationContext(),getString(R.string.okay), Toast.LENGTH_LONG);
    }

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(master_list_fragment, getApplicationContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getApplicationContext().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SnackBasedConnection();
                    }
                });

    }

    private void SnackBasedConnection() {
        VerifyConnection verifyConnection=new VerifyConnection(getApplicationContext());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            // get fata from firebase
        }else {
            //if no data in content provider
            // Show Snack
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_frame, noInternetFragment, "newsApi")
                    .commit();
        }
    }

    Snackbar snackbar;
    SnackBarClassLauncher snackBarLauncher;



    @Override
    public void onFirebaseArticleSelected(FirebaseDataHolder firebaseDataHolder, boolean TwoPane, int position) {
        if (mTwoPaneUi) {
            Config.RetrieveFirebaseData=true;
            Bundle twoPaneExtras = new Bundle();
            twoPaneExtras.putSerializable(KEY_FIREBASE,firebaseDataHolder);
            twoPaneExtras.putBoolean(TwoPANEExtras_KEY, TwoPane);
            twoPaneExtras.putInt(Position_KEY,position);
            FragmentSoundPlayer soundPlayer=new FragmentSoundPlayer();
            FragmentArticleViewer articleViewer=new FragmentArticleViewer();
            soundPlayer.setArguments(twoPaneExtras);
            articleViewer.setArguments(twoPaneExtras);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Audio_container, soundPlayer, SoundFrag_KEY)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Article_container, articleViewer, ArticleFrag_KEY)
                    .commit();
        }else if (!mTwoPaneUi){
            Intent intent = new Intent(this, ArticleDetailsActivity.class)
                    .putExtra(ArticleInfo_KEY, firebaseDataHolder)
                    .putExtra(Position_KEY, position);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
    }
}