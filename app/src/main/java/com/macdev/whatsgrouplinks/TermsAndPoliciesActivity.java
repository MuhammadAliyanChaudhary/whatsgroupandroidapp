package com.macdev.whatsgrouplinks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class TermsAndPoliciesActivity extends AppCompatActivity {

    WebView webViewTerms;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_policies);


        webViewTerms = findViewById(R.id.webViewTerms);

        toolbar = findViewById(R.id.toolbarGroupTerms);
        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webViewTerms.getSettings().setJavaScriptEnabled(true);
        webViewTerms.loadUrl("file:///android_asset/terms.html");


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}