package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.WebViewerActivity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.ArticlesMasterListFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiRecyclerAdapter extends RecyclerView.Adapter<NewsApiRecyclerAdapter.ViewHOlder> implements Serializable{

    private final String LOG_TAG = NewsApiRecyclerAdapter.class.getSimpleName();
    Context mContext;
    List<ArticlesEntity> feedItemList;
    boolean TwoPane;

    public static String NOTHING_TODO="NoTHING_TODO";
    private String NULL_KEY="null";

    public NewsApiRecyclerAdapter(Context mContext, List<ArticlesEntity> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_api_list_item, null);
        RecyclerView.ViewHolder viewHolder = new ViewHOlder(view);
        return (ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHOlder holder, final int position) {
        if (feedItemList != null && feedItemList.size() > 0) {
            final ArticlesEntity feedItem = feedItemList.get(position);
            if (mContext!=null){
                if (feedItem.getAUTHOR() != null && feedItem.getTITLE() != null) {
                    if (feedItem.getAUTHOR().equals(NULL_KEY)) {
                        holder.Author.setText(mContext.getResources().getString(R.string.author_not_identified));
                    } else {
                        holder.Author.setText(feedItem.getAUTHOR());
                    }
                    if (feedItem.getTITLE().equals(NULL_KEY)) {
                        holder.Title.setText(mContext.getResources().getString(R.string.not_identified));
                    } else {
                        holder.Title.setText(feedItem.getTITLE());
                    }
                    if (feedItem.getDESCRIPTION() != null) {
                        if (feedItem.getDESCRIPTION().equals(NULL_KEY)) {
                            holder.Description.setText(mContext.getResources().getString(R.string.not_identified));
                        } else {
                            holder.Description.setText(feedItem.getDESCRIPTION());
                        }
                        if (feedItem.getPUBLISHED_AT() != null && feedItem.getARTICLE_URL() != null && feedItem.getIMAGE_URL() != null) {
                            if (feedItem.getPUBLISHED_AT().equals(NULL_KEY)) {
                                holder.Date.setText(mContext.getResources().getString(R.string.not_identified));
                            } else {
                                holder.Date.setText(feedItem.getPUBLISHED_AT());
                            }
                            if (feedItem.getIMAGE_URL()!=null&&!feedItem.getIMAGE_URL().isEmpty()&&feedItem.getIMAGE_URL()!=""&&feedItem.getIMAGE_URL()!=" "){
                                Picasso.with(mContext).load(feedItem.getIMAGE_URL())
                                        .error(R.drawable.breaking_news)
                                        .into(holder.Image);
                                Config.PosNewsFragment=position;
                            }else {
                                Picasso.with(mContext).load(R.drawable.breaking_news)
                                        .into(holder.Image);
                            }
                        } else {
                            holder.Date.setText("");
                        }
                    } else {
                        holder.Description.setText("");

                    }
                } else {
                    holder.Author.setText("");
                    holder.Title.setText("");
                }
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if two pane ---- > Web View
                    // if PHone --------> Web View
                    if (Config.ActivityNum != 0 && feedItemList != null) {
                        ((ArticlesMasterListFragment.OnSelectedArticleListener) mContext).onArticleSelected(feedItemList.get(position), TwoPane, position);
//                        Config.position=position;
                    }
                    if (Config.ActivityNum == 0) {
                        if (feedItem.getARTICLE_URL() != null) {
                            String url = feedItem.getARTICLE_URL();
                            Intent intent = new Intent(mContext, WebViewerActivity.class);
                            intent.putExtra(URL_KEY, url);
                            mContext.startActivity(intent);
                        }
                    } else {
                        /*
                        Do nothing
                         */
                        Log.e(LOG_TAG, NOTHING_TODO);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class ViewHOlder extends RecyclerView.ViewHolder {

        protected LinearLayout linearLayout;
        protected TextView Title;
        protected TextView Author;
        protected TextView Date;
        protected TextView Description;

        protected ImageView Image;
        protected WebView browser;

        public ViewHOlder(View converview) {
            super(converview);
            /*
            others
             */
            this.Title = (TextView) converview.findViewById(R.id.title);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.Description= (TextView) converview.findViewById(R.id.description);

            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
            this.browser= (WebView) converview.findViewById(R.id.webview);

            if (Config.FragmentNewsApiNum==11){
                Image.setVisibility(View.GONE);
            }
        }
    }
}