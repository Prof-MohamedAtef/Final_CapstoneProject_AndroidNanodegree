package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Firebase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 2/15/2019.
 */
@Entity(tableName = "Reports")
public class FirebaseDataHolder implements Serializable {

    @Exclude
    @NonNull
    public int getID() {
        return id;
    }

    public void setID(@NonNull int ID) {
        this.id = ID;
    }

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;


    @NonNull
    @ColumnInfo(name = "audiourl")
    public String audiourl;


    @NonNull
    @ColumnInfo(name = "key")
    public String key;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @NonNull
    @ColumnInfo(name = "description")
    public String description;

    @NonNull
    @ColumnInfo(name = "category_id")
    public String category_id;



    @Ignore
    @NonNull
    @ColumnInfo(name = "published_at")
    public String published_at;

    @NonNull
    @ColumnInfo(name = "user_email")
    public String user_email;

    @Ignore
    @NonNull
    @ColumnInfo(name = "token_id")
    public String token_id;

    @NonNull
    @ColumnInfo(name = "date")
    public String date;

    @NonNull
    @ColumnInfo(name = "user_name")
    public String user_name;

    @NonNull
    @ColumnInfo(name = "image_file_uri")
    public String image_file_uri;


    public FirebaseDataHolder() {
    }

    @Ignore
    public FirebaseDataHolder(String title_str, String description_str, String category_id_str, String userEmail, String image_str, String TokenID, String date, String user_name) {
        this.title = title_str;
        this.description= description_str;
        this.category_id= category_id_str;
        this.user_email = userEmail;
        this.image_file_uri = image_str;
        this.token_id = TokenID;
        this.user_name= user_name;
        this.date= date;
    }

    @Exclude
    @NonNull
    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audioURL) {
        audiourl = audioURL;
    }

    @Exclude
    @NonNull
    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    @Ignore
    public FirebaseDataHolder(String key, String title_str, String description_str, String category_id_str, String api_token_str, String audio_file, String image_str, String date, String user_name, String userEmail) {
        this.key = key;
        this.title = title_str;
        this.description= description_str;
        this.category_id= category_id_str;
        this.token_id = api_token_str;
        this.audiourl = audio_file;
        this.image_file_uri= image_str;
        this.date = date;
        this.user_name= user_name;
        this.user_email= userEmail;
    }

    @Exclude
    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }


    @NonNull
    @Exclude
    public String getUserName() {
        return user_name;
    }

    public void setUserName(@NonNull String userName) {
        this.user_name = userName;
    }

    @Exclude
    @NonNull
    public String getTokenID() {
        return token_id;
    }

    public void setTokenID(@NonNull String tokenID) {
        token_id= tokenID;
    }

    @Exclude
    @NonNull
    public String getTITLE() {
        return title;
    }

    public void setTITLE(@NonNull String TITLE) {
        this.title = TITLE;
    }

    @Exclude
    @NonNull
    public String getDESCRIPTION() {
        return description;
    }

    public void setDESCRIPTION(@NonNull String DESCRIPTION) {
        this.description = DESCRIPTION;
    }

    @Exclude
    @NonNull
    public String getCategoryID() {
        return category_id;
    }

    public void setCategoryID(@NonNull String categoryID) {
        category_id = categoryID;
    }


    @Exclude
    @NonNull
    public String getPUBLISHEDAT() {
        return published_at;
    }

    public void setPUBLISHEDAT(@NonNull String PUBLISHEDAT) {
        this.published_at = PUBLISHEDAT;
    }

    @Exclude
    @NonNull
    public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(@NonNull String userEmail) {
        user_email = userEmail;
    }

    @Exclude
    @NonNull
    public String getImageFileUri() {
        return image_file_uri;
    }

    public void setImageFileUri(@NonNull String imageFileUri) {
        image_file_uri= imageFileUri;
    }
}