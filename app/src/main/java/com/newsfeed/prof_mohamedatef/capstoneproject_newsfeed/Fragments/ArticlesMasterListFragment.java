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
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.FirebaseRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.NewsApiRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.WebHoseRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.BuildConfig;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.AppExecutors;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseReportsAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.InsertLocallyFirebaseAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.OptionsEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao.ArticlesDao;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Helpers.InsertClass;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.FirebaseViewModel;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.TypeArticlesViewModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.CATEGORY_NAME;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.WebHoseAPIKEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config.POSTActivity;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class ArticlesMasterListFragment extends Fragment implements
        NewsApiAsyncTask.OnNewsTaskCompleted,
        WebHoseApiAsyncTask.OnWebHoseTaskCompleted,
        FirebaseReportsAsyncTask.OnDownloadCompleted,
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
//    FirebaseDataHolder firebaseDataHolder;
    ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
    Uri dirUri ;

    private ProgressDialog dialog;
    private String Date_KEY="date";
    private String Date_STR;
    private Cursor cursor;
    private OptionsEntity optionsEntity;
    private String CategoryName;
    private TypeArticlesViewModel typeArticlesViewModel;
    private LiveData<List<ArticlesEntity>> ArticlesListLiveData;
    private List<FirebaseDataHolder> FirebaseArticlesList;
    private FirebaseDataHolder firebaseDataHolder;
    private DatabaseReference mDatabase;
    private FirebaseViewModel firebaseViewModel;

    public ArticlesMasterListFragment(){

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
//        firebaseHelper=new FirebaseHelper();
    }


    private ArrayList<ArticlesEntity> TypesArticlesList;
    private String WebHoseVerifier, NewsApiVerifier;
    private String KEY_ArticleTypeArray="ArticleTypeArr";
    private String MustImplementListener="must Implement OnSelectedArticleListener";
    private int FragmentNewsApiNum=11;
    private int FragmentWebHoseApiNum=22;
    private int FragmentFirebaseApiNum=33;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Config.RetrieveFirebaseData){
            outState.putSerializable(KEY_FIREBASE, firebaseDataHolder);
        }else if (TypesArticlesList!=null){
            outState.putSerializable(KEY_ArticleTypeArray, TypesArticlesList);
        }
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

//    public static DataSnapshot dataSnapshot;
//    private void SendToMethod(DataSnapshot dataSnapshot) {
//        this.dataSnapshot=dataSnapshot;
//    }



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

//    private void AddCursorToArrayList() {
//        if (cursor.moveToFirst()){
//            for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
//                optionsEntity=new OptionsEntity();
//                optionsEntity.setAUTHOR(cursor.getString(cursor.getColumnIndex(NewsProvider.AUTHOR)));
//                optionsEntity.setTITLE(cursor.getString(cursor.getColumnIndex(NewsProvider.TITLE)));
//                optionsEntity.setDESCRIPTION(cursor.getString(cursor.getColumnIndex(NewsProvider.DESCRIPTION)));
//                optionsEntity.setURL(cursor.getString(cursor.getColumnIndex(NewsProvider.ARTICLE_URL)));
//                optionsEntity.setURLTOIMAGE(cursor.getString(cursor.getColumnIndex(NewsProvider.IMAGE_URL)));
//                optionsEntity.setPUBLISHEDAT(cursor.getString(cursor.getColumnIndex(NewsProvider.PUBLISHED_AT)));
//                optionsEntity.setNAME(cursor.getString(cursor.getColumnIndex(NewsProvider.SOURCE_NAME)));
//                UrgentArticlesList.add(optionsEntity);
//            }
//        }
//        if (UrgentArticlesList.size()>0){
//            PopulateUrgentArticles(UrgentArticlesList);
//        }
//    }


//    private void FetchFromFirebaseOfflineData() {
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<String> Articles=new ArrayList<>();
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                     Articles.add(childSnapshot.getChildren().toString());
//                     String x= Articles.get(0);
////                    FirebaseArticlesList.add()childSnapshot.getValue().toString();
////                    firebaseDataHolder.add
//                }
//
//
//
//                PopulateFirebaseList(FirebaseArticlesList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
////        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference(Data_KEY);
////        mDatabase.orderByValue().limitToLast(4).addChildEventListener(new ChildEventListener() {
////            @Override
////            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
////            }
////
////            @Override
////            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////            }
////
////            @Override
////            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////
////            }
////
////            @Override
////            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
//    }


//    private void checkSavedOfflineData(String category){
//        String selection= NewsProvider.CATEGORY+"=?";
//        String[] selectionArgs = new String[1];
//        selectionArgs[0] = category;
//        ContentResolver resolver=getActivity().getContentResolver();
//        cursor=resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
//        if (cursor.moveToFirst()) {
//            ContentResolver CR = getActivity().getContentResolver();
//            CR.delete(CONTENT_URI, null, null);
//        }
//    }

    private void ConnectToAPIs() {
        if (WebHoseVerifier.equals(URL)){
            WebHoseVerifier=null;
            WebHoseApiAsyncTask webHoseApiAsyncTask=new WebHoseApiAsyncTask(mRoomDatabase, Config.ArticlesMasterListFragment, getActivity(), CategoryName);
            webHoseApiAsyncTask.execute(URL);
        }else if (NewsApiVerifier.equals(URL)){
            NewsApiVerifier=null;
            newsApiAsyncTask=new NewsApiAsyncTask(mRoomDatabase, Config.ArticlesMasterListFragment, getActivity(), CategoryName);
            newsApiAsyncTask.execute(URL);
        }
    }

    private void PopulateTypesList(List<ArticlesEntity> typesArticlesList) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),typesArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mAdapter);
        Config.FragmentNewsApiNum=FragmentNewsApiNum;
    }

//    private void PopulateFirebaseList(ArrayList<FirebaseDataHolder> result) {
//        FirebaseArticlesList=result;
//        if (result!=null){
//            if (result.size()>0){
//                for (FirebaseDataHolder firebaseDataHolder : result){
//                    ContentValues values = CursorTypeConverter.saveFireBDataHolderUsingContentProvider(firebaseDataHolder,CategoryName);
//                    Uri uri = getActivity().getContentResolver().insert(
//                            CONTENT_URI, values);
//                }
//            }
//        }
//        PopulateFireBRecyclerArticles();
//        Config.FirebaseArticlesList=result;
//        Config.FragmentFirebaseApiNum=FragmentFirebaseApiNum;
//    }

//    private void PopulateFireBRecyclerArticles() {
//        FirebaseRecyclerAdapter mAdapter=new FirebaseRecyclerAdapter(getActivity(),FirebaseArticlesList, TwoPane);
//        mAdapter.notifyDataSetChanged();
//        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(mAdapter);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.articles_fragment_master_list,container,false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        if (Config.RetrieveFirebaseData){
            VerifyConnection verifyConnection=new VerifyConnection(getActivity());
            verifyConnection.checkConnection();
            if (verifyConnection.isConnected()){
                FetchDataFromFirebase();
            }
            else {
                initializeFirebaseViewModel();
            }
        }else {
            if (savedInstanceState!=null){
                if (TypesArticlesList.isEmpty()){
                    TypesArticlesList=(ArrayList<ArticlesEntity>) savedInstanceState.getSerializable(KEY_ArticleTypeArray);
                    PopulateTypesList(TypesArticlesList);
                }
            }else {
                VerifyConnection verifyConnection=new VerifyConnection(getActivity());
                verifyConnection.checkConnection();
                if (verifyConnection.isConnected()){
                    ConnectToAPIs();
                }
                else {
                    initializeTypesViewModel();
                }
            }
        }

        return rootView;
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
            RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            Config.FragmentWebHoseApiNum=FragmentWebHoseApiNum;
            TypesArticlesList=result;
            Config.ArrArticle=result;
        }
    }

    @Override
    public void onDownloadTaskCompleted(ArrayList<FirebaseDataHolder> result) {
        if (result!=null&&result.size()>0){
            PopulateFirebaseList(result);
        }
    }

    private void PopulateFireBRecyclerArticles() {
        FirebaseRecyclerAdapter mAdapter=new FirebaseRecyclerAdapter(getActivity(),FirebaseArticlesList, TwoPane);
        mAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
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