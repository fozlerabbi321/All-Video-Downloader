package com.demo.youtubedownloaderdemo.ads;


import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;



public class AdmobBanner implements iConstants {
    private static String mImeiNumber;
    private static Context mContext;
    //  private AdView mAdView;


    public static void LoadAd(Context context, RelativeLayout adContainer, String ID) {



        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

         adContainer.setGravity(Gravity.CENTER_HORIZONTAL);

        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(ID);
        adContainer.addView(mAdView , layoutParams);
        adContainer.invalidate();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }



}
