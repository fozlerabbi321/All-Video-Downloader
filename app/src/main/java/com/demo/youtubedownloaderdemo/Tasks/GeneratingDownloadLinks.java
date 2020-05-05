package com.demo.youtubedownloaderdemo.Tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import com.demo.youtubedownloaderdemo.R;
import com.demo.youtubedownloaderdemo.ads.AdmobInterstitial;
import com.demo.youtubedownloaderdemo.ads.FanInterstitial;
import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.demo.youtubedownloaderdemo.utils.JSONParser;
import com.demo.youtubedownloaderdemo.utils.Session;
import com.demo.youtubedownloaderdemo.utils.iUtils;

import static android.content.Context.MODE_PRIVATE;


public class GeneratingDownloadLinks implements iConstants  {
   public static Context Mcontext;
    public  static ProgressDialog pd;
    public  static  Dialog dialog;
   static  String SessionID;
    static  int error=1;
    public  static   String AdNetwork,FBannerID,FInterstitialID,ABannerID,AInterstitialID;
    public  static  SharedPreferences prefs;


    public  static void Start(final Context context , String url , final String title){
        error=1;
            for (int i = 0; i < DISABLE_DOWNLOADING.length; i++) {
        if (url.contains(DISABLE_DOWNLOADING[i])) {
            error=0;
      }
   }
        Mcontext=context;
        Session session;
        session = new Session(Mcontext);
        SessionID=session.getSid();
        if(error==1) {
            if(url.contains(".fbcdn.net")){
                final   CharSequence options[] = new CharSequence[] {"Download", "Watch"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Download / Watch");
                final String finalUrl = url;
                final String finalUrl1 = url;
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]i
                        if(options[which].equals("Download")) {
                            dialog.dismiss();
                            DownloadFile.Downloading(Mcontext, finalUrl1,title,".mp4");
                        }else{
                           // iUtils.ShowToast(context,"watch");
                            dialog.dismiss();
                            PlayVideo.play(Mcontext, finalUrl1,title,".mp4");

                        }
                    }
                });
                builder.show();

            }else {
                pd = new ProgressDialog(context);
                pd.setMessage(DOWNLOADING_MSG);
                pd.show();
                if (url.startsWith("https://tvfplay.com")) {
                    url = url.replace("https://tvfplay.com/", "https://tvfplay.com/api/");
                    new GetTvfPlayVideo().execute(url);
                    //   iUtils.ShowToast(Mcontext,url);
                } else {

                    //SessionID=title;
                    new GetUrls().execute(url);
                        //iUtils.ShowToast(Mcontext,url);
                        //iUtils.ShowToast(Mcontext,SessionID);

                }
            }
        }else{
            iUtils.ShowToast(Mcontext,WEB_DISABLE);

        }
        prefs = Mcontext.getSharedPreferences("AppConfig", MODE_PRIVATE);
        AdNetwork=prefs.getString("AdNetwork" , "");
        ABannerID=prefs.getString("ABannerID" , "" );
        AInterstitialID=prefs.getString("AInterstitialID", "" );
        FBannerID=prefs.getString("FBannerID", "" );
        FInterstitialID=prefs.getString("FInterstitialID", "" );
    }

//    public static class GetUrls extends AsyncTask<String, Void, JSONObject> {
//        JSONParser FJson = new JSONParser();
//        @Override
//        protected JSONObject doInBackground(String... urls) {
//            return FJson.getOJSONFromUrl(urls[0]);
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(JSONObject result) {
//
//            Log.e("ERROR", result.toString());
//            pd.dismiss();
//            String error = "";
//            try {
//                error = result.getString("error");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (error.contains("not-supported") || error.contains("no_media_found") || error.contains("miss")) {
//                iUtils.ShowToast(Mcontext, URL_NOT_SUPPORTED);
//            } else {
//                try {
//                    GenerateDownloadUI(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
public static class GetUrls extends AsyncTask<String, Void, Document> {
    Document doc;
    @Override
    protected Document doInBackground(String... urls) {

            //return doc = Jsoup.connect(API_URL2).data("url",urls[0]).data("sid",SessionID).userAgent("Mozilla").post();

                 //GetData(urls[0]);
        try {
            doc = Jsoup.connect(API_URL2).data("url",urls[0]).data("sid",SessionID).cookie("PHPSESSID", SessionID).referrer("https://savevideo.tube/").userAgent(" Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:68.0) Gecko/20100101 Firefox/68.0").post();
            //Log.e("ErrorURL",SessionID+"-----"+urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;

    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Document result) {
        pd.dismiss();
        JSONObject SortedData = new JSONObject();
        JSONArray url = new JSONArray();


        String thumbnail = null;
        Boolean thumbex=false;
        //Log.e("ErrorDocs", result.getElementsByAttributeValueContaining("alt","save online video").toString());
        Element thdata = result.select("img.img-fluid.mb-3").first();
        try {

            thumbnail=thdata.attr("src");

            thumbex=true;

        } catch (Exception e){


        }
        //Log.e("ErrorThumb", thumbnail);
        if(thumbex) {
            try {
                SortedData.put("thumbnail", thumbnail);
                SortedData.put("title", thdata.attr("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Element downloadTable = result.select("#moreOptions .table-dark").first();

            Elements downloadlinks = downloadTable.select("tbody tr");

            Log.e("ErrorsQ", downloadlinks.html() );

            for (Element download : downloadlinks) {
                Element link = download.select("a.btn.btn-sm.btn-outline-danger.shadow").first();
                Element quality = download.select("td").first();
                Element format = download.select("span.btn-sm.btn-outline-secondary").first();
                Element size = download.select("td.text-center").last();



                JSONObject urlData = new JSONObject();
                try {
                    urlData.put("label", quality.html() + "----" + format.html());
                    urlData.put("id", link.attr("href"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                url.put(urlData);


            }


            try {
                SortedData.put("urls", url);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                GenerateDownloadUI(SortedData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            iUtils.ShowToast(Mcontext,URL_NOT_SUPPORTED);
        }
    }
}

public static Document GetData(final String urls){

    final Document[] Doc = new Document[1];

    AsyncTask.execute(new Runnable() {
        @Override
        public void run() {

            try {
                Document doc = Jsoup.connect(API_URL2).data("url",urls).data("sid",SessionID).userAgent("Mozilla").post();
                Log.e("ErrorDoc", doc.html());
                 Doc[0] =doc;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    });

    return Doc[0];
}
    public static void GenerateDownloadUI(JSONObject result) throws JSONException {

        String thumbnail = result.getString("thumbnail");
        final String title = result.getString("title");
        final  JSONArray urls = result.getJSONArray("urls");

        dialog = new Dialog(Mcontext);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setTitle("Title...");
        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        ListView LV = (ListView)dialog.findViewById(R.id.ListView);


        String[] listItems = new String[urls.length()];
        text.setText(title);

        if(!thumbnail.equals("")) {
            Picasso.with(Mcontext).load(thumbnail).resize(100, 100).centerCrop().into(image);
        }
        String label="";
        for(int i = 0; i < urls.length(); i++){
            JSONObject list = urls.getJSONObject(i);

            label=list.getString("label");
            if(label.contains("(audio - no video) webm")){
                label=label.replace("(audio - no video) webm","mp3");
            }
            listItems[i] = label;

        }
        ArrayAdapter adapter = new ArrayAdapter(Mcontext, android.R.layout.simple_list_item_1, listItems);
        LV.setAdapter(adapter);





        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String ext = "";
                try {
                    final JSONObject m = urls.getJSONObject(position);

                    if(m.getString("label").contains(" mp4")){
                        ext=".mp4";
                    }else if(m.getString("label").contains(" mp3")){
                        ext=".mp3";
                    }else if(m.getString("label").contains(" 360p - webm")){
                        ext=".webm";
                    }else if(m.getString("label").contains(" webm")){
                        ext=".mp3";
                    }else if(m.getString("label").contains(" m4a")){
                        ext=".m4a";
                    }else if(m.getString("label").contains(" 3gp")){
                        ext=".3gp";
                    }else if(m.getString("label").contains(" flv")){
                        ext=".flv";
                    }else{
                        ext=".mp4";
                    }

                    DownloadFile.Downloading(Mcontext,m.getString("id"),title,ext);
                    dialog.dismiss();
                    showInterstitial();
                    // iUtils.ShowToast(Mcontext,"Something error" + m.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.show();




    }
    private void loadInterstitialAd() {
        if(AdNetwork.equals("Admob")) {
            new AdmobInterstitial().LoadAd(Mcontext, AInterstitialID);
        }else if(AdNetwork.equals("Fan")){
            new FanInterstitial().LoadAd(Mcontext, FInterstitialID,false);

        }
    }
    public static  void showInterstitial() {
        if(AdNetwork.equals("Admob")) {
            new AdmobInterstitial().ShowAd(Mcontext);
        }else if(AdNetwork.equals("Fan")){
            new FanInterstitial().ShowAd(Mcontext);
        }
    }
    public static class GetTvfPlayVideo extends AsyncTask<String, Void, JSONObject> {
        JSONParser FJson = new JSONParser();
        @Override
        protected JSONObject doInBackground(String... urls) {
            return FJson.getOJSONFromUrl(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONObject result) {

            Log.e("ERROR", result.toString());
            pd.dismiss();
            String error = "";
            JSONObject episode;
            try {
                error = result.getString("resp_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(error.contains("401")){
                iUtils.ShowToast(Mcontext,"Please login first then try to download TVFPlay videos");


            }else{
            try {
             episode = result.getJSONObject("episode");
             iUtils.ShowToast(Mcontext,episode.getString("video_tag"));
            } catch (JSONException e) {
                e.printStackTrace();
            }}
        }
    }
  

}
