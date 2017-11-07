package com.foxconn.matthew.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matthew on 2017/11/6.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    @SerializedName("update")
    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
