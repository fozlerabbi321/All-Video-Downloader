package com.demo.youtubedownloaderdemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class SettingActivity  extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Settings");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }
}