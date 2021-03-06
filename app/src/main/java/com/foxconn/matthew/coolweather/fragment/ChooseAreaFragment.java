package com.foxconn.matthew.coolweather.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.foxconn.matthew.coolweather.activity.MainActivity;
import com.foxconn.matthew.coolweather.R;
import com.foxconn.matthew.coolweather.activity.WeatherActivity;
import com.foxconn.matthew.coolweather.db.City;
import com.foxconn.matthew.coolweather.db.Conty;
import com.foxconn.matthew.coolweather.db.Province;
import com.foxconn.matthew.coolweather.util.DialogUtil;
import com.foxconn.matthew.coolweather.util.HttpUtil;
import com.foxconn.matthew.coolweather.util.LogUtil;
import com.foxconn.matthew.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Matthew on 2017/11/4.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_CONTY = 2;

    private static final String TAG = "ChooseAreaFragment";

    private ProgressDialog progressDialog;

    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.back_bt)
    Button backButton;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.ll_data)
    LinearLayout ll_data;
    @BindView(R.id.ll_try_again)
    LinearLayout ll_try_again;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
    private List<Conty> contyList;

    /**
     * 选中的省份
     */
    private Province selectedProvince;

    /**
     * 选中的市
     */
    private City selectedCity;

    /**
     * 选中的县
     */
    private Conty selectedConty;


    /**
     * 当前选中的级别
     */
    private int currentLevel;

    private static final String ADDRESS = "http://guolin.tech/api/china";

    private SVProgressHUD svProgressHUD;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.e(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.choose_area, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @OnClick(R.id.btn_try_again)
    public void onClick(View view){
        queryProvince();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.e(TAG, "onActivityCreated");
        svProgressHUD = new SVProgressHUD(getActivity());
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        LogUtil.e(TAG, (listView == null) + "");
        LogUtil.e(TAG, (titleText == null) + "");
        LogUtil.e(TAG, (backButton == null) + "");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryConties();
                } else if (currentLevel == LEVEL_CONTY) {
                    String weatherId = contyList.get(position).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
                        weatherActivity.drawerLayout.closeDrawers();
                        weatherActivity.swipeRefresh.setRefreshing(true);
                        weatherActivity.requestWeather(weatherId);
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY) {
                    queryProvince();
                } else if (currentLevel == LEVEL_CONTY) {
                    queryCities();
                }
            }
        });
        queryProvince();
    }

    private void queryProvince() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(ADDRESS, "Province");
        }
    }

    private void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceId=?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            queryFromServer(ADDRESS + "/" + provinceCode, "City");
        }
    }

    private void queryConties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        contyList = DataSupport.where("cityId=?", String.valueOf(selectedCity.getId())).find(Conty.class);
        if (contyList.size() > 0) {
            dataList.clear();
            for (Conty conty : contyList)
                dataList.add(conty.getContyName());
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CONTY;
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            queryFromServer(ADDRESS + "/" + provinceCode + "/" + cityCode, "Conty");
        }
    }

    private void queryFromServer(String address, final String type) {
        DialogUtil.showProgressDialog(svProgressHUD, "正在加载");
        ll_try_again.setVisibility(View.GONE);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.dissmissProgressDialog(svProgressHUD);
                        LogUtil.e(TAG, "加载失败");
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        if("Province".equals(type)){
                            ll_try_again.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                LogUtil.e(TAG, responseText);
                boolean result = false;
                if ("Province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("City".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                } else if ("Conty".equals(type)) {
                    result = Utility.handleContyResponse(responseText, selectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.dissmissProgressDialog(svProgressHUD);
                            if ("Province".equals(type)) {
                                queryProvince();
                            } else if ("City".equals(type)) {
                                queryCities();
                            } else if ("Conty".equals(type)) {
                                queryConties();
                            }
                        }
                    });
                }
            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
