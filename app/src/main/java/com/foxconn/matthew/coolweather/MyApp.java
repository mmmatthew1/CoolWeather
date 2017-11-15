package com.foxconn.matthew.coolweather;


import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by Matthew on 2017/11/14.
 */

public class MyApp extends LitePalApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
