package com.demo.youtubedownloaderdemo;

import android.app.Application;


public class DownloaderApp extends Application {
    public static final String TAG = DownloaderApp.class
            .getSimpleName();

    private static DownloaderApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

}