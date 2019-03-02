package com.a7adeth2lyoum.prof_mohamedatef.capstoneproject_7adeth2lyoum.helpers;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Prof-Mohamed Atef on 12/31/2018.
 */

public class OptionsEntity implements Serializable{

    public OptionsEntity (){

    }
    public static Object FBAccessToken;
    public static String LANGUAGE;
    public static String SECTIONTITLE;
    public static String TITLEFULL;
    String UserName;
    String ID;
    String AUTHOR, TITLE, DESCRIPTION, URL, URLTOIMAGE, PUBLISHEDAT, NAME;
    Uri ImageFileUri;


    String ArticleID, Email;

    public String getArticleID() {
        return ArticleID;
    }

    public void setArticleID(String articleID) {
        ArticleID = articleID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public OptionsEntity(String articleID, String email, String user_name, String token_str, String category, String title, String image_file, String audio_file, String description){
        this.ArticleID=articleID;
        this.Email=email;
        this.UserName=user_name;
        this.API_TOKEN=token_str;
        this.CategoryName=category;
        this.TITLE=title;
        this.URLTOIMAGE=image_file;
        this.AudioFile=audio_file;
        this.DESCRIPTION=description;
    }

    public Uri getImageFileUri() {
        return ImageFileUri;
    }

    public void setImageFileUri(Uri imageFileUri) {
        ImageFileUri = imageFileUri;
    }

    String AudioFile;
    String AUTHOR_PHOTO;
    String Category_ID, API_TOKEN;

    public OptionsEntity(String title_str, String description_str, String category_id_str, String api_token_str, Uri image_str) {
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.Category_ID=category_id_str;
        this.API_TOKEN=api_token_str;
        this.ImageFileUri=image_str;
    }

    public OptionsEntity(String title_str, String description_str, String category_id_str, String api_token_str, String image_str) {
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.Category_ID=category_id_str;
        this.API_TOKEN=api_token_str;
        this.URLTOIMAGE=image_str;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    String CategoryName;

    public OptionsEntity(String id_str, String categoryName_str) {
        this.ID=id_str;
        this.CategoryName=categoryName_str;
    }

    public String getAudioFile() {
        return AudioFile;
    }

    public void setAudioFile(String audioFile) {
        AudioFile = audioFile;
    }

    public OptionsEntity(String author_str, String url_str, String language_str, String site_str, String sectiontitle_str, String title_str, String titlefull_str, String published_str, String mainimage_str, String text_str) {
        this.AUTHOR=author_str;
        this.URL=url_str;
        this.LANGUAGE=language_str;
        this.NAME=site_str;
        this.SECTIONTITLE=sectiontitle_str;
        this.TITLE=title_str;
        this.TITLEFULL=titlefull_str;
        this.PUBLISHEDAT=published_str;
        this.URLTOIMAGE=mainimage_str;
        this.DESCRIPTION=text_str;
    }

    public static String getLANGUAGE() {
        return LANGUAGE;
    }

    public static void setLANGUAGE(String LANGUAGE) {
        OptionsEntity.LANGUAGE = LANGUAGE;
    }

    public static String getSECTIONTITLE() {
        return SECTIONTITLE;
    }

    public static void setSECTIONTITLE(String SECTIONTITLE) {
        OptionsEntity.SECTIONTITLE = SECTIONTITLE;
    }

    public static String getTITLEFULL() {
        return TITLEFULL;
    }

    public static void setTITLEFULL(String TITLEFULL) {
        OptionsEntity.TITLEFULL = TITLEFULL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAUTHOR() {
        return AUTHOR;
    }

    public void setAUTHOR(String AUTHOR) {
        this.AUTHOR = AUTHOR;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURLTOIMAGE() {
        return URLTOIMAGE;
    }

    public void setURLTOIMAGE(String URLTOIMAGE) {
        this.URLTOIMAGE = URLTOIMAGE;
    }

    public String getPUBLISHEDAT() {
        return PUBLISHEDAT;
    }

    public void setPUBLISHEDAT(String PUBLISHEDAT) {
        this.PUBLISHEDAT = PUBLISHEDAT;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public OptionsEntity(String author_str, String title_str, String description_str, String url_str, String url_to_image_str, String published_at_str, String name_str) {
        this.AUTHOR=author_str;
        this.TITLE=title_str;
        this.DESCRIPTION=description_str;
        this.URL=url_str;
        this.URLTOIMAGE=url_to_image_str;
        this.PUBLISHEDAT=published_at_str;
        this.NAME=name_str;
    }
}