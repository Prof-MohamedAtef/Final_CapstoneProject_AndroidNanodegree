package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.WebViewerActivity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Fragments.ArticlesMasterListFragment;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities.ArticleTypesListActivity.URL_KEY;
import static com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Adapter.NewsApiRecyclerAdapter.NOTHING_TODO;

/**
 * Created by Prof-Mohamed Atef on 1/11/2019.
 */

public class WebHoseRecyclerAdapter extends  RecyclerView.Adapter<WebHoseRecyclerAdapter.ViewHOlder> implements Serializable {

    private final String LOG_TAG = WebHoseRecyclerAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<ArticlesEntity> feedItemList;
    boolean TwoPane;

    public WebHoseRecyclerAdapter(Context mContext, ArrayList<ArticlesEntity> feedItemList, boolean twoPane) {
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
    public void onBindViewHolder(@NonNull ViewHOlder holder, final int position) {
        final ArticlesEntity feedItem = feedItemList.get(position);
        if (feedItem!=null){
            if (feedItem.getAUTHOR()!=null&&feedItem.getTITLE()!=null){
                holder.Author.setText(feedItem.getAUTHOR());
                holder.Title.setText(feedItem.getTITLE());
                if (feedItem.getDESCRIPTION()!=null&&feedItem.getSOURCE_NAME()!=null){
                    holder.Description.setText(feedItem.getDESCRIPTION());
                    holder.SourceName.setText(feedItem.getSOURCE_NAME());
                    String ImagePath=feedItem.getIMAGE_URL().toString();
                    if (feedItem.getPUBLISHED_AT()!=null&&feedItem.getIMAGE_URL()!=null){
                        holder.Date.setText(feedItem.getPUBLISHED_AT());
                        if (ImagePath!=null&&!ImagePath.equals("")){
                            Picasso.with(mContext).load(ImagePath)
                                    .error(R.drawable.stanly)
                                    .into(holder.Image);
                            Config.position=position;
                        }else {
                            Picasso.with(mContext).load(R.drawable.stanly).into(holder.Image);
                            Log.v(LOG_TAG, "No URL To Image Returned" );
                        }
                    }else {holder.Date.setText("");}
                }else {
                    holder.Description.setText("");
                    holder.SourceName.setText("");
                }
            }else {
                holder.Author.setText("");
                holder.Title.setText("");
            }
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ArticlesMasterListFragment.OnSelectedArticleListener) mContext).onArticleSelected(feedItemList.get(position),TwoPane, position);
//                    Config.position=position;
                    if (Config.ActivityNum==0){
                        if (feedItem.getARTICLE_URL() != null) {
                            String url=feedItem.getARTICLE_URL();
                            Intent intent=new Intent(mContext,WebViewerActivity.class);
                            intent.putExtra(URL_KEY,url);
                            mContext.startActivity(intent);
                        }
                    }else {
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
        protected TextView Title;
        protected TextView Author;
        protected TextView Date;
        protected TextView Description;
        protected TextView SourceName;
        protected ImageView Image;
        protected LinearLayout linearLayout;
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
            this.SourceName= (TextView) converview.findViewById(R.id.source_name);
            this.Image =(ImageView)converview.findViewById(R.id.image);
            this.linearLayout=(LinearLayout)converview.findViewById(R.id.linearLayout);
            this.browser= (WebView) converview.findViewById(R.id.webview);

            if (Config.FragmentWebHoseApiNum==22){
                Image.setVisibility(View.GONE);
            }
        }
    }
}