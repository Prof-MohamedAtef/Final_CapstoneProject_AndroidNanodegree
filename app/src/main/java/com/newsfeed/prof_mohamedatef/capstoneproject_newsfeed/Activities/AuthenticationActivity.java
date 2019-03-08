package com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.Listeners.SnackBarLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.R;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Config;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.SnackBarClassLauncher;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.Network.VerifyConnection;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.OptionsEntity;
import com.newsfeed.prof_mohamedatef.capstoneproject_newsfeed.helpers.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Prof-Mohamed Atef on 12/31/2018.
 */

public class AuthenticationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, SnackBarLauncher {

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SignInButton Gbtn_sign_in;
                       GoogleSignInAccount GAccessToken;
    CallbackManager callbackManager;
    Snackbar snackbar;
    SnackBarClassLauncher snackBarLauncher;
    String currentSignature;
    AccessTokenTracker accessTokenTracker;
    AccessToken FBaccessToken;
    LoginButton fbloginButton;
    SessionManagement sessionManagement;
    GoogleApiClient mGoogleApiClient;
    private final String LOG_TAG = AuthenticationActivity.class.getSimpleName();
    String Google_personName;
    String Google_personPhotoUrl;
    String Google_email, Google_AccessToken,GoogleUserID;
    Uri PhotoUri;
    GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 007;
    VerifyConnection verifyConnection;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        verifyConnection=new VerifyConnection(getApplicationContext());
        snackBarLauncher=new SnackBarClassLauncher();
        sessionManagement=new SessionManagement(getApplicationContext());
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        if (verifyConnection.isConnected()){
            mAuth=FirebaseAuth.getInstance();
            mAuthListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    if (user!=null){
                        //signed In
                        Toast.makeText(getApplicationContext(), "Successful Sign In", Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    }else {
                        Log.d(LOG_TAG, "onAuthStateChanged:signed_out:" );
                    }
                }
            };
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Gbtn_sign_in=(SignInButton)findViewById(R.id.btn_sign_in);
            Gbtn_sign_in.setSize(SignInButton.SIZE_STANDARD);
            Gbtn_sign_in.setScopes(gso.getScopeArray());
            Gbtn_sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
            /*
            Facebook SDK usage
             */
            callbackManager = CallbackManager.Factory.create();

            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "fbdemo.androidbeasts.com.facebookdemo",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    Log.e(LOG_TAG, "KeyHash" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    Log.d("KeyHash:", currentSignature);
                    Log.e(LOG_TAG, "KeyHash is:" + currentSignature);
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {
            }
            callbackManager = CallbackManager.Factory.create();
            fbloginButton = (LoginButton) findViewById(R.id.fblogin_button);
            fbloginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginManager.getInstance().logInWithReadPermissions(AuthenticationActivity.this, Arrays.asList("public_profile", "email"));
                    FBaccessToken = AccessToken.getCurrentAccessToken();
                }
            });
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                }
            };
            // If the access token is available already assign it.
            FBaccessToken = AccessToken.getCurrentAccessToken();
            Config.FBAccessToken=FBaccessToken;
            // Callback registration
            fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Toast.makeText(getApplicationContext(), "Facebook Login success", Toast.LENGTH_SHORT).show();
                    FBaccessToken = AccessToken.getCurrentAccessToken();
                }

                @Override
                public void onCancel() {
                    // App code
                    Toast.makeText(getApplicationContext(), "Facebook Login cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            fbloginButton.setReadPermissions(Arrays.asList("public_profile"));
        }else if(!verifyConnection.isConnected()){
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
        }
    }

    private Snackbar NetCut() {
        return snackbar= Snackbar
                .make(linearLayout, getApplicationContext().getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getApplicationContext().getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkConnection();
                    }
                });

    }

    private void checkConnection() {
        if (verifyConnection.isConnected()){
            snackBarLauncher.SnackBarLoadedData(linearLayout,getApplicationContext());
        }else if(!verifyConnection.isConnected()){
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GAccessToken = result.getSignInAccount();
            if (GAccessToken!=null){
                Google_personName= GAccessToken .getDisplayName();
                if (GAccessToken.getPhotoUrl() != null) {
                    Google_personPhotoUrl = GAccessToken.getPhotoUrl().toString();
                }
                Google_email = GAccessToken .getEmail();
                Google_AccessToken=GAccessToken.getIdToken();
                GoogleUserID= GAccessToken.getId();
                SharedPrefAndDiaryEntryRedirectGoogleDetails();
            }
        } else {
        }
    }

    private void SharedPrefAndDiaryEntryRedirectGoogleDetails() {
        sessionManagement.createLoginSession(Google_personName,Google_email,Google_personPhotoUrl,Google_AccessToken, GoogleUserID);
        sessionManagement.createLoginSessionType("G");
        Intent intent_create=new Intent(this,HomeActivity.class);
        startActivity(intent_create);
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager!=null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (verifyConnection.isConnected()){
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GAccessToken= result.getSignInAccount();
                    GoogleSignInResult result1=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    handleSignInResult(result1);
                }
            }
            if (FBaccessToken!=null){
                FBaccessToken.getCurrentAccessToken().getPermissions();
                FBaccessToken.getCurrentAccessToken().getDeclinedPermissions();
                Log.i("accessToken", FBaccessToken.toString());
                GraphRequest UserDataRequest = GraphRequest.newMeRequest(FBaccessToken, new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("FBLoginActivity", response.toString());
//                     Get facebook data from login
                        final Bundle bFacebookData = getFacebookData(object);
                        if (bFacebookData!=null||GAccessToken!=null){
                            if (bFacebookData!=null){
                                SharedPrefAndDiaryEntryRedirectFBDetails();
                            }
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                UserDataRequest.setParameters(parameters);
                UserDataRequest.executeAsync();
            }
        }else  if (!verifyConnection.isConnected()){
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
//            Toast.makeText(getApplicationContext(), "Connection Disabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void SharedPrefAndDiaryEntryRedirectFBDetails() {
        String ActualAccessToken= FBaccessToken.getToken().toString();
        sessionManagement.createLoginSession(FB_UserName,FB_Email,FB_ProfilePic,ActualAccessToken, FB_id);
        sessionManagement.createLoginSessionType("F");
        Intent intent_create=new Intent(this,HomeActivity.class);
        startActivity(intent_create);
    }

    String FB_id, FB_Email, FB_FirstName, FB_LastName, FB_UserName, FB_ProfilePic;
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            if (object.has("id")) {
                FB_id = object.getString("id");
            }
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + FB_id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                FB_ProfilePic=profile_pic.toString();
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            bundle.putString("idFacebook", FB_id);
            if (object.has("first_name"))
                FB_FirstName=object.getString("first_name").toString();
            bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                FB_LastName=object.getString("last_name").toString();
            bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                FB_Email= object.getString("email").toString();
            bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            FB_UserName=FB_FirstName+" "+FB_LastName;
            return bundle;
        }
        catch(JSONException e) {
            Log.d(LOG_TAG,"Error parsing JSON");
            return null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (verifyConnection.isConnected()){
            if (mGoogleApiClient!=null){
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
                if (opr.isDone()) {
//             If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//             and the GoogleSignInResult will be available instantly.
                    Log.d(LOG_TAG, "Got cached sign-in");
                    GoogleSignInResult result = opr.get();
                    handleSignInResult(result);
                } else {
//             If the user has not previously signed in on this device or the sign-in has expired,
//             this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgressDialog();
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            handleSignInResult(googleSignInResult);
                        }
                    });
                }
            }

        }else if (!verifyConnection.isInternetConnected){
            snackbar=NetCut();
            snackBarLauncher.SnackBarInitializer(snackbar);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        checkConnection();
    }


    @Override
    public void onNoInternetConnection() {

    }
}