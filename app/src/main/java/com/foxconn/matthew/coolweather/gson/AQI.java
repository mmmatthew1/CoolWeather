package com.foxconn.matthew.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Matthew on 2017/11/6.
 */

public class AQI {
    @SerializedName("city")
    public AQICity aqiCity;

    public class AQICity {
        @SerializedName("aqi")
        public String aqi;
        @SerializedName("pm25")
        public String pm25;
    }
}
