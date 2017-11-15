package com.foxconn.matthew.coolweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.foxconn.matthew.coolweather.MyApp;
import com.foxconn.matthew.coolweather.R;
import com.foxconn.matthew.coolweather.util.HandlerUtil;
import com.foxconn.matthew.coolweather.widget.SplashScreen;

/**
 * Created by Matthew on 2017/11/15.
 */

public class SplashActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SplashScreen splashScreen = new SplashScreen(this);
        splashScreen.show(R.drawable.splash_screen, SplashScreen.FADE_OUT);
        HandlerUtil.getInstance(this).postDelayed(new Runnable() {
            @Override
            public void run() {
                splashScreen.removeSplashScreen();
                Intent intent=new Intent(MyApp.getContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
