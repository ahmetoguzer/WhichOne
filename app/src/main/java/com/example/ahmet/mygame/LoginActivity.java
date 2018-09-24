package com.example.ahmet.mygame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmet.mygame.Preferences.MySharedPreferences;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Ahmet on 10.5.2017.
 */

public class LoginActivity  extends AppCompatActivity  {

    Button loginFacebook;
    Button login;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private MySharedPreferences mySharedPreferences;

    private EditText username;
    private TextView txtwelcome;
    private TextView txtloginOr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activit_login);

        loginFacebook = (Button) findViewById(R.id.btnLogin_facebook);
        login = (Button) findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.et_user_name);
        txtwelcome = (TextView) findViewById(R.id.txt_welcome);
        txtloginOr = (TextView) findViewById(R.id.txt_login_or);

        mySharedPreferences = MySharedPreferences.getInstance(LoginActivity.this);
        String data=mySharedPreferences.getData("user_name");
        if( !data.equals("")){
            startActivity(new Intent(getApplicationContext(), GamePreScreen.class));
        }

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"GoodDog.otf");
        login.setTypeface(tf);
        username.setTypeface(tf);
        loginFacebook.setTypeface(tf);
        txtwelcome.setTypeface(tf);
        txtloginOr.setTypeface(tf);

        onTouch();

     login.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

             String name = username.getText().toString();
             if(name.equals("")){

             }else{
                 mySharedPreferences.saveData("user_name",name);
                 startActivity(new Intent(getApplicationContext(), GamePreScreen.class));
             }

         }
     });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        if (AccessToken.getCurrentAccessToken() != null) {
            updateWithToken(AccessToken.getCurrentAccessToken());
        }

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("FaceLog", "Success");
            }

            @Override
            public void onCancel() {
                Log.i("FaceLog", "Cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("FaceLog", "Error");
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                updateWithToken(currentAccessToken);
            }
        };

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {
            Log.i("FaceLog", "Already Logged.");

            GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                    Log.i("FaceLog", jsonObject.toString());
                    Profile profile = Profile.getCurrentProfile();
                    if (profile != null) {
                        AppController.getInstance().profilImageUrl = profile.getProfilePictureUri(200, 200);
                        AppController.getInstance().profilName = profile.getName();
                        AppController.getInstance().profilLink = profile.getLinkUri();
                        try {
                            AppController.getInstance().profilGender = jsonObject.getString("gender");
                            AppController.getInstance().profilEmail = jsonObject.getString("email");
                            AppController.getInstance().profilLocale = jsonObject.getString("locale");
                            AppController.getInstance().profilTimezone = jsonObject.getString("timezone");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + "me" + "/friends", null, HttpMethod.GET, new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse graphResponse) {
                            Log.i("FaceLog", graphResponse.getJSONObject().toString());
                            try {
                                AppController.getInstance().profilFriendsCount = graphResponse.getJSONObject().getJSONObject("summary").getString("total_count");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(new Intent(getApplicationContext(), GamePreScreen.class));
                            finish();
                        }
                    }).executeAsync();
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,locale,timezone");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();

        } else {
            Log.i("FaceLog", "Not Logged.");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }


    public void onTouch() {
        login.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    v.setAlpha(0.5f);
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setAlpha(1);
                }


                return false;
            }
        });

        loginFacebook.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    v.setAlpha(0.5f);
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setAlpha(1);
                }


                return false;
            }
        });
    }
}
