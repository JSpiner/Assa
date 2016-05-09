package net.jspiner.assa.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import net.jspiner.assa.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Copyright 2016 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project Assa
 * @since 2016. 5. 9.
 */

public class SplashActivity extends Activity {

    //로그에 쓰일 tag
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Bind(R.id.btn_splash_login)
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        init();
        initFacebookSdk();
    }
    ProfileTracker mProfileTracker;

    void initFacebookSdk() {

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_about_me");
        loginButton.setReadPermissions("user_friends");

        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        if(Profile.getCurrentProfile() == null) {
                            mProfileTracker = new ProfileTracker() {
                                @Override
                                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                    // profile2 is the new profile
                                    Log.v("facebook - profile", profile2.getFirstName());
                                    mProfileTracker.stopTracking();
                                }
                            };
                            mProfileTracker.startTracking();
                        }
                        else {
                            Profile profile = Profile.getCurrentProfile();
                            Log.v("facebook - profile", profile.getFirstName());
                        }
                        Profile profile = Profile.getCurrentProfile();

                        Log.d(TAG, "login success");
                        Log.d(TAG,"profile : "+new Gson().toJson(profile));
                        Log.d(TAG,"profile : "+profile.getProfilePictureUri(100,100).toString());

                        Toast.makeText(getBaseContext(), "로그인을 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG, "login cancel");
                        Toast.makeText(getBaseContext(), "로그인을 취소하셨습니다.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        // App code
                        Log.d(TAG, "login error" + error.getMessage());
                        Toast.makeText(getBaseContext(), "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "net.jspiner.enerbnb", PackageManager.GET_SIGNATURES); //Your            package name here
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    void init(){

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                /*
                if(AccessToken.getCurrentAccessToken()!=null) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);


                    finish();
                }*/

            }
        };

        handler.sendEmptyMessageDelayed(0, 2000);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
