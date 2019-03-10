package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.WebViewerActivity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleDetailsActivity.Activity_Num;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.OtherTypes_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.TwoPANEExtras_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.PublishToNewsFeed.KEY_FIREBASE;

/**
 * Created by Prof-Mohamed Atef on 1/10/2019.
 */

public class FragmentArticleViewer extends Fragment{


    SnackBarClassLauncher snackBarLauncher;
    Snackbar snackbar;
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
    private boolean TwoPaneUi;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_viewer_fragment, container, false);
        snackBarLauncher=new SnackBarClassLauncher();
        Title = (TextView) rootView.findViewById(R.id.title);
        Author = (TextView) rootView.findViewById(R.id.author);
        Date = (TextView) rootView.findViewById(R.id.date_publish);
        Description = (TextView) rootView.findViewById(R.id.description);
        SourceName = (TextView) rootView.findViewById(R.id.source_name);
        if (Config.ActivityNum!=Activity_Num){
            Image = (ImageView) rootView.findViewById(R.id.image);
        }
        read_more=(TextView) rootView.findViewById(R.id.read_more);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            final Bundle bundle = getArguments();
            if (bundle != null) {
                if (Config.RetrieveFirebaseData){
                    firebaseDataHolder=(FirebaseDataHolder) bundle.getSerializable(KEY_FIREBASE);
                    if (firebaseDataHolder!=null){
                        Config.FirebaseDataHolder=firebaseDataHolder;
                        DisplayFirebaseData(firebaseDataHolder);
                    }
                }else {
                    articlesEntity = (ArticlesEntity) bundle.getSerializable(OtherTypes_KEY);
                    if (articlesEntity!=null){
                        Config.ArticlesEntity=articlesEntity;
                        DisplayOptionsData(articlesEntity);
                    }
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
        if (articlesEntity != null) {
            if (articlesEntity.getAUTHOR() != null && articlesEntity.getTITLE() != null) {
                Author.setText(articlesEntity.getAUTHOR());
                Title.setText(articlesEntity.getTITLE());
                if (articlesEntity.getDESCRIPTION() != null && articlesEntity.getSOURCE_NAME() != null) {
                    Description.setText(articlesEntity.getDESCRIPTION());
                    SourceName.setText(articlesEntity.getSOURCE_NAME());
                    if (articlesEntity.getPUBLISHED_AT() != null && articlesEntity.getARTICLE_URL() != null && articlesEntity.getIMAGE_URL() != null) {
                        Date.setText(articlesEntity.getPUBLISHED_AT());
                        if (articlesEntity.getIMAGE_URL() != null && !articlesEntity.getIMAGE_URL().isEmpty() && !articlesEntity.getIMAGE_URL().equals("")) {
                            if (Config.ActivityNum!=Activity_Num){
                                Picasso.with(getActivity()).load(articlesEntity.getIMAGE_URL())
                                        .error(R.drawable.breaking_news)
                                        .into(Image);
                            }
                        }
                        linearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                VerifyConnection verifyConnection=new VerifyConnection(getActivity());
                                if (verifyConnection.isConnected()){
                                    if (articlesEntity.getARTICLE_URL() != null) {
                                        String url = articlesEntity.getARTICLE_URL();
                                        Intent intent = new Intent(getActivity(), WebViewerActivity.class);
                                        intent.putExtra(URL_KEY, url);
                                        getActivity().startActivity(intent);
                                    }
                                }else {
                                    Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Date.setText("");
                    }
                } else {
                    Description.setText("");
                    SourceName.setText("");
                }
            } else {
                Author.setText("");
                Title.setText("");
            }
        } else {
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
                    SourceName.setVisibility(View.VISIBLE);
                    if (firebaseDataHolder.getDate()!=null&&firebaseDataHolder.getImageFileUri()!=null){
                        Date.setText(firebaseDataHolder.getDate());
                        Date.setVisibility(View.VISIBLE);
                        if (Config.ActivityNum!=Activity_Num){
                            Picasso.with(getActivity()).load(firebaseDataHolder.getImageFileUri())
                                    .error(R.drawable.breaking_news)
                                    .into(Image);
                        }
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