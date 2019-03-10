package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.FragmentArticleViewer;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleEntityInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleFirebaseInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.ArticleInfo_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.Frags_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

public class ArticleDetailsActivity extends AppCompatActivity {

    private FragmentSoundPlayer fragmentSoundPlayer;
    private FragmentArticleViewer fragmentArticleViewer;
    private FirebaseDataHolder firebaseDataHloder;
    private ArticlesEntity articlesEntity;
    private ImageView Article_MainImage;
    public static int Activity_Num=2;
    private Toolbar mToolbar;
    private Intent intent;
    private boolean TwoPaneUi=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        Bundle bundle=new Bundle();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_left_arrow_circular_button_outline));
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.ActivityNum = 1;
                onBackPressed();
            }
        });
        if (findViewById(R.id.coordinator_layout_detail)!=null){
            TwoPaneUi=false;
            bundle.putBoolean(TwoPANEExtras_KEY, TwoPaneUi);
        }
        Config.ActivityNum=Activity_Num;
        Article_MainImage=(ImageView)findViewById(R.id.Article_MainImage);
        fragmentSoundPlayer=new FragmentSoundPlayer();
        fragmentArticleViewer=new FragmentArticleViewer();
        if (savedInstanceState!=null){
            if (Config.RetrieveFirebaseData){
                Config.FirebaseDataHolder = (FirebaseDataHolder) savedInstanceState.getSerializable(KEY_FIREBASE);
                bundle.putSerializable(KEY_FIREBASE, Config.FirebaseDataHolder);
                NavigatToFragments(bundle);
                DisplayArticleImage(Config.FirebaseDataHolder);
            }else {
                Config.ArticlesEntity = (ArticlesEntity) savedInstanceState.getSerializable(OtherTypes_KEY);
                bundle.putSerializable(OtherTypes_KEY, Config.ArticlesEntity);
                NavigatToFragments(bundle);
                DisplayArticleImage(Config.ArticlesEntity);
            }
        }else {
            if (Config.ArticlesEntity_Obj==KEY_FIREBASE){
                firebaseDataHloder=(FirebaseDataHolder) getIntent().getSerializableExtra(KEY_FIREBASE);
                bundle.putSerializable(KEY_FIREBASE, firebaseDataHloder);
                NavigatToFragments(bundle);
                DisplayArticleImage(firebaseDataHloder);
            }else if (Config.ArticlesEntity_Obj==OtherTypes_KEY){
                articlesEntity=(ArticlesEntity) getIntent().getSerializableExtra(OtherTypes_KEY);
                bundle.putSerializable(OtherTypes_KEY, articlesEntity);
                NavigatToFragments(bundle);
                DisplayArticleImage(articlesEntity);
            }
        }
    }

    private void DisplayArticleImage(ArticlesEntity articlesEntity) {
        String Image= articlesEntity.getIMAGE_URL();
        Picasso.with(getApplicationContext()).load(Image)
                .error(R.drawable.breaking_news)
                .into(Article_MainImage);
    }

    private void DisplayArticleImage(FirebaseDataHolder firebaseDataHolder) {
        String Image= firebaseDataHolder.getImageFileUri();
        Picasso.with(getApplicationContext()).load(Image)
                .error(R.drawable.breaking_news)
                .into(Article_MainImage);
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