package com.demo.youtubedownloaderdemo.Tasks;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import java.io.File;

import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.demo.youtubedownloaderdemo.utils.iUtils;


public class DownloadFile implements iConstants {
    public static DownloadManager downloadManager;
    public static  long downloadID;
    private static String mBaseFolderPath;


    public  static void Downloading(Context context , String url , String  title , String ext){
        String cutTitle = title;
        cutTitle = cutTitle.replace(" ", "-").replace(".", "-") + ext;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        String folderName = DOWNLOAD_DIRECTORY;
        SharedPreferences preferences = context.getSharedPreferences(PREF_APPNAME, Context.MODE_PRIVATE);

        if(!preferences.getString("path","DEFAULT").equals("DEFAULT")){

              mBaseFolderPath = preferences.getString("path","DEFAULT");
        }else{


             mBaseFolderPath=android.os.Environment.getExternalStorageDirectory()+ File.separator+folderName;
        }
        String[] bits = mBaseFolderPath.split("/");
        String Dir = bits[bits.length-1];
      //  request.setDestinationUri(new File(mBaseFolderPath).);
        request.setDestinationInExternalPublicDir(Dir, cutTitle);
        request.allowScanningByMediaScanner();
        downloadID = downloadManager.enqueue(request);

        iUtils.ShowToast(context,"Downloading Start!");
    }
}
