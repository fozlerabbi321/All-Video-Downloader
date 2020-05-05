package com.demo.youtubedownloaderdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {
     private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setSid(String Sid) {
        prefs.edit().putString("SessionID", Sid).commit();
    }

    public String getSid() {
        String Sid = prefs.getString("SessionID","");
        return Sid;
    }
}