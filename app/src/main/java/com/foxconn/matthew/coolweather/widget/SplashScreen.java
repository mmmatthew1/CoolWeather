package com.foxconn.matthew.coolweather.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.foxconn.matthew.coolweather.R;

/**
 * Created by Matthew on 2017/11/14.
 */

public class SplashScreen {

    public static final int SLIDE_LEFT = 1;
    public static final int SLIDE_UP = 2;
    public static final int FADE_OUT = 3;


    private Dialog splashDialog;
    private Activity activity;

    public SplashScreen(Activity activity) {
        this.activity = activity;
    }


    public void show(final int imageResource, final int animation) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                //Create rootLayout for dialog
                LinearLayout rootLayout = new LinearLayout(activity);
                rootLayout.setMinimumHeight(metrics.heightPixels);
                rootLayout.setMinimumWidth(metrics.widthPixels);
                rootLayout.setOrientation(LinearLayout.VERTICAL);
                rootLayout.setBackgroundColor(Color.BLACK);
                rootLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT,
                        0.0F));
                rootLayout.setBackgroundResource(imageResource);

                //create dialog
                splashDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar);

                //check if necessary to set fullscreen
                if ((activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                        == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                    splashDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                Window window = splashDialog.getWindow();

                switch (animation) {
                    case SLIDE_LEFT:
                        window.setWindowAnimations(R.style.dialog_anim_slide_left);
                        break;
                    case SLIDE_UP:
                        window.setWindowAnimations(R.style.dialog_anim_slide_up);
                        break;
                    case FADE_OUT:
                        window.setWindowAnimations(R.style.dialog_anim_fade_out);
                        break;
                }

                splashDialog.setContentView(rootLayout);
                splashDialog.setCancelable(false);
                splashDialog.show();
            }
        };
        activity.runOnUiThread(runnable);
    }


    public void removeSplashScreen() {
        if (splashDialog != null && splashDialog.isShowing()) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }
}
