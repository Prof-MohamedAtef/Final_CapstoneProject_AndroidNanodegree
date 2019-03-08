package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.SessionManagement;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    ProgressDialog mProgressDialog;
    SessionManagement sessionManagement;
    HashMap<String, String> user;
    String LoggedEmail, LoggedUserName;
    @Override
    protected void onResume() {
        super.onResume();
        if (LoggedEmail != null&&LoggedUserName!=null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Apply activity transition
                        startActivity(intent, options.toBundle());
                    } else {
                        // Swap without transition
                        startActivity(intent);
                    }
                    finish();
                }
            }, 2000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    Intent intent= new Intent(SplashActivity.this, AuthenticationActivity.class);
                    hideProgressDialog();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // Apply activity transition
                        startActivity(intent, options.toBundle());
                    } else {
                        // Swap without transition
                        startActivity(intent);
                    }
                    finish();
                }
            },2000);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTheme(R.style.ArishTheme);
        sessionManagement=new SessionManagement(getApplicationContext());
        user =sessionManagement.getUserDetails();
        if (user!=null){
            LoggedUserName=user.get(SessionManagement.KEY_NAME);
            LoggedEmail=user.get(SessionManagement.KEY_EMAIL);
        }
        mProgressDialog = new ProgressDialog(this);
    }
}