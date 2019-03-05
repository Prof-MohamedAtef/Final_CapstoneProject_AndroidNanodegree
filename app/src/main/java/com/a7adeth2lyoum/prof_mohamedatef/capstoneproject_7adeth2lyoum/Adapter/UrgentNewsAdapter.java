package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.R;
import com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Prof-Mohamed Atef on 1/11/2019.
 */

public class UrgentNewsAdapter extends RecyclerView.Adapter<UrgentNewsAdapter.ViewHOlder> implements Serializable {

    Context mContext;
    ArrayList<ArticlesEntity> feedItemList;
    boolean TwoPane;

    public UrgentNewsAdapter(Context mContext, ArrayList<ArticlesEntity> feedItemList, boolean twoPane) {
        this.mContext = mContext;
        this.feedItemList = feedItemList;
        TwoPane = twoPane;

    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urgent_news_list_item, null);
        RecyclerView.ViewHolder viewHolder = new ViewHOlder(view);
        return (ViewHOlder) viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, final int position) {
        final ArticlesEntity feedItem = feedItemList.get(position);
        if (feedItem!=null){
            if (feedItem.getTITLE()!=null){
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getSOURCE_NAME()!=null){
                    holder.SourceName.setText(feedItem.getSOURCE_NAME());
                    if (feedItem.getIMAGE_URL()!=null){
                        Picasso.with(mContext).load(feedItem.getIMAGE_URL())
                                .error(R.drawable.urgent)
                                .into(holder.Image);
                    }
                }else {
                    holder.SourceName.setText("");
                }
            }else {
                holder.Title.setText("");
            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    class ViewHOlder extends RecyclerView.ViewHolder {
        protected TextView Title;
        protected TextView SourceName;
        protected CircleImageView Image;

        public ViewHOlder(View converview) {
            super(converview);
            this.Title = (TextView) converview.findViewById(R.id.ArticleTitle);
            this.SourceName= (TextView) converview.findViewById(R.id.ArticleSource);
            this.Image =(CircleImageView)converview.findViewById(R.id.Article_MainImage);
        }
    }
}
