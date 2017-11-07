package com.foxconn.matthew.coolweather;

import com.foxconn.matthew.coolweather.util.HttpUtil;
import com.foxconn.matthew.coolweather.util.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Matthew on 2017/11/4.
 */

public class Test {

    @org.junit.Test
    public void testOKhttp(){
        HttpUtil.sendOkHttpRequest("http://guolin.tech/api/china", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
