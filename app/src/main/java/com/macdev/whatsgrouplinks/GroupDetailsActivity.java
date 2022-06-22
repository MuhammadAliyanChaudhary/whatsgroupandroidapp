package com.macdev.whatsgrouplinks;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.imageview.ShapeableImageView;

public class GroupDetailsActivity extends AppCompatActivity {

    private TextView groupName, groupCategory, groupLink;
    private Button copyLink, joinGroup;
    private ShapeableImageView imageGroup;
    private String groupUrlCopy;
    private Toolbar toolbar;

    AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);


        toolbar = findViewById(R.id.toolbarGroupInfo);
        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        toolbar.setTitle("Group Info");
        toolbar.getMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        groupName = findViewById(R.id.group_detail_name);
        groupCategory = findViewById(R.id.group_detail_category_name);
        groupLink = findViewById(R.id.group_detail_link);
        copyLink = findViewById(R.id.copy_group_link_btn);
        joinGroup = findViewById(R.id.group_detail_join_btn);
        imageGroup = findViewById(R.id.group_detail_Image);




        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });






        String imageLink = getIntent().getStringExtra("image");
        groupName.setText(getIntent().getStringExtra("groupTitle"));
        groupCategory.setText(getIntent().getStringExtra("groupCategory"));
        groupLink.setText(getIntent().getStringExtra("groupLink"));


        Glide.with(GroupDetailsActivity.this)
                .load(imageLink) // image url
                .placeholder(R.drawable.ic_image_24) // any placeholder to load at start
                .error(R.drawable.ic_image_24)  // any image in case of error
                .override(250, 250) // resizing
                .centerCrop()
                .into(imageGroup);




        groupUrlCopy = getIntent().getStringExtra("groupLink");

        copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setClipboard(GroupDetailsActivity.this, groupUrlCopy);
                Toast.makeText(GroupDetailsActivity.this, "Link Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });


        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(groupUrlCopy));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }


    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}