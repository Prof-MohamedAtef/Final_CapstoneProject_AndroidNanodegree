package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Fragments;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Adapter.NewsApiRecyclerAdapter;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Adapter.WebHoseRecyclerAdapter;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.BuildConfig;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.AppExecutors;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Config;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.GenericAsyncTask.WebHoseApiAsyncTask;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Network.VerifyConnection;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.OptionsEntity;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.AppDatabase;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.Dao.ArticlesDao;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.CATEGORY_NAME;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.NEWSAPI_KEY;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.URL_KEY;
import static com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Activities.ArticleTypesListActivity.WebHoseAPIKEY;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class ArticlesMasterListFragment extends Fragment implements
        NewsApiAsyncTask.OnNewsTaskCompleted,
        WebHoseApiAsyncTask.OnWebHoseTaskCompleted
//        ,
//        FirebaseReportsAsyncTask.OnDownloadCompleted
{


    private AppDatabase mRoomDatabase;
    private AppExecutors mAppExecutors;
    private LiveData<List<ArticlesEntity>> TypesArticlesListLiveData;
    String apiKey;
    private String URL;
    private RecyclerView recyclerView;
    private boolean TwoPane;
//    private DatabaseReference mDatabase;
//    FirebaseHelper firebaseHelper;
//    ArrayList<FirebaseDataHolder> FirebaseArticlesList;
    private String NULL_KEY="Someone";
    private String NEWSAPI_CATEGORY="NewsApi";
//    private FirebaseReportsAsyncTask firebaseReportsAsyncTask;
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
    private String Category_KEY="categoryID";
    private String Description_KEY="description";
    private String IMAGE_FILE_KEY="imageFileUri";
    private String TITLE_KEY="title";
    private String Token_STR;
    private String TokenID_KEY="tokenID";
    private String Email_KEY="userEmail";
    private String UserName_KEY="userName";
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
        if (TypesArticlesList!=null){
            outState.putSerializable(KEY_ArticleTypeArray, TypesArticlesList);
        }
    }

//    public ArrayList<FirebaseDataHolder> FetchDataFromFirebase() {
//        DatabaseReference ThoughtsRef   =mDatabase.child(Data_KEY);
//        ValueEventListener valueEventListener=new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
////                SendToMethod(dataSnapshot);
//
//                FirebaseArticlesList.clear();
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ContentValues values = new ContentValues();
//                    KEY=ds.getKey();
//                    AudioFile_STR = ds.child(AudioFile_KEY).getValue(String.class);
//                    Category_STR = ds.child(Category_KEY).getValue(String.class);
//                    Date_STR = ds.child(Date_KEY).getValue(String.class);
//                    Description_STR = ds.child(Description_KEY).getValue(String.class);
//                    ImageFile_STR= ds.child(IMAGE_FILE_KEY).getValue(String.class);
//                    Title_STR= ds.child(TITLE_KEY).getValue(String.class);
//                    Token_STR= ds.child(TokenID_KEY).getValue(String.class);
//                    Email_STR= ds.child(Email_KEY).getValue(String.class);
//                    UserName_STR= ds.child(UserName_KEY).getValue(String.class);
//                    Log.d(LOG_TAG, AudioFile_STR+ " / " + Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR+ " / " + Token_STR+ " / " + UserName_STR);
//                    firebaseDataHolder=new FirebaseDataHolder(KEY, Title_STR, Description_STR, Category_STR, Token_STR, AudioFile_STR , ImageFile_STR, Date_STR, UserName_STR,Email_STR);
//                    FirebaseArticlesList.add(firebaseDataHolder);
//                    /*
//                    insert in content provider
//                     */
//
////                    values.put(ItemsContract.Items.KeyFireB_ID, KEY);
////                    values.put(ItemsContract.Items.TITLE, Title_STR);
////                    values.put(ItemsContract.Items.AUTHOR, UserName_STR);
////                    values.put(ItemsContract.Items.Description, Description_STR);
////                    values.put(ItemsContract.Items.ImageFile_URL, ImageFile_STR);
////                    values.put(ItemsContract.Items.AudioFile_URL, AudioFile_STR);
////                    values.put(ItemsContract.Items.Category, Category_STR);
////                    values.put(ItemsContract.Items.PUBLISHED_DATE, Date_STR);
////                    values.put(ItemsContract.Items.TokenID, Token_STR);
////                    values.put(ItemsContract.Items.Email, Email_STR);
////                    cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
//                }
//
//                if (FirebaseArticlesList.size()>0){
//                    PopulateFirebaseList(FirebaseArticlesList);
//                }
////                if (cpo.size()>0){
//
////                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        };
//        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
//        return FirebaseArticlesList;
//    }

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
        CategoryName=bundle.getString(CATEGORY_NAME);
        Config.CategoryName=CategoryName;
//        FirebaseArticlesList=new ArrayList<>();
//        if (mDatabase==null){
//            FirebaseDatabase database= FirebaseDatabase.getInstance();
//            mDatabase=database.getInstance().getReference();
//        }
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
            WebHoseApiAsyncTask webHoseApiAsyncTask=new WebHoseApiAsyncTask(this, getActivity());
            webHoseApiAsyncTask.execute(URL);
        }else if (NewsApiVerifier.equals(URL)){
            NewsApiVerifier=null;
            newsApiAsyncTask=new NewsApiAsyncTask(mRoomDatabase, Config.ArticlesMasterListFragment, getActivity(), CategoryName);
            newsApiAsyncTask.execute(URL);
        }
    }

    private void PopulateTypesList(ArrayList<ArticlesEntity> typesArticlesList) {
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
//                checkSavedOfflineData(CategoryName);
                /*
                if content provider have data, .... delete all then insert
                if content provider returned zero .... insert directly
                 */
//                FetchDataFromFirebase();
            }
//            else {
//                String selection= NewsProvider.CATEGORY+"=?";
//                String[] selectionArgs = new String[1];
//                selectionArgs[0] = KEY_FIREBASE;
//                ContentResolver resolver=getActivity().getContentResolver();
//                cursor=resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
//                if (cursor!=null){
//                    FirebaseArticlesList= CursorTypeConverter.AddFirebaseCursorToArrayList(cursor);
//                    if (FirebaseArticlesList.size()>0){
//                        PopulateFirebaseList(FirebaseArticlesList);
//                    }
//                }else {
//                    // Show Snack
//                    // redirect no internet fragment
//                }
                // select only from content provider
                // if returned 0
                // display no internet fragment
//            }
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
//                    checkSavedOfflineData("Sports");
                    ConnectToAPIs();
                }
//                else {
//                    String selection= NewsProvider.CATEGORY+"=?";
//                    String[] selectionArgs = new String[1];
//                    selectionArgs[0] ="Sports";
//                    ContentResolver resolver=getActivity().getContentResolver();
//                    cursor=resolver.query(CONTENT_URI, null, selection, selectionArgs, NewsProvider.CATEGORY);
//                    if (cursor!=null){
//                        TypesArticlesList= CursorTypeConverter.AddCursorToArrayList(cursor);
//                        if (TypesArticlesList.size()>0){
//                            PopulateTypesList(TypesArticlesList);
//                        }
//                    }else {
//                        // Show Snack
//                        // redirect no internet fragment
//                    }
//                }
            }
        }

        return rootView;
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null&&result.size()>0){
            if (result.size()>0){
//                for (ArticlesEntity optionsEntity:result){
////                    ContentValues values = CursorTypeConverter.saveUrgentOptionsUisngContentProvider(optionsEntity);
//                    ContentValues values = CursorTypeConverter.saveOptionsUisngContentProvider(optionsEntity,"Sports");
//                    Uri uri = getActivity().getContentResolver().insert(
//                            CONTENT_URI, values);
//                }
            }
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
            if (result.size()>0){
//                for (ArticlesEntity optionsEntity:result){
//                    ContentValues values = CursorTypeConverter.saveOptionsUisngContentProvider(optionsEntity,CategoryName);
//                    Uri uri = getActivity().getContentResolver().insert(
//                            CONTENT_URI, values);
//                }
            }
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

//    @Override
//    public void onDownloadTaskCompleted(ArrayList<FirebaseDataHolder> result) {
//        if (result!=null&&result.size()>0){
//            PopulateFirebaseList(result);
//        }
//    }

    public interface OnSelectedArticleListener {
        void onArticleSelected(ArticlesEntity articlesEntity, boolean TwoPane, int position);
    }

//    public interface OnFirebaseArticleSelectedListener {
//        void onFirebaseArticleSelected(FirebaseDataHolder firebaseDataHolder, boolean TwoPane, int position);
//    }

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
}