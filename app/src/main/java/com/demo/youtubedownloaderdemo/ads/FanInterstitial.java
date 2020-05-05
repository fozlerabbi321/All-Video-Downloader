package com.demo.youtubedownloaderdemo.ads;

import android.content.Context;

import com.facebook.ads.*;

public class FanInterstitial {
    private static InterstitialAd interstitialAd;
    private static String IDs;

    public static void LoadAd(Context context , String ID, final Boolean show) {
        IDs=ID;
        interstitialAd = new InterstitialAd(context, ID);
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Show the ad when it's done loading.
            if(show){

                interstitialAd.show();
            }
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }
    public  static void ShowAd(Context context){
        if (interstitialAd.isAdLoaded()) {
            interstitialAd.show();
            LoadAd(context,IDs,false);
        }
    }
    }
