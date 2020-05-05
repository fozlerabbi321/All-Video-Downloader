package com.demo.youtubedownloaderdemo.ads;

import android.content.Context;

import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;




public class AdmobInterstitial implements iConstants {
    private static InterstitialAd mInterstitialAd;
    private  static String IDs;
    public static void LoadAd(Context context , String ID){
       mInterstitialAd = new InterstitialAd(context);
        // set the ad unit ID
        IDs = ID;
        mInterstitialAd.setAdUnitId(IDs);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("BE9BB532808E71B746B8C96E58F9576D")
                .build();
        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
              //  showInterstitial();
            }
        });
    }


    public  static void ShowAd(Context context){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            LoadAd(context,IDs);
        }
    }
}
