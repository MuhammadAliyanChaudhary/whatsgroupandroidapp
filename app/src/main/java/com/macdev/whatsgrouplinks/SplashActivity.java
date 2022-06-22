package com.macdev.whatsgrouplinks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {


                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashActivity.this, BottomNavigationActivity.class);
                    startActivity(mainIntent);
                    finish();

            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}