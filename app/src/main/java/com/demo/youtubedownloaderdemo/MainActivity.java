package com.demo.youtubedownloaderdemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.net.URISyntaxException;

import com.demo.youtubedownloaderdemo.Tasks.GeneratingDownloadLinks;
import com.demo.youtubedownloaderdemo.adapter.SitesAdapter;
import com.demo.youtubedownloaderdemo.ads.AdmobBanner;
import com.demo.youtubedownloaderdemo.ads.AdmobInterstitial;
import com.demo.youtubedownloaderdemo.ads.FanBanner;
import com.demo.youtubedownloaderdemo.ads.FanInterstitial;
import com.demo.youtubedownloaderdemo.constants.iConstants;
import com.demo.youtubedownloaderdemo.utils.AdBlock;
import com.demo.youtubedownloaderdemo.utils.IOUtils;
import com.demo.youtubedownloaderdemo.utils.iUtils;

import static com.demo.youtubedownloaderdemo.utils.iUtils.GetSessionID;

public class MainActivity extends AppCompatActivity
        implements iConstants , NavigationView.OnNavigationItemSelectedListener  {
     private String postUrl = "http://www.google.com";
    private WebView webView;
    private ProgressBar progressBar;
    private float m_downX;
    private static AutoCompleteTextView SearchText;
    final Activity activity = this;
    FloatingActionButton fab;
    private boolean AdblockEnabled = true;
    private BottomNavigationView bottomNavigationView;
    private CountDownTimer timer;
    private Boolean SearchHasFocus = false;
    private View bottomSheet;
    private View webViewCon;
    private String SessionID;
    SharedPreferences sharedPrefs;
    public Boolean WebviewLoaded=false;
    String URL;
    GridView HomeView;
    Toolbar toolbar;
    String AdNetwork,FBannerID,FInterstitialID,ABannerID,AInterstitialID;
    Boolean YTe = false;
    JSONArray Hint_uri;

RelativeLayout adViewContainer;
    public static final int REQUEST_PERMISSION_CODE = 1001;
    public static final String REQUEST_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SearchText=(AutoCompleteTextView) findViewById(R.id.SearchText);
        SearchText.setSelectAllOnFocus(true);
         bottomSheet = findViewById(R.id.design_bottom_sheet);
           HomeView = (GridView) findViewById(R.id.HomePage);
        webViewCon=(View)findViewById(R.id.webViewCon);
         bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        OnPrepareBottomNav(bottomNavigationView.getMenu());
        HomeView.setAdapter(new SitesAdapter(this));
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

               GetSessionID(MainActivity.this);
                 AdNetwork=getString(R.string.AdNetwork);
                ABannerID=getString(R.string.AdmobBanner);
                AInterstitialID=getString(R.string.AdmobInterstitial);
                FBannerID=getString(R.string.FabBanner);
                FInterstitialID=getString(R.string.FabInterstitial);
              //  Hint_uri=config.getJSONArray("Hint_url");
                SharedPreferences.Editor editor = getSharedPreferences("AppConfig", MODE_PRIVATE).edit();
                editor.putString("AdNetwork",  AdNetwork);
                editor.putString("ABannerID", ABannerID);
                editor.putString("AInterstitialID", AInterstitialID);
                editor.putString("FBannerID", FBannerID);
                editor.putString("FInterstitialID", FInterstitialID);
                editor.apply();
               // iUtils.ShowToast(MainActivity.this,AdNetwork);
                HintSet();
                loadBannerAd();
                loadInterstitialAd();
             //   new FanInterstitial().LoadAd(MainActivity.this, FInterstitialID,true);

        adViewContainer = (RelativeLayout)findViewById(R.id.adViewContainer);
        HomeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
        webView.loadUrl(HomePageURI[position]);
                WebviewLoaded=false;
        SwithcView(true);
            }
        });


        AdblockEnabled=sharedPrefs.getBoolean("ADBLOCK",true);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_home:
                                SwithcView(false);
                                break;
                            case R.id.action_bookmark:
                                iUtils.bookmarkUrl(MainActivity.this,webView.getUrl());
                                if(iUtils.isBookmarked(MainActivity.this,webView.getUrl())){

                                    item.setIcon(R.drawable.ic_bookmark_grey_800_24dp);
                                    item.getIcon().setAlpha(255);
                                    iUtils.ShowToast(MainActivity.this,"Bookmarked");
                                }else{
                                    item.setIcon(R.drawable.ic_bookmark_border_grey_800_24dp);
                                    item.getIcon().setAlpha(130);

                                }
                                break;
                            case R.id.action_back:
                                back();
                                break;
                            case R.id.action_forward:
                                forward();

                                break;
                        }
                        return true;
                    }
                });


        //WebView
        initWebView();

     //   webView.loadUrl(postUrl);


        SearchText.setText(webView.getUrl());
        SearchText.setSelected(false);
        isNeedGrantPermission();
        //Floating Button :)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GeneratingDownloadLinks.Start(MainActivity.this,webView.getUrl(),webView.getTitle());
               // showInterstitial();

            }
        });
        if(intent.hasExtra("URL")){
            URL = extras.getString("URL");

            if(!URL.equals("")){
                LoadFromIntent(URL);
            }}
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//         final Handler handler = new Handler();
//         Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                UpdateUi();
//                handler.postDelayed(this, 1000);
//            }
//        };
//
////Start
//        handler.postDelayed(runnable, 1000);
        //Search Text
        SearchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ( (i == EditorInfo.IME_ACTION_DONE) || ((keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (keyEvent.getAction() == KeyEvent.ACTION_DOWN ))){
                    String url = SearchText.getText().toString();
                      //  iUtils.ShowToast(MainActivity.this,url);
                    SearchText.clearFocus();
                    SwithcView(true);
                    InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(SearchText.getWindowToken(), 0);
                    if(iUtils.checkURL(url)){
                     //   iUtils.ShowToast(MainActivity.this,url);
                        if(!url.startsWith("http://") && !url.startsWith("https://")) {
                            url = "http://" + url;
                            }

                        webView.loadUrl(url);
                        WebviewLoaded=false;
                        SearchText.setText(webView.getUrl());
                    }else{
                        String Searchurl = String.format(SEARCH_ENGINE,url);
                        webView.loadUrl(Searchurl);
                        WebviewLoaded=false;
                        SearchText.setText(webView.getUrl());
                    }
                    return true;
                }
                return false;
            }


        });



        SearchText.setOnFocusChangeListener(focusListener);
    }
    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                SearchHasFocus = true;
            } else {
                SearchHasFocus = false;
            }
        }
    };
    public void UpdateUi(){

        String WebUrl = webView.getUrl();
        String SearchUrl = SearchText.getText().toString();
      //  iUtils.ShowToast(MainActivity.this,WebUrl);
if(!SearchHasFocus) {
//    if (!WebUrl.equals(SearchUrl)) {
if(WebviewLoaded) {
    SearchText.setText(WebUrl);


    OnPrepareBottomNav(bottomNavigationView.getMenu());
}
}

    }


    private  void  LoadFromIntent(String url){
        SwithcView(true);
        webView.loadUrl(url);
        WebviewLoaded=false;
    }
    private void SwithcView(Boolean show){
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) adViewContainer.getLayoutParams();


        //RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(30,30); //Parent Params


        if(show){
            lp.setMargins(0,0,0,0);
            lp.gravity = Gravity.BOTTOM;
            adViewContainer.setLayoutParams(lp);
            webViewCon.setVisibility(View.VISIBLE);
            HomeView.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
        }else{
            webViewCon.setVisibility(View.GONE);
            HomeView.setVisibility(View.VISIBLE);
          // iUtils.ShowToast(this,String.valueOf(toolbar.getHeight()));
           lp.setMargins(0,toolbar.getHeight(),0,0);
            lp.gravity = Gravity.TOP;
            adViewContainer.setLayoutParams(lp);
            webView.stopLoading();
            webView.loadUrl("about:blank");
            SearchText.setText("");
            fab.setVisibility(View.GONE);
        }


    }
    private void HintSet(){
       final  String[] WEBSITES = getStringArray(Hint_uri);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.autocomplete_list_item);
        SearchText.setAdapter(adapter);
        SearchText.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub
                SearchText.showDropDown();
                SearchText.requestFocus();
                return false;
            }
        });
        SearchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                webView.loadUrl("http://"+WEBSITES[i]);
                WebviewLoaded=false;
                SwithcView(true);
            }
        });
    }
    public static String[] getStringArray(JSONArray jsonArray) {
        String[] stringArray = null;
        if (jsonArray != null) {
            int length = jsonArray.length();
            stringArray = new String[length];
            for (int i = 0; i < length; i++) {
                stringArray[i] = jsonArray.optString(i);
            }
        }
        return stringArray;
    }
    private void initWebView() {
        webView.setWebChromeClient(new MyWebChromeClient(this));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                OnPrepareBottomNav(bottomNavigationView.getMenu());

            }
            @Override
            public void onLoadResource(WebView webview, String s)
            {
                if(webview.getUrl()!=null) {

                    if (webview.getUrl().contains("facebook.com")) {
                        webView.loadUrl("javascript:(function prepareVideo() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;console.log(i);var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\",\"'+jsonData['videoID']+'\");');}}})()");
                        webView.loadUrl("javascript:( window.onload=prepareVideo;)()");
                    } else if (webview.getUrl().contains("instagram.com")) {
                        webView.loadUrl("javascript:(function prepareVideo() {var el =document.getElementsByClassName(\"QvAa1\");for(var i=0;i<el.length; i++){var prnt =el[i].parentElement;var vid = prnt.querySelector('.tWeCl');var vidsrc =vid.getAttribute('src');if(vidsrc!=\"\"){el[i].setAttribute('onClick', ' IGDownloader.processVideo(\"'+vidsrc+'\"); return false; ');}}})()");
                        webView.loadUrl("javascript:( window.onload=prepareVideo;)()");
                    }
                }
            }
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("video.flhe2")){
                    iUtils.ShowToast(MainActivity.this,"Intercept Facebook video playing");
                }
                if(url.startsWith("intent://")){
                    try {
                        Intent furl = Intent.parseUri(url, 1);
                        if (getPackageManager().getLaunchIntentForPackage(furl.getPackage()) != null) {
                            startActivity(furl);
                            return true;
                        }
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse("market://details?id=" + furl.getPackage()));
                        startActivity(intent);
                        return true;
                    } catch (URISyntaxException e) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e.printStackTrace();
                    }

                }else if (url.startsWith("market://")) {
                    try {
                        Intent r1 = Intent.parseUri(url, 1);
                        if (r1 == null) {
                            return true;
                        }
                        startActivity(r1);
                        return true;
                    } catch (Throwable e22) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e22.printStackTrace();
                        return true;
                    }
                }else{
                SearchText.setText(url);
                webView.loadUrl(url);
                    WebviewLoaded=false;
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url=request.getUrl().toString();
                if(url.contains("video.flhe2")){
                    iUtils.ShowToast(MainActivity.this,"Intercept Facebook video playing");
                }
                if(url.startsWith("intent://")){
                    try {
                        Intent furl = Intent.parseUri(url, 1);
                        if (getPackageManager().getLaunchIntentForPackage(furl.getPackage()) != null) {
                            startActivity(furl);
                            return true;
                        }
                        Intent intent = new Intent("android.intent.action.VIEW");
                        intent.setData(Uri.parse("market://details?id=" + furl.getPackage()));
                        startActivity(intent);
                        return true;
                    } catch (URISyntaxException e) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e.printStackTrace();
                    }

                }else if (url.startsWith("market://")) {
                    try {
                        @SuppressLint("WrongConstant") Intent r1 = Intent.parseUri(url, 1);
                        if (r1 == null) {
                            return true;
                        }
                        startActivity(r1);
                        return true;
                    } catch (Throwable e22) {
                        iUtils.ShowToast(MainActivity.this,"No Application Found!");
                        e22.printStackTrace();
                        return true;
                    }
                }else{
                    SearchText.setText(url);
                    webView.loadUrl(url);
                    WebviewLoaded=false;}
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                WebviewLoaded=true;
                  //  view.loadUrl("javascript:window.android.onUrlChange(window.location.href);");
                webView.loadUrl("javascript:(function() { var el = document.querySelectorAll('div[data-sigil]');for(var i=0;i<el.length; i++){var sigil = el[i].dataset.sigil;if(sigil.indexOf('inlineVideo') > -1){delete el[i].dataset.sigil;var jsonData = JSON.parse(el[i].dataset.store);el[i].setAttribute('onClick', 'FBDownloader.processVideo(\"'+jsonData['src']+'\");');}}})()");
                Log.e("WEBVIEWFIN", url);
                OnPrepareBottomNav(bottomNavigationView.getMenu());


            }

            @Deprecated
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {



                if (AdblockEnabled && new  AdBlock(MainActivity.this).isAd(url)) {
                    return new WebResourceResponse(
                            "text/plain",
                            "UTF-8",
                            new ByteArrayInputStream("".getBytes())
                    );
                }


                for (int i = 0; i < DISABLE_DOWNLOADING.length; i++) {
                    if (url.contains(DISABLE_DOWNLOADING[i])) {
                        return new WebResourceResponse(
                                "text/html",
                                "UTF-8",
                                new ByteArrayInputStream("<h1 style='margin:30px 0; font-size:35px;'>We cannot allow to download videos form this website.</h1>".getBytes())
                        );
                    }
                }



                return super.shouldInterceptRequest(view, url);
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (AdblockEnabled &&  new  AdBlock(MainActivity.this).isAd(request.getUrl().toString())) {
                        return new WebResourceResponse(
                                "text/plain",
                                "UTF-8",
                                new ByteArrayInputStream("".getBytes())
                        );
                    }


                    for (int i = 0; i < DISABLE_DOWNLOADING.length; i++) {
                        if (request.getUrl().toString().contains(DISABLE_DOWNLOADING[i])) {
                            return new WebResourceResponse(
                                    "text/html",
                                    "UTF-8",
                                    new ByteArrayInputStream("<h1 style='margin:30px 0; font-size:35px;'>We cannot allow to download videos form this website.</h1>".getBytes())
                            );
                        }
                    }

                }

                return super.shouldInterceptRequest(view, request);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
              //  WebviewLoaded=true;
                OnPrepareBottomNav(bottomNavigationView.getMenu());
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new FBDownloader(MainActivity.this), "FBDownloader");
        webView.addJavascriptInterface(new IGDownloader(MainActivity.this), "IGDownloader");

        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(),
                "android");
        webView.setWebChromeClient(new WebChromeClient() {});

        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateUi();
                    }
                }, 1000);

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

    }


    public class FBDownloader {
        Context mContext;


        FBDownloader(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void processVideo(String s, String s1)
        {
            //Log.e("vid_data", s);
            Log.e("vid_id", s1);
            final String vid_data=s;
            final String vid_id=s1;
         //  iUtils.ShowToast(MainActivity.this,vid_id);
            final String  url = "https://m.facebook.com/video.php?story_fbid="+vid_id;

           GeneratingDownloadLinks.Start(MainActivity.this, vid_data, "Facebook Video");
            showInterstitial();



        }
    }
    public class IGDownloader {
        Context mContext;


        IGDownloader(Context c) {
            mContext = c;
        }


        @JavascriptInterface
        public void processVideo(String s)
        {
            //Log.e("vid_data", s);
           // Log.e("vid_id", s1);
            final String vid_data=s;

            //  iUtils.ShowToast(MainActivity.this,vid_id);
           // final String  url = "https://m.facebook.com/video.php?story_fbid="+vid_id;

            GeneratingDownloadLinks.Start(MainActivity.this, vid_data, "Instagram Video");
            showInterstitial();



        }
    }


    class MyJavaScriptInterface {
        @JavascriptInterface
        public void onUrlChange(String url) {

        }
    }
   private void loadInterstitialAd() {
       if(AdNetwork.equals("Admob")) {
           new AdmobInterstitial().LoadAd(this, AInterstitialID);
       }else if(AdNetwork.equals("Fan")){
           new FanInterstitial().LoadAd(this, FInterstitialID,false);

       }
   }
    private void loadBannerAd(){
        RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.Ad_container);
        if(AdNetwork.equals("Admob")) {
            new AdmobBanner().LoadAd(this, adContainer , ABannerID);
        }else if(AdNetwork.equals("Fan")){
            new FanBanner().LoadAd(this, adContainer ,FBannerID);
        }
    }
    private void showInterstitial() {
        if(AdNetwork.equals("Admob")) {
            new AdmobInterstitial().ShowAd(this);
        }else if(AdNetwork.equals("Fan")){
            new FanInterstitial().ShowAd(this);
        }
    }


    private  void GetMedia(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter URL to get media ");
// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Get", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //iUtils.ShowToast(MainActivity.this,input.getText().toString());
                String url = input.getText().toString();
                if(iUtils.checkURL(url)) {
                    GeneratingDownloadLinks.Start(MainActivity.this, input.getText().toString(), SessionID);
                }else{
                    iUtils.ShowToast(MainActivity.this,URL_NOT_SUPPORTED);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(webView.canGoBack()){

                back();

            }else{

                webView.loadUrl("");
                webView.stopLoading();

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Rate us")
                        .setMessage(getString(R.string.rating))
                        .setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, close
                                // current activity
                                dialog.cancel();
                                finish();

                            }
                        }).setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                                final RatingDialog ratingDialog = new RatingDialog.Builder(MainActivity.this)
                                        .threshold(4)
                                        .session(1)
                                        .positiveButtonTextColor(R.color.black)
                                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                                            @Override
                                            public void onFormSubmitted(String feedback) {
                                                Toast.makeText(MainActivity.this,"Thank you for the suggestions and your feedback.",Toast.LENGTH_LONG).show();
                                            }
                                        }).build();

                                ratingDialog.show();
                            }
                        });
                builder.create().show();
                }
        }
    }
    public  void DownloadHandle(boolean show , final String postUrl){

        if(show) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.VISIBLE);
                }
            });
        }else{

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.GONE);
                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public boolean OnPrepareBottomNav(Menu menu){
      //  Log.e("ERROR BOOKMARK",webView.getUrl());
   if (!iUtils.isBookmarked(this,SearchText.getText().toString())) {
       menu.getItem(1).setIcon(R.drawable.ic_bookmark_border_grey_800_24dp);
       menu.getItem(1).getIcon().setAlpha(130);
   } else {
            menu.getItem(1).setIcon(R.drawable.ic_bookmark_grey_800_24dp);
       menu.getItem(1).getIcon().setAlpha(255);

   }

        if (!webView.canGoBack()) {
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        } else {
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }

        if (!webView.canGoForward()) {
            menu.getItem(3).setEnabled(false);
            menu.getItem(3).getIcon().setAlpha(130);
        } else {
            menu.getItem(3).setEnabled(true);
            menu.getItem(3).getIcon().setAlpha(255);
        }



        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

         return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_downloads) {

        // startActivity(new Intent("android.intent.action.VIEW_DOWNLOADS"));
            GetMedia();
            return  true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void back() {
        if (webView.canGoBack()) {
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();

            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()-1).getUrl();

            webView.goBack();
            SearchText.setText(historyUrl);

        }
    }

    private void forward() {
        if (webView.canGoForward()) {
            WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex()+1).getUrl();
            webView.goForward();
            SearchText.setText(webView.getUrl());

        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }


    }

    private boolean isNeedGrantPermission() {
        try {
            if (IOUtils.hasMarsallow()) {
                if (ContextCompat.checkSelfPermission(this, REQUEST_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUEST_PERMISSION)) {
                        final String msg = String.format(getString(R.string.format_request_permision), getString(R.string.app_name));

                        AlertDialog.Builder localBuilder = new AlertDialog.Builder(MainActivity.this);
                        localBuilder.setTitle("Permission Required!");
                        localBuilder
                                .setMessage(msg).setNeutralButton("Grant",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface paramAnonymousDialogInterface,
                                            int paramAnonymousInt) {
                                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{REQUEST_PERMISSION}, REQUEST_PERMISSION_CODE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface paramAnonymousDialogInterface,
                                            int paramAnonymousInt) {


                                        paramAnonymousDialogInterface.dismiss();
                                        finish();
                                    }
                                });
                        localBuilder.show();

                    }
                    else {
                        ActivityCompat.requestPermissions(this, new String[]{REQUEST_PERMISSION}, REQUEST_PERMISSION_CODE);
                    }
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //
                }
                else {
                    iUtils.ShowToast(MainActivity.this,getString(R.string.info_permission_denied));

                    finish();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            iUtils.ShowToast(MainActivity.this,getString(R.string.info_permission_denied));
            finish();
        }

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_downloads) {
            startActivity(new Intent("android.intent.action.VIEW_DOWNLOADS"));
        } else if (id == R.id.nav_bookmarks) {
            Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_get_media) {
            GetMedia();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out best video downloader at: https://play.google.com/store/apps/details?id="+MainActivity.this.getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (id == R.id.nav_send) {
//            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                    "mailto",EMAIL_ID, null));
//            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        webView.loadUrl(getString(R.string.privacy));
            WebviewLoaded=false;
            SwithcView(true);
        }else if (id == R.id.nav_rate) {
//            Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
//            }
            final RatingDialog ratingDialog = new RatingDialog.Builder(MainActivity.this)
                    .threshold(4)
                    .session(1)
                    .positiveButtonTextColor(R.color.black)
                    .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                        @Override
                        public void onFormSubmitted(String feedback) {
                            Toast.makeText(MainActivity.this,"Thank you for the suggestions and your feedback.",Toast.LENGTH_LONG).show();
                        }
                    }).build();

            ratingDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
//        if (mAdView != null) {
//            mAdView.resume();
//        }
        AdblockEnabled=sharedPrefs.getBoolean("ADBLOCK",true);
        webView.onResume();
        webView.resumeTimers();


    }
    @Override
    public void onPause(){
//        if (mAdView != null) {
//            mAdView.pause();
//        }
        super.onPause();
        // put your code here...
        webView.onPause();
        webView.pauseTimers();

    }
    @Override
    public void onStop(){
        super.onStop();

    }
    @Override
    protected void onDestroy() {
//        if (mAdView != null) {
//            mAdView.destroy();
//        }
        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView=null;
     }
}
