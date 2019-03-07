package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.GenericAsyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.AppDatabase;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.ArticlesEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Room.Helpers.InsertClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Prof-Mohamed Atef on 1/3/2019.
 */

public class NewsApiAsyncTask extends AsyncTask <String, Void, ArrayList<ArticlesEntity>> {

    private final String LOG_TAG = NewsApiAsyncTask.class.getSimpleName();
    private AppDatabase mDatabase;
    private String KEY;
    private ProgressDialog dialog;
    public JSONObject ArticlesJson;
    public JSONArray ArticlesDataArray;
    public JSONObject oneArticleData;
    private ArrayList<ArticlesEntity> list = new ArrayList<ArticlesEntity>();

    String MAIN_LIST="articles";
    String SOURCE="source";
    String NAME="name";
    String AUTHOR="author";
    String TITLE="title";
    String DESCRIPTION="description";
    String URL_="url";
    String URL_TO_IMAGE="urlToImage";
    String PUBLISHED_AT="publishedAt";
    private ArticlesEntity articlesEntity;
    private String Name_STR;
    private String AUTHOR_STR;
    private String TITLE_STR;
    private String DESCRIPTION_STR;
    private String URL_STR;
    private String URL_TO_IMAGE_STR;
    private String PUBLISHED_AT_STR;


    public OnNewsTaskCompleted onNewsTaskCompleted;
    OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted;
    Context mContext;

    public NewsApiAsyncTask(AppDatabase database, OnNewsTaskCompleted onTaskCompleted, Context context, String Category){
        this.onNewsTaskCompleted=onTaskCompleted;
        dialog = new ProgressDialog(context);
        mContext=context;
        this.mDatabase=database;
        this.KEY=Category;
    }

    public NewsApiAsyncTask(OnNewsUrgentTaskCompleted onNewsUrgentTaskCompleted, Context context){
        this.onNewsUrgentTaskCompleted=onNewsUrgentTaskCompleted;
        dialog = new ProgressDialog(context);
        mContext=context;
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
    protected ArrayList<ArticlesEntity> doInBackground(String... params) {
        CertificateFactory cf = null;

        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        InputStream caInput=null;
        InputStream is = null;
        InputStream inputStream = null;
//            is=getClass().getResourceAsStream("/raw/certificate.crt");

        InputStream path=null;
        try {
            path=mContext.getResources().openRawResource(R.raw.newsapi_certificate);
        }catch (Exception e){
            path=null;
        }


        if (path==null){
            path=null;
        }else {
            caInput = new BufferedInputStream(path);
        }
        Certificate ca = null;
        try {
            try {
                ca = cf.generateCertificate(caInput);
            } catch (CertificateException e) {
                e.printStackTrace();
            }
            Log.v(LOG_TAG, "my Certificate Authority= " + ((X509Certificate) ca).getSubjectDN());
        } finally {
            try {
                caInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }


// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

// Create an SSLContext that uses our TrustManager
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = null;
        BufferedReader reader = null;
        try {
            url = new URL(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        if (params.length == 0) {
            return null;
        }
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
//            copyInputStreamToOutputStream(in, System.out);


            /*
            End of SSL
             */


        String UsersDesires_JsonSTR = null;

//            HttpURLConnection urlConnection = null;

        try {
//                URL url = new URL(params[0]);
//                urlConnection = (HttpURLConnection) url.openConnection();


            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                UsersDesires_JsonSTR = null;
            }else {
                reader = new BufferedReader(new InputStreamReader(inputStream));
            }
            String line;
            if (reader!=null){
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            }
            if (buffer.length() == 0) {
                return null;
            }
            UsersDesires_JsonSTR = buffer.toString();
            Log.v(LOG_TAG, "Articles String: " + UsersDesires_JsonSTR);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error here Exactly ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        try {
            return getArticlesJson(UsersDesires_JsonSTR);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "didn't got Users Desires from getJsonData method", e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ArticlesEntity> result) {
        super.onPostExecute(result);
        if (result != null) {
            if (onNewsTaskCompleted!=null){
                onNewsTaskCompleted.onNewsApiTaskCompleted(result);
            }else if (onNewsUrgentTaskCompleted!=null){
                onNewsUrgentTaskCompleted.onNewsUrgentApiTaskCompleted(result);
            }
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }

    private ArrayList<ArticlesEntity> getArticlesJson(String Articles_JsonSTR) throws JSONException {
        ArticlesJson = new JSONObject(Articles_JsonSTR );
        ArticlesDataArray= ArticlesJson.getJSONArray(MAIN_LIST);
        list.clear();
        for (int i = 0; i < ArticlesDataArray.length(); i++) {
            oneArticleData = ArticlesDataArray.getJSONObject(i);
            AUTHOR_STR = oneArticleData.getString(AUTHOR);
            TITLE_STR = oneArticleData.getString(TITLE);
            DESCRIPTION_STR = oneArticleData.getString(DESCRIPTION);
            URL_STR = oneArticleData.getString(URL_);
            URL_TO_IMAGE_STR = oneArticleData.getString(URL_TO_IMAGE);
            PUBLISHED_AT_STR = oneArticleData.getString(PUBLISHED_AT);
            JSONObject SourceJsonObj = oneArticleData.getJSONObject(SOURCE);
            Name_STR = SourceJsonObj.getString(NAME);
            if (AUTHOR_STR==null){
                AUTHOR_STR="";
            }
            if (TITLE_STR==null){
                TITLE_STR="";
            }
            if (DESCRIPTION_STR==null){
                DESCRIPTION_STR="";
            }
            if (PUBLISHED_AT_STR==null){
                PUBLISHED_AT_STR="";
            }
            if (Name_STR==null){
                Name_STR="";
            }
            articlesEntity = new ArticlesEntity(AUTHOR_STR, TITLE_STR, DESCRIPTION_STR, URL_STR, URL_TO_IMAGE_STR, PUBLISHED_AT_STR, Name_STR);
            list.add(articlesEntity);
        }
        if (list.size()>0){
            InsertClass insertClass=new InsertClass();
            insertClass.TryInsertNewsAPIData(mDatabase,list,onNewsTaskCompleted, KEY);
        }
        return list;
    }

    public interface OnNewsTaskCompleted{
        void onNewsApiTaskCompleted(ArrayList<ArticlesEntity> result);
    }

    public interface OnNewsUrgentTaskCompleted{
        void onNewsUrgentApiTaskCompleted(ArrayList<ArticlesEntity> result);
    }
}