package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.ArticlesMasterListFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase.FirebaseDataHolder;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prof-Mohamed Atef on 2/17/2019.
 */

public class FirebaseRecyclerAdapter extends RecyclerView.Adapter<FirebaseRecyclerAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    List<FirebaseDataHolder> feedItemList;
    Cursor mCursor;
    boolean TwoPane;

    public FirebaseRecyclerAdapter(Context mContext, List<FirebaseDataHolder> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_reports_list_item, null);
        RecyclerView.ViewHolder viewHolder = new ViewHOlder(view);
        return (ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, final int position) {
        final FirebaseDataHolder feedItem = feedItemList.get(position);
        if (feedItem != null) {
            if (feedItem.getUserName() != null) {
                holder.Author.setText(feedItem.getUserName());
                if (feedItem.getCategoryID() != null) {
                    holder.SourceName.setText(feedItem.getCategoryID());
                    if (feedItem.getDate() != null ) {
                        holder.Date.setText(feedItem.getDate());
                        if (feedItem.getImageFileUri()!=null){
                            Picasso.with(mContext).load(feedItem.getImageFileUri())
                                    .error(R.drawable.breaking_news)
                                    .into(holder.Image);
                        }
                    } else {
                        holder.Date.setText("");
                    }
                } else {
                    holder.Description.setText("");
                    holder.SourceName.setText("");
                }
            } else {
                holder.Author.setText("");
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Config.ActivityNum!=0){
                        ((ArticlesMasterListFragment.OnFirebaseArticleSelectedListener) mContext).onFirebaseArticleSelected(feedItemList.get(position),TwoPane, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int size=0;
        if (feedItemList!=null){
            size=(null != feedItemList ? feedItemList.size() : 0);
        }if (mCursor!=null){
            size=(null != mCursor ? mCursor.getCount() : 0);
        }
        return size;
    }

    class ViewHOlder extends RecyclerView.ViewHolder {

        protected TextView Author;
        protected TextView Date;
        protected TextView Description;
        protected TextView SourceName;
        protected WebView browser;
        protected LinearLayout linearLayout;
        protected ImageView Image;

        public ViewHOlder(View converview) {
            super(converview);
            this.Author= (TextView) converview.findViewById(R.id.author);
            this.Date= (TextView) converview.findViewById(R.id.date_publish);
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.browser= (WebView) converview.findViewById(R.id.webview);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
        }
    }
}