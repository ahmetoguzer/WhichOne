package com.example.ahmet.mygame.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ahmet on 11.5.2017.
 */

public class MySharedPreferences {
    private static MySharedPreferences mySharedPreferences;
    private SharedPreferences sharedPreferences;

    public static MySharedPreferences getInstance(Context context) {
        if (mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("YourCustomNamedPreference",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
