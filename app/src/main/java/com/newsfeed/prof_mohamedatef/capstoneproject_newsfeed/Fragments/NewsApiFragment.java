package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.NewsApiRecyclerAdapter;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Listeners.SnackBarLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.AppExecutors;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask.NewsApiAsyncTask;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.JobDispatcher.JobDispatcherReminder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.OptionsEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Dao.ArticlesDao;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.SessionManagement;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.ViewModel.UrgentArticlesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiFragment extends Fragment implements NewsApiAsyncTask.OnNewsTaskCompleted,
SnackBarLauncher{

    SnackBarClassLauncher snackBarLauncher;
    Snackbar snackbar;
    private String Urgent="urgent";
    String URL;
    private boolean TwoPane;
    RecyclerView recyclerView;
    SessionManagement sessionManagement;
    @BindView(R.id.LinearUiIdentifier)
    LinearLayout LinearUiIdentifier;
    private Cursor cursor;
    private ArrayList<ArticlesEntity> UrgentArticlesList;
    private String KEY_POSITION="KEY_POSITION";
    RecyclerView.LayoutManager mLayoutManager;
    public static String KEY_Urgent="KEY_Urgent";
    private GridLayoutManager layoutManager;
    private String NewsApiFrag_KEY="NewsApiFrag_KEY";
    private UrgentArticlesViewModel urgentArticlesViewModel;
    private AppDatabase mDatabase;
    private AppExecutors mAppExecutors;
    private LiveData<List<ArticlesEntity>> UrgentArticlesListLiveData;
    private NoInternetFragment noInternetFragment;


    public void initializeViewModel(){
        urgentArticlesViewModel = ViewModelProviders.of(getActivity()).get(UrgentArticlesViewModel.class);
        urgentArticlesViewModel.setCategory(KEY_Urgent);
        if (urgentArticlesViewModel !=null){
            urgentArticlesViewModel.getmObserverMediatorLiveDataListUrgentArticles().observe((LifecycleOwner) getActivity(), new Observer<List<ArticlesEntity>>() {
                @Override
                public void onChanged(@Nullable List<ArticlesEntity> articleEntities) {
                    if (articleEntities!=null){
                        if (articleEntities.size()>0){
                            urgentArticlesViewModel.getmObserverMediatorLiveDataListUrgentArticles().removeObserver(this::onChanged);
                            UrgentArticlesListLiveData=mDatabase.articlesDao().getArticlesDataByCategory(KEY_Urgent);
                            UrgentArticlesListLiveData.observe((LifecycleOwner)Config.mContext,UrgentList -> {
                                if (UrgentList.size()>0){
                                        PopulateUrgentArticles(UrgentList);
                                }
                            });
                        }else {
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            UrgentArticlesList= (ArrayList<ArticlesEntity>) savedInstanceState.getSerializable(KEY_Urgent);
            if (UrgentArticlesList!=null){
                PopulateUrgentArticles(UrgentArticlesList);
            }
        }
    }

    private void connectToApi(){
        VerifyConnection verifyConnection=new VerifyConnection(getActivity());
        verifyConnection.checkConnection();
        if (verifyConnection.isConnected()){
            NewsApiAsyncTask newsApiAsyncTask=new NewsApiAsyncTask(mDatabase,this, getActivity(),KEY_Urgent);
            newsApiAsyncTask.execute(URL);
        }else {
            initializeViewModel();
        }
    }

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(Config.LinearUiIdentifier, getActivity().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getActivity().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        connectToApi();
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        URL= bundle.getString(Urgent);
        Config.URL=URL;
        noInternetFragment=new NoInternetFragment();
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
        mDatabase= AppDatabase.getAppDatabase(getActivity(),mAppExecutors);
//        initializeViewModel();
        Config.mDatabase=mDatabase;
        Config.CategoryName=KEY_Urgent;
        Config.mContext=getActivity();
        Config.NewsApiFragment=this;
        Config.onNewsTaskCompleted=this;
        connectToApi();
        JobDispatcherReminder.scheduleFetchReminder(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Config.LinearUiIdentifier=LinearUiIdentifier;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_ui_identifier,container,false);
        ButterKnife.bind(this, rootView);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);

        snackBarLauncher=new SnackBarClassLauncher();
        sessionManagement=new SessionManagement(getActivity());
        if (rootView.findViewById(R.id.two_pane)!=null){
            TwoPane=true;
            Config.TwoPane=true;
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_Urgent,UrgentArticlesList);
    }

    @Override
    public void onNewsApiTaskCompleted(ArrayList<ArticlesEntity> result) {
        if (result!=null){
            if (result.size()>0){
                PopulateUrgentArticles(result);
            }
            String UrgentTextLines;
            String UrgentOneLine = null;
            for (ArticlesEntity x : result){
                UrgentTextLines=x.getTITLE().toString();
                UrgentOneLine+=UrgentTextLines+".\n";
            }
            sessionManagement.createUrgentIntoPrefs(UrgentOneLine);
        }
    }

    private void PopulateUrgentArticles(List<ArticlesEntity> result) {
        NewsApiRecyclerAdapter mAdapter=new NewsApiRecyclerAdapter(getActivity(),result, TwoPane);
        mAdapter.notifyDataSetChanged();
        mLayoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        snackBarLauncher.SnackBarLoadedData(Config.LinearUiIdentifier,Config.mContext);
    }


    public interface NewsApiSelectedArticleListener {
        void onNewsApiArticleSelected(OptionsEntity optionsEntity, boolean TwoPane, int position);
    }

    @Override
    public void onNoInternetConnection() {
        snackbar=NetCut();
        snackBarLauncher.SnackBarInitializer(snackbar);
    }
}