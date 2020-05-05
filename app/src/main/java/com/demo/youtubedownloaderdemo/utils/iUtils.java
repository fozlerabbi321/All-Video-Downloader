package com.demo.youtubedownloaderdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.google.android.gms.ads.InterstitialAd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class iUtils implements iConstants {
    private InterstitialAd interstitialAd;

    public static boolean isSameDomain(String url, String url1) {
        return getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
    }

    private static String getRootDomainUrl(String url) {
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www") ? 1 : 0;
        if (length - dummy == 2)
            return domainKeys[length - 2] + "." + domainKeys[length - 1];
        else {
            if (domainKeys[length - 1].length() == 2) {
                return domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1];
            } else {
                return domainKeys[length - 2] + "." + domainKeys[length - 1];
            }
        }
    }

    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable drawable = item.getIcon();
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            drawable.mutate();
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void bookmarkUrl(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        // if url is already bookmarked, unbookmark it
        if (pref.getBoolean(url, false)) {
            editor.remove(url).commit();
        } else {
            editor.putBoolean(url, true);
        }

        editor.commit();
    }

    public static boolean isBookmarked(Context context, String url) {
        SharedPreferences pref = context.getSharedPreferences(PREF_APPNAME, 0);
        return pref.getBoolean(url, false);
    }
    public static void  ShowToast(Context context , String str){

        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }


   public static void GetSessionID(final Context cntx){
       final String[] ID = new String[1];

       AsyncTask.execute(new Runnable() {
           @Override
           public void run() {

               try {
                   Document doc = Jsoup.connect(API_URL2).post();

                   Elements scriptElements = doc.getElementsByTag("script");
                   for (Element element : scriptElements) {
                       if (element.data().contains("sid")) {
                            // find the line which contains 'infosite.token = <...>;'
                           Pattern pattern = Pattern.compile("(?is)sid=\'(.+?)\'");
                           Matcher matcher = pattern.matcher(element.data());
                           // we only expect a single match here so there's no need to loop through the matcher's groups
                           if (matcher.find()) {
                               //System.out.println(matcher.group());
                               //System.out.println(matcher.group(1));
                               ID[0] = matcher.group(1).toString();
                           } else {
                               System.err.println("No match found!");
                           }
                           break;
                       }
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }


               Session session;
               session = new Session(cntx);
               session.setSid(ID[0]);
                   }


       });




    //    return ID[0];
   }



}
