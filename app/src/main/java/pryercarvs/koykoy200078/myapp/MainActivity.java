package pryercarvs.koykoy200078.myapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import pryercarvs.koykoy200078.myapp.ui.UIManager;
import pryercarvs.koykoy200078.myapp.webview.WebViewHelper;

public class MainActivity extends AppCompatActivity {
    private WebViewHelper webViewHelper;
    private boolean intentHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup Theme
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the FirebaseAnalytics instance.
        FirebaseAnalytics.getInstance(this);

        // Admob
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Setup Helpers
        // Globals
        UIManager uiManager = new UIManager(this);
        webViewHelper = new WebViewHelper(this, uiManager);

        // Setup App
        webViewHelper.setupWebView();
        uiManager.changeRecentAppsIcon();

        // Check for Intents
        try {
            Intent i = getIntent();
            String intentAction = i.getAction();
            // Handle URLs opened in Browser
            if (!intentHandled && intentAction != null && intentAction.equals(Intent.ACTION_VIEW)) {
                Uri intentUri = i.getData();
                String intentText = "";
                if (intentUri != null) {
                    intentText = intentUri.toString();
                }
                // Load up the URL specified in the Intent
                if (!intentText.equals("")) {
                    intentHandled = true;
                    webViewHelper.loadIntentUrl(intentText);
                }
            } else {
                // Load up the Web App
                webViewHelper.loadHome();
            }
        } catch (Exception e) {
            // Load up the Web App
            webViewHelper.loadHome();
        }
    }

    @Override
    protected void onPause() {
        webViewHelper.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        webViewHelper.onResume();
        // retrieve content from cache primarily if not connected
        webViewHelper.forceCacheIfOffline();
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FirebaseApp.initializeApp(this);
    }

    // Handle back-press in browser
    @Override
    public void onBackPressed() {
        if (!webViewHelper.goBack()) {
            super.onBackPressed();
        }
    }
}