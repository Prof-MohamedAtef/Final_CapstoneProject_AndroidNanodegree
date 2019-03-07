package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;

import java.util.ArrayList;


/**
 * Created by Prof-Mohamed Atef on 2/17/2019.
 */

public class FirebaseReportsAsyncTask  extends AsyncTask<String, Void, ArrayList<FirebaseDataHolder>> {

    private final OnDownloadCompleted onUploadCompleted;
    private final Context mContext;
    ArrayList<FirebaseDataHolder> FirebaseArticlesList;
    DatabaseReference mDatabase;
    FirebaseDataHolder firebaseDataHolder;
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
    private String  TokenID_KEY="tokenID";
    private String Email_KEY="userEmail";
    private String UserName_KEY="userName";
    private String Data_KEY="data";
    private String LOG_TAG="TAG";

    private ProgressDialog dialog;
    private String Date_KEY="date";
    private String Date_STR;

    public FirebaseReportsAsyncTask(OnDownloadCompleted onDownloadCompleted, Context context, DatabaseReference databaseReference){
        this.mDatabase=databaseReference;
        this.onUploadCompleted=onDownloadCompleted;
        this.mContext=context;
        FirebaseArticlesList=new ArrayList<>();
        dialog = new ProgressDialog(context);
    }

    @Override
    protected ArrayList<FirebaseDataHolder> doInBackground(String... strings) {
        return FetchDataFromFirebase();
    }

    public ArrayList<FirebaseDataHolder> FetchDataFromFirebase() {
        DatabaseReference ThoughtsRef=mDatabase.child(Data_KEY);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseArticlesList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    KEY=ds.getKey();
                    AudioFile_STR = ds.child(AudioFile_KEY).getValue(String.class);
                    Category_STR = ds.child(Category_KEY).getValue(String.class);
                    Date_STR = ds.child(Date_KEY).getValue(String.class);
                    Description_STR = ds.child(Description_KEY).getValue(String.class);
                    ImageFile_STR= ds.child(IMAGE_FILE_KEY).getValue(String.class);
                    Title_STR= ds.child(TITLE_KEY).getValue(String.class);
                    Token_STR= ds.child(TokenID_KEY).getValue(String.class);
                    Email_STR= ds.child(Email_KEY).getValue(String.class);
                    UserName_STR= ds.child(UserName_KEY).getValue(String.class);
                    Log.d(LOG_TAG, AudioFile_STR+ " / " + Email_STR+ " / " + Category_STR+ " / " + Description_STR+ " / " + ImageFile_STR+ " / " + Title_STR+ " / " + Token_STR+ " / " + UserName_STR);
                    firebaseDataHolder=new FirebaseDataHolder(KEY, Title_STR, Description_STR, Category_STR, Token_STR, AudioFile_STR , ImageFile_STR, Date_STR, UserName_STR,Email_STR);
                    FirebaseArticlesList.add(firebaseDataHolder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        ThoughtsRef.addListenerForSingleValueEvent(valueEventListener);
        return FirebaseArticlesList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try{
            if (dialog!=null&&dialog.isShowing()){
                this.dialog.dismiss();
            }else {
                this.dialog.setMessage(mContext.getResources().getString(R.string.loading));
                this.dialog.show();
            }
        }catch (Exception e){
            Log.v(LOG_TAG, "Problem in ProgressDialogue" );
        }
    }

    @Override
    protected void onPostExecute(ArrayList<FirebaseDataHolder> result) {
        super.onPostExecute(result);
        if (result != null) {
            if (onUploadCompleted!=null){
                onUploadCompleted.onDownloadTaskCompleted(result);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        }
    }

    public interface OnDownloadCompleted{
        void onDownloadTaskCompleted(ArrayList<FirebaseDataHolder> result);
    }
}