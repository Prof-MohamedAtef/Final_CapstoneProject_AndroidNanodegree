package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.FirebaseRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.NewsApiRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.UrgentNewsAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.WebHoseRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.BuildConfig;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.AppExecutors;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.InsertLocallyFirebaseAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.UrgentAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao.ArticlesDao;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Helpers.InsertClass;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.FirebaseViewModel;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.TypeArticlesViewModel;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.UrgentArticlesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.CATEGORY_NAME;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.WebHoseAPIKEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.HomeActivity.UrgentURL;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment.KEY_Urgent;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.NewsApiFragment.NewsApiUrgentListenet_KEY;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class ArticlesMasterListFragment extends Fragment implements
        NewsApiAsyncTask.OnNewsTaskCompleted,
        UrgentAsyncTask.OnNewsUrgentTaskCompleted,
        WebHoseApiAsyncTask.OnWebHoseTaskCompleted,
        InsertLocallyFirebaseAsyncTask.OnFirebaseInsertedLocallyCompleted
{

    private AppDatabase mRoomDatabase;
    private AppExecutors mAppExecutors;
    String apiKey;
    private String URL;
    private RecyclerView recyclerView;
    private boolean TwoPane;
    private String NULL_KEY="Someone";
    private String NEWSAPI_CATEGORY="NewsApi";
    private NewsApiAsyncTask newsApiAsyncTask;
    private String KEY;
    private String Category_STR;
    private String Description_STR;
    private String ImageFile_STR;
    private String Title_STR;
    private String Email_STR;
    public static String ArticlesList_KEY="ArticlesList_KEY";
    private String AudioFile_STR;
    private String UserName_STR;
    private String AudioFile_KEY="audioFileUri";
    private String Category_KEY="category_id";
    private String Description_KEY="description";
    private String IMAGE_FILE_KEY="imageFileUri";
    private String TITLE_KEY="title";
    private String Token_STR;
    private String TokenID_KEY="token_id";
    private String Email_KEY="user_email";
    private String UserName_KEY="user_name";
    private String Data_KEY="data";
    private String LOG_TAG="TAG";
    ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
        RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog dialog;
    private String Date_KEY="date";
    private String Date_STR;
    private Cursor cursor;
    private String CategoryName;
    private TypeArticlesViewModel typeArticlesViewModel;
    private LiveData<List<ArticlesEntity>> ArticlesListLiveData;
    private List<FirebaseDataHolder> FirebaseArticlesList;
    private FirebaseDataHolder firebaseDataHolder;
    private DatabaseReference mDatabase;
    private FirebaseViewModel firebaseViewModel;
    private String KEY_POSITION_TYPES="KEY_POSITION_TYPES";
    private java.lang.String KEY_POSITION_FIREBASE="KEY_POSITION_FIREBASE";
    private String KEY_POSITION_WebHose="KEY_POSITION_WebHose";
    private String KEY_POSITION_NewFragment="KEY_POSITION_NewFragment";
    private UrgentArticlesViewModel urgentArticlesViewModel;
    private LiveData UrgentArticlesListLiveData;
    private ArrayList<ArticlesEntity> UrgentArticlesList;
    private RecyclerView recyclerView_Horizontal;
    private UrgentAsyncTask urgentAsyncTask;
    private SnackBarClassLauncher snackBarLauncher;
    Snackbar snackbar;

    public ArticlesMasterListFragment(){

    }

    private ArrayList<ArticlesEntity> TypesArticlesList;
    private String WebHoseVerifier, NewsApiVerifier;
    private String KEY_ArticleTypeArray="ArticleTypeArr";
    private String MustImplementListener="must Implement OnSelectedArticleListener";
    private int FragmentNewsApiNum=11;
    private int FragmentWebHoseApiNum=22;
    private int FragmentFirebaseApiNum=33;

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            Config.positionFirebase= savedInstanceState.getInt(KEY_POSITION_FIREBASE);
            Config.positionWebHose= savedInstanceState.getInt(KEY_POSITION_WebHose);
            Config.PosNewsFragment= savedInstanceState.getInt(KEY_POSITION_NewFragment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Config.RetrieveFirebaseData){
            outState.putSerializable(KEY_FIREBASE, firebaseDataHolder);
        }
        if (TypesArticlesList!=null){
            outState.putSerializable(KEY_ArticleTypeArray, TypesArticlesList);
        }
        if (UrgentArticlesList!=null){
            outState.putSerializable(KEY_Urgent, UrgentArticlesList);
        }

        outState.putInt(KEY_POSITION_FIREBASE,Config.positionFirebase);
        outState.putInt(KEY_POSITION_WebHose,Config.positionWebHose);
        outState.putInt(KEY_POSITION_NewFragment,Config.PosNewsFragment);
    }

    public List<FirebaseDataHolder> FetchDataFromFirebase() {
        DatabaseReference ThoughtsRef   =mDatabase.child(Data_KEY);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseArticlesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    KEY=ds.getKey();
                    AudioFile_STR = ds.child(AudioFile_KEY).getValue(String.class);
                    Category_STR = ds.child(Category_KEY).getValue(String.class);
                    Date_STR = ds.child(Date_KEY).getValue(String.class);
                    Description_STR = ds.child(Description_KEY).getValue(String.class);
                    ImageFile_STR= ds.child(IMAGE_FILE_KEY).getValue(String.class);
                    Title_STR= ds.child(TITLE_KEY).getValue(String.class);
                    Token_STR= ds.child(TokenID_KEY).getValue(String.class);
                    Email_STR= ds.child(Email_KEY).getValue(String.class);
                    UserName_STR= ds.child(UserName_KEY).getValue(String.class);
                    Log.d(LOG_TAG, AudioFile_STR+ " / " + Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR+ " / " + Token_STR+ " / " + UserName_STR);
                    firebaseDataHolder=new FirebaseDataHolder(KEY, Title_STR, Description_STR, Category_STR, Token_STR, AudioFile_STR , ImageFile_STR, Date_STR, UserName_STR,Email_STR);
                    FirebaseArticlesList.add(firebaseDataHolder);
                    /*
                    insert in Room
                     */
                }

                InsertClass insertClass = new InsertClass();
                insertClass.TryInsertFirebaseReportList(mRoomDatabase, FirebaseArticlesList, ArticlesMasterListFragment.this, CategoryName);
                if (FirebaseArticlesList.size()>0){
                    PopulateFirebaseList(FirebaseArticlesList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
        return FirebaseArticlesList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        Config.mContext=getActivity();

        apiKey= BuildConfig.ApiKey;
        Config.ArticlesMasterListFragment=this;
        Config.onNewsTaskCompleted=this;
        URL= bundle.getString(URL_KEY);
        mRoomDatabase =new AppDatabase() {
            @Override
            public ArticlesDao articlesDao() {
                return null;
            }
            @Override
            public void clearAllTables() {

            }
        };
        mAppExecutors = new AppExecutors();
        mRoomDatabase= AppDatabase.getAppDatabase(getActivity(),mAppExecutors);
        Config.mDatabase=mRoomDatabase;
        NewsApiVerifier=bundle.getString(NEWSAPI_KEY);
        WebHoseVerifier=bundle.getString(WebHoseAPIKEY);
        TwoPane=bundle.getBoolean(TwoPANEExtras_KEY);
        firebaseDataHolder= (FirebaseDataHolder) bundle.getSerializable(KEY_FIREBASE);
        CategoryName=bundle.getString(CATEGORY_NAME);
        Config.CategoryName=CategoryName;
        FirebaseArticlesList=new ArrayList<>();
        if (mDatabase==null){
            FirebaseDatabase database= FirebaseDatabase.getInstance();
            mDatabase=database.getInstance().getReference();
        }
    }

    private void ConnectToAPIs() {
        if (WebHoseVerifier.equals(URL)){
            WebHoseVerifier=null;
            WebHoseApiAsyncTask webHoseApiAsyncTask=new WebHoseApiAsyncTask(mRoomDatabase, Config.ArticlesMasterListFragment, getActivity(), CategoryName);
            webHoseApiAsyncTask.execute(URL);
        }else if (NewsApiVerifier.equals(URL)){
            NewsApiVerifier=null;
            newsApiAsyncTask=new NewsApiAsyncTask(mRoomDatabase, (NewsApiAsyncTask.OnNewsTaskCompleted) Config.ArticlesMasterListFragment, getActivity(), CategoryName);
            newsApiAsyncTask.execute(URL);
        }
        UrgentConnection();
    }

    private void UrgentConnection() {
        if (!TwoPane){
            urgentAsyncTask=new UrgentAsyncTask(mRoomDatabase, (UrgentAsyncTask.OnNewsUrgentTaskCompleted) this, getActivity(), KEY_Urgent);
            urgentAsyncTask.execute(UrgentURL+apiKey);
        }
    }

    private void PopulateTypesList(List<ArticlesEntity> typesArticlesList) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),typesArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.smoothScrollToPosition(Config.PosNewsFragment);
        Config.positionWebHose=0;
        Config.positionFirebase=0;
        Config.FragmentNewsApiNum=FragmentNewsApiNum;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.articles_fragment_master_list,container,false);
        ButterKnife.bind(this, rootView);
        snackBarLauncher=new SnackBarClassLauncher();

        if (TwoPane){
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        }else {
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            recyclerView_Horizontal= (RecyclerView) rootView.findViewById(R.id.recycler_view_horizontal);
        }

        if (Config.RetrieveFirebaseData){
            VerifyConnection verifyConnection=new VerifyConnection(getActivity());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
                FetchDataFromFirebase();
                if (!TwoPane){
                    UrgentConnection();
                }
            }
            else {
                initializeFirebaseViewModel();
                if (!TwoPane){
                    initializeUrgentViewModel();
                }
            }
        }else {
            if (savedInstanceState!=null) {
                TypesArticlesList = (ArrayList<ArticlesEntity>) savedInstanceState.getSerializable(KEY_ArticleTypeArray);
                PopulateTypesList(TypesArticlesList);
                if (!TwoPane){
                    UrgentArticlesList= (ArrayList<ArticlesEntity>) savedInstanceState.getSerializable(KEY_Urgent);
                    if (UrgentArticlesList!=null){
                        PopulateUrgentArticles(UrgentArticlesList);
                    }
                }
            }else {
                VerifyConnection verifyConnection=new VerifyConnection(getActivity());
                verifyConnection.checkConnection();
                if (verifyConnection.isConnected()){
                    ConnectToAPIs();
                }
                else {
                    initializeTypesViewModel();
                    if (!TwoPane) {
                        initializeUrgentViewModel();
                    }
                }
            }
        }
        return rootView;
    }






        public void initializeUrgentViewModel(){
        urgentArticlesViewModel = ViewModelProviders.of(getActivity()).get(UrgentArticlesViewModel.class);
        urgentArticlesViewModel.setCategory(KEY_Urgent);
        if (urgentArticlesViewModel !=null){
            urgentArticlesViewModel.getmObserverMediatorLiveDataListUrgentArticles().observe((LifecycleOwner) getActivity(),
                    new Observer<List<ArticlesEntity>>() {
                        @Override
                        public void onChanged(@Nullable List<ArticlesEntity> articlesEntities) {
                            if (articlesEntities!=null){
                                if (articlesEntities.size()>0){
                                    PopulateUrgentArticles(articlesEntities);
                                }
                            }
                        }
                    });
        }
    }

    private void initializeFirebaseViewModel() {
        firebaseViewModel = ViewModelProviders.of(getActivity()).get(FirebaseViewModel.class);
        if (firebaseViewModel != null) {
            firebaseViewModel.returnAllFirebaseData().observe((LifecycleOwner) getActivity(),
                    new Observer<List<FirebaseDataHolder>>() {
                        @Override
                        public void onChanged(@Nullable List<FirebaseDataHolder> firebaseDataHolders) {
                            if (firebaseDataHolders != null) {
                                if (firebaseDataHolders.size() > 0) {
                                    PopulateFirebaseList(firebaseDataHolders);
                                }
                            }
                        }
                    });
        }
    }

    public void initializeTypesViewModel(){
        typeArticlesViewModel = ViewModelProviders.of(getActivity()).get(TypeArticlesViewModel.class);
        typeArticlesViewModel.setCategory(CategoryName);
        if (typeArticlesViewModel!=null){
            typeArticlesViewModel.getmObserverMediatorLiveDataListUrgentArticles().observe((LifecycleOwner) getActivity(), new Observer<List<ArticlesEntity>>() {
                @Override
                public void onChanged(@Nullable List<ArticlesEntity> articleEntities) {
                    if (articleEntities!=null){
                        if (articleEntities.size()>0){
                            PopulateTypesList(articleEntities);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateTypesList(result);
            TypesArticlesList=result;
            Config.ArrArticle=result;
        }
    }

    @Override
    public void onWebHoseTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null&&result.size()>0){
            TypesArticlesList=result;
            Config.ArrArticle=result;
            WebHoseRecyclerAdapter mAdapter=new WebHoseRecyclerAdapter(getActivity(),result, TwoPane);
            mAdapter.notifyDataSetChanged();
            mLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            recyclerView.smoothScrollToPosition(Config.positionWebHose);
            Config.PosNewsFragment=0;
            Config.positionFirebase=0;
            Config.FragmentWebHoseApiNum=FragmentWebHoseApiNum;
            TypesArticlesList=result;
            Config.ArrArticle=result;
        }
    }

    private void PopulateFireBRecyclerArticles() {
        FirebaseRecyclerAdapter mAdapter=new FirebaseRecyclerAdapter(getActivity(),FirebaseArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.computeVerticalScrollOffset();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.smoothScrollToPosition(Config.positionFirebase);
        Config.positionWebHose=0;
        Config.PosNewsFragment=0;
    }

    private void PopulateFirebaseList(List<FirebaseDataHolder> result) {
        FirebaseArticlesList=result;
        if (result!=null){
            PopulateFireBRecyclerArticles();
            Config.FirebaseArticlesList=result;
        }
        Config.FragmentFirebaseApiNum=FragmentFirebaseApiNum;
    }

    @Override
    public void OnLocalFirebaseInsert(FirebaseDataHolder firebaseDataHolder) {

    }

    private void PopulateUrgentArticles(List<ArticlesEntity> urgentArticlesList) {
        UrgentNewsAdapter mAdapter=new UrgentNewsAdapter(getActivity(),urgentArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_Horizontal.setLayoutManager(mLayoutManager);
        recyclerView_Horizontal.setItemAnimator(new DefaultItemAnimator());
        recyclerView_Horizontal.setAdapter(mAdapter);
    }


    @Override
    public void onNewsUrgentApiTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null&&result.size()>0){
            PopulateUrgentArticles(result);
        }
    }

    public interface OnSelectedArticleListener {
        void onArticleSelected(ArticlesEntity articlesEntity, boolean TwoPane, int position);
    }


    OnSelectedArticleListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback= (OnSelectedArticleListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+MustImplementListener);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallback= (OnSelectedArticleListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+MustImplementListener);
        }
    }

    public interface OnFirebaseArticleSelectedListener {
        void onFirebaseArticleSelected(FirebaseDataHolder firebaseDataHolder, boolean TwoPane, int position);
    }

}