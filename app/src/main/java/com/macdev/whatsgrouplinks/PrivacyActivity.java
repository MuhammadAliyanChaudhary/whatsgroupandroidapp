package com.macdev.whatsgrouplinks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class PrivacyActivity extends AppCompatActivity {
    WebView webViewPrivacy;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);


        webViewPrivacy = findViewById(R.id.webViewPrivacy);

        toolbar = findViewById(R.id.toolbarGroupPrivacy);
        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webViewPrivacy.getSettings().setJavaScriptEnabled(true);
        webViewPrivacy.loadUrl("file:///android_asset/privacy.html");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}