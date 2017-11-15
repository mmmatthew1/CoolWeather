package com.foxconn.matthew.coolweather.util;

import android.text.TextUtils;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**
 * Created by Matthew on 2017/11/14.
 */

public class DialogUtil {


    public static void showProgressDialog(SVProgressHUD svProgressHUD) {
        dissmissProgressDialog(svProgressHUD);
        svProgressHUD.showWithStatus("加载中",SVProgressHUD.SVProgressHUDMaskType.Black);
    }

    public static void showProgressDialog(SVProgressHUD svProgressHUD,String msg) {
        if(TextUtils.isEmpty(msg)){
            showProgressDialog(svProgressHUD);
        }else {
            svProgressHUD.showWithStatus(msg,SVProgressHUD.SVProgressHUDMaskType.Black);
        }

    }

    public static void dissmissProgressDialog(SVProgressHUD svProgressHUD) {

        if (svProgressHUD.isShowing()) {
            svProgressHUD.dismiss();
        }
    }
}
