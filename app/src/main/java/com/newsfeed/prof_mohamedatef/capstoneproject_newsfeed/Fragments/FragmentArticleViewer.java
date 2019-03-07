package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.WebViewerActivity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class FragmentArticleViewer extends Fragment{

    private TextView Title;
    private TextView Author;
    private TextView Date;
    private TextView Description;
    private TextView SourceName;
    private ImageView Image;
    private LinearLayout linearLayout;
    public static String KEY_optionsEntity = "Options";
    public static String KEY_firebase= "firebase";
    private ArticlesEntity articlesEntity;
    private TextView read_more;
    private FirebaseDataHolder firebaseDataHolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_viewer_fragment, container, false);
        Title = (TextView) rootView.findViewById(R.id.title);
        Author = (TextView) rootView.findViewById(R.id.author);
        Date = (TextView) rootView.findViewById(R.id.date_publish);
        Description = (TextView) rootView.findViewById(R.id.description);
        SourceName = (TextView) rootView.findViewById(R.id.source_name);
        Image = (ImageView) rootView.findViewById(R.id.image);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (Config.RetrieveFirebaseData){
            outState.putSerializable(KEY_FIREBASE, firebaseDataHolder);
        }else {
            outState.putSerializable(OtherTypes_KEY, articlesEntity);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (Config.RetrieveFirebaseData){
                firebaseDataHolder = (FirebaseDataHolder) savedInstanceState.getSerializable(KEY_FIREBASE);
                DisplayFirebaseData(firebaseDataHolder);
            }else {
                articlesEntity = (ArticlesEntity) savedInstanceState.getSerializable(OtherTypes_KEY);
                DisplayOptionsData(articlesEntity);
            }
        } else if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (Config.RetrieveFirebaseData){
                    firebaseDataHolder=(FirebaseDataHolder) bundle.getSerializable(KEY_FIREBASE);
                    DisplayFirebaseData(firebaseDataHolder);
                }else {
                    articlesEntity = (ArticlesEntity) bundle.getSerializable(OtherTypes_KEY);
                    DisplayOptionsData(articlesEntity);
                }
            }else {
                if (Config.ArrArticle!=null&&Config.ArrArticle.size()>0) {
                    ArticlesEntity articlesEntity= Config.ArrArticle.get(0);
                    DisplayOptionsData(articlesEntity);
                }
            }
        }
    }

    private void DisplayOptionsData(final ArticlesEntity articlesEntity) {
        if (articlesEntity!=null){
            if (articlesEntity.getAUTHOR()!=null&&articlesEntity.getTITLE()!=null){
                Author.setText(articlesEntity.getAUTHOR());
                Title.setText(articlesEntity.getTITLE());
                if (articlesEntity.getDESCRIPTION()!=null&&articlesEntity.getSOURCE_NAME()!=null){
                    Description.setText(articlesEntity.getDESCRIPTION());
                    SourceName.setText(articlesEntity.getSOURCE_NAME());
                    if (articlesEntity.getPUBLISHED_AT()!=null&&articlesEntity.getARTICLE_URL()!=null&&articlesEntity.getIMAGE_URL()!=null){
                        Date.setText(articlesEntity.getPUBLISHED_AT());
                        if (articlesEntity.getIMAGE_URL()!=null&&!articlesEntity.getIMAGE_URL().isEmpty()&&!articlesEntity.getIMAGE_URL().equals("")){
                            Picasso.with(getActivity()).load(articlesEntity.getIMAGE_URL())
                                    .error(R.drawable.breaking_news)
                                    .into(Image);
                        }
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (articlesEntity.getARTICLE_URL() != null) {
                                    String url=articlesEntity.getARTICLE_URL();
                                    Intent intent=new Intent(getActivity(),WebViewerActivity.class);
                                    intent.putExtra(URL_KEY,url);
                                    getActivity().startActivity(intent);
                                }
                            }
                        });
                    }else {Date.setText("");}
                }else {
                    Description.setText("");
                    SourceName.setText("");
                }
            }else {
                Author.setText("");
                Title.setText("");
            }
        }else {
            Date.setText("");
            Description.setText("");
            SourceName.setText("");
            Author.setText("");
            Title.setText("");
        }
    }

    private void DisplayFirebaseData(final FirebaseDataHolder firebaseDataHolder) {
        if (firebaseDataHolder!=null){
            if (firebaseDataHolder.getUserName()!=null&&firebaseDataHolder.getTITLE()!=null){
                Author.setText(firebaseDataHolder.getUserName());
                Title.setText(firebaseDataHolder.getTITLE());
                if (firebaseDataHolder.getDESCRIPTION()!=null&&firebaseDataHolder.getCategoryID()!=null){
                    Description.setText(firebaseDataHolder.getDESCRIPTION());
                    SourceName.setText(firebaseDataHolder.getCategoryID());
                    if (firebaseDataHolder.getDate()!=null&&firebaseDataHolder.getImageFileUri()!=null){
                        Date.setText(firebaseDataHolder.getDate());
                        Picasso.with(getActivity()).load(firebaseDataHolder.getImageFileUri())
                                .error(R.drawable.breaking_news)
                                .into(Image);
                    }else {Date.setText("");}
                }else {
                    Description.setText("");
                    SourceName.setText("");
                }
            }else {
                Author.setText("");
                Title.setText("");
            }
        }else {
            Date.setText("");
            Description.setText("");
            SourceName.setText("");
            Author.setText("");
            Title.setText("");
        }
    }
}