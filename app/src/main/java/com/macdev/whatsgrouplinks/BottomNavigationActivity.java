package com.macdev.whatsgrouplinks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.macdev.whatsgrouplinks.databinding.ActivityBottomNavigationBinding;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class BottomNavigationActivity extends AppCompatActivity {

    private ActivityBottomNavigationBinding binding;
    Toolbar toolbar;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar); //NO PROBLEM !!!!
        toolbar.setTitle("Whats Group Links");

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_groups, R.id.navigation_categories, R.id.navigation_addgroup)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.custom_action_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.rate_app_btn){
            rateApp();
        }
        if(itemId == R.id.share_app_btn){
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage= "\nLet me recommend you this Whatsapp Bulk Groups joining application\n\nWhats Group Links\n\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                e.toString();
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if(itemId == R.id.terms_and_policies){
            Intent intent = new Intent(BottomNavigationActivity.this, TermsAndPoliciesActivity.class);
            startActivity(intent);
        }

        if(itemId== R.id.privacy_policy){

            Intent intent = new Intent(BottomNavigationActivity.this, PrivacyActivity.class);
            startActivity(intent);


        }

        return true;
    }



    public void rateApp(){
        try{
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }



//    public void rateApp()
//    {
//        try
//        {
//            Intent rateIntent = rateIntentForUrl("market://details"+getPackageName());
//            startActivity(rateIntent);
//        }
//        catch (ActivityNotFoundException e)
//        {
//            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details"+getPackageName());
//            startActivity(rateIntent);
//        }
//    }

//    private Intent rateIntentForUrl(String url)
//    {
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
//        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
//        if (Build.VERSION.SDK_INT >= 21)
//        {
//            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
//        }
//        else
//        {
//            //noinspection deprecation
//            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
//        }
//        intent.addFlags(flags);
//        return intent;
//    }
}