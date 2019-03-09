package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.FragmentArticleViewer;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleEntityInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleFirebaseInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.Frags_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

public class ArticleDetailsActivity extends AppCompatActivity {

    private FragmentSoundPlayer fragmentSoundPlayer;
    private FragmentArticleViewer fragmentArticleViewer;
    private FirebaseDataHolder firebaseDataHloder;
    private ArticlesEntity articlesEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Bundle bundle=new Bundle();
        fragmentSoundPlayer=new FragmentSoundPlayer();
        fragmentArticleViewer=new FragmentArticleViewer();
        if (savedInstanceState!=null){
            if (Config.RetrieveFirebaseData){
                Config.FirebaseDataHolder = (FirebaseDataHolder) savedInstanceState.getSerializable(KEY_FIREBASE);
                bundle.putSerializable(KEY_FIREBASE, Config.FirebaseDataHolder);
                NavigatToFragments(bundle);
            }else {
                Config.ArticlesEntity = (ArticlesEntity) savedInstanceState.getSerializable(OtherTypes_KEY);
                bundle.putSerializable(OtherTypes_KEY, Config.ArticlesEntity);
                NavigatToFragments(bundle);
            }
        }else {
            if (Config.ArticlesEntity_Obj==KEY_FIREBASE){
                firebaseDataHloder=(FirebaseDataHolder) getIntent().getSerializableExtra(KEY_FIREBASE);
                bundle.putSerializable(KEY_FIREBASE, firebaseDataHloder);
                NavigatToFragments(bundle);
            }else if (Config.ArticlesEntity_Obj==OtherTypes_KEY){
                articlesEntity=(ArticlesEntity) getIntent().getSerializableExtra(OtherTypes_KEY);
                bundle.putSerializable(OtherTypes_KEY, articlesEntity);
                NavigatToFragments(bundle);
            }
        }
    }

    private void NavigatToFragments(Bundle bundle) {
        fragmentArticleViewer.setArguments(bundle);
        fragmentSoundPlayer.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Audio_container, fragmentSoundPlayer, Frags_KEY)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Article_container, fragmentArticleViewer, Frags_KEY)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Config.ArticlesEntity!=null){
            outState.putSerializable(OtherTypes_KEY, Config.ArticlesEntity);
        }else {
            outState.putSerializable(KEY_FIREBASE, Config.FirebaseDataHolder);
        }
    }
}
