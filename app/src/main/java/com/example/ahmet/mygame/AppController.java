package com.example.ahmet.mygame;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ahmet on 10.5.2017.
 */

public class AppController extends Application {

    private static AppController mInstance;
    public Uri profilImageUrl;
    public String profilName;
    public Uri profilLink;
    public String profilGender;
    public String profilTimezone;
    public String profilLocale;
    public String profilEmail;
    public String profilFriendsCount;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        printHashKey();
    }

    public void printHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.ahmet.mygame",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }
}