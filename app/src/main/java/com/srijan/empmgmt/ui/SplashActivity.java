package com.srijan.empmgmt.ui;

import androidx.appcompat.app.AppCompatActivity;


import com.srijan.empmgmt.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {



    private int SPLASH_LENGTH=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



               Intent i = new Intent(SplashActivity.this, SelectActivity.class);
               startActivity(i);
                finish();
            }
        }, SPLASH_LENGTH);


    }
}



