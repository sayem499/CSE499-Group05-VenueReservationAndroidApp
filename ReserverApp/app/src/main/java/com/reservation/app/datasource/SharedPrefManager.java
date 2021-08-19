package com.reservation.app.datasource;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context mContext;

    public static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_PHONE = "phoneNumber";

    public SharedPrefManager(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void userLogin(String phoneNumber) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONE,phoneNumber);
        editor.apply();
    }

    public void logout(){
        editor.clear();
        editor.apply();
    }

    public String getPhoneNumber(){
        String phoneNumber;
        phoneNumber = sharedPreferences.getString(KEY_PHONE,"");
        return phoneNumber;
    }

    public boolean checkLogin() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
