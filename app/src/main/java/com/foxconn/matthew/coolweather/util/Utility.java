package com.foxconn.matthew.coolweather.util;

/**
 * Created by Matthew on 2017/11/4.
 */

import android.text.TextUtils;

import com.foxconn.matthew.coolweather.db.City;
import com.foxconn.matthew.coolweather.db.Conty;
import com.foxconn.matthew.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析Json数据
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级的数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObj = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObj.getString("name"));
                    province.setProvinceCode(provinceObj.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级的数据
     */
    public static boolean handleCityResponse(String response,int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCitys = new JSONArray(response);
                for (int i = 0; i < allCitys.length(); i++) {
                    JSONObject cityObj = allCitys.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObj.getString("name"));
                    city.setCityCode(cityObj.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级的数据
     */
    public static boolean handleContyResponse(String response,int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allContys = new JSONArray(response);
                for (int i = 0; i < allContys.length(); i++) {
                    JSONObject contyObj = allContys.getJSONObject(i);
                    Conty conty = new Conty();
                    conty.setContyName(contyObj.getString("name"));
                    conty.setWeatherId(contyObj.getString("weather_id"));
                    conty.setCityId(cityId);
                    conty.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
