package com.sururiana.bukatoko.data.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.HashMap;

public class PrefsManager {

    private SharedPreferences pref; //untuk objek share preference
    private SharedPreferences.Editor editor; //untuk melakukan edit, seperti edit data atau update data, atau create data

    private Context context; //induk activity

    private int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "TokoOnlinePref";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String SESS_ID = "id";
    public static final String SESS_NAME = "name";
    public static final String SESS_EMAIL = "email";
    public static final String SESS_PASS = "password";

    public PrefsManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String email, String password){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(SESS_ID, id);
        editor.putString(SESS_NAME, name);
        editor.putString(SESS_EMAIL, email);
        editor.putString(SESS_PASS, password);

        editor.commit();

        Toast.makeText(context,"Berhasil Login sebagai" + name, Toast.LENGTH_SHORT).show();
    }

    public HashMap<String, String > getUserDetail(){
        HashMap<String, String> user = new HashMap<>();

        user.put(SESS_ID, pref.getString(SESS_ID, null));
        user.put(SESS_NAME, pref.getString(SESS_NAME, null));
        user.put(SESS_EMAIL, pref.getString(SESS_EMAIL, null));
        user.put(SESS_PASS, pref.getString(SESS_PASS, null));

        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();

        Toast.makeText(context,"Keluar", Toast.LENGTH_SHORT).show();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
