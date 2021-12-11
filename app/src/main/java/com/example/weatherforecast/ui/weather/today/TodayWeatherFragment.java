package com.example.weatherforecast.ui.weather.today;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.DB.AppDatabase;
import com.example.weatherforecast.DB.InterestedCity;
import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherViewModel;
import com.example.weatherforecast.queryWeather.Forecast;
import com.example.weatherforecast.queryWeather.QueryWeather;
import com.example.weatherforecast.queryWeather.Weather;
import com.example.weatherforecast.queryWeather.Weathers;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodayWeatherFragment extends Fragment {
    private static List<Tx> txList = new ArrayList<>();//一个全局的链表 详细天气
    private static List<Tx> otherCityList = new ArrayList<>();//一个全局的链表 其他城市信息
    private View view;
    private static RecyclerView recyclerView;
    private static TextView titleNowCity;
    private static LinearLayoutManager layoutManager;
    private static RecyclerView recyclerOtherCityView;
    private static LinearLayoutManager layoutOtherCityManager;
    private static Context context;
    public static List<Weather>cityWeathers;
    private ImageView cityImg;

    private TodayWeatherViewModel mViewModel;
    public static Handler handler=new Handler(Looper.getMainLooper()){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(android.os.Message msg){
            Bundle bundle = msg.getData();
            switch (msg.what){
                case 1:
                    Weather cityWeather = (Weather) bundle.getSerializable("weather");
                    Forecast todayWeather=cityWeather.getData().getForecast().get(0);
                    String hasText=titleNowCity.getText().toString();
                    titleNowCity.setText(hasText+"  "+todayWeather.getHigh().substring(3)+"/"+todayWeather.getLow().substring(3));

                    txList=initTxs(todayWeather);//下面的初始化方法
                    recyclerView.setLayoutManager(layoutManager);
                    TxAdapter adapter = new TxAdapter(txList);//适配器对象
                    recyclerView.setAdapter(adapter);//设置适配器为上面的对象
                    break;
                case 2:
                    Weathers weathers= (Weathers) bundle.getSerializable("weathers");
                    cityWeathers=weathers.getWeathers();
                    otherCityList=initOtherCityTxs(cityWeathers);
                    recyclerOtherCityView.setLayoutManager(layoutOtherCityManager);
                    TxOtherCityAdapter adapterOtherCity = new TxOtherCityAdapter(otherCityList);//适配器对象
                    recyclerOtherCityView.setAdapter(adapterOtherCity);//设置适配器为上面的对象
                    recyclerOtherCityView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
                    break;
            }
        }
    };

    public static TodayWeatherFragment newInstance() {
        return new TodayWeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //初始化recyclerview
        view=inflater.inflate(R.layout.today_weather_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler);//找到RecyclerView控件
        layoutManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerOtherCityView = view.findViewById(R.id.other_city_recycler);//找到RecyclerView控件
        layoutOtherCityManager = new LinearLayoutManager(getContext());//布局管理器
        cityImg=view.findViewById(R.id.city_img);
        context=getContext();
        //其他城市

        //初始化变量
        titleNowCity=view.findViewById(R.id.title_now_city);


        WeatherViewModel weatherViewModel = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);
        //监听城市变化
        weatherViewModel.getCityEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //查询天气
                titleNowCity.setText(s);
                if (s.equals("杭州市"))
                    cityImg.setBackgroundResource(R.drawable.hangzhou);
                else
                    cityImg.setBackgroundResource(R.drawable.othercity);
                QueryWeather.queryWeather(s);
            }
        });
        //监听感兴趣城市的变化
        weatherViewModel.getInterestedCity().observe(getViewLifecycleOwner(), new Observer<List<InterestedCity>>() {
            @Override
            public void onChanged(@Nullable List<InterestedCity> s) {
                QueryWeather.queryWeathers(s);
                /*otherCityList=initOtherCityTxs(s);
                RecyclerView recyclerOtherCityView = view.findViewById(R.id.other_city_recycler);//找到RecyclerView控件
                LinearLayoutManager layoutOtherCityManager = new LinearLayoutManager(getContext());//布局管理器
                recyclerOtherCityView.setLayoutManager(layoutOtherCityManager);
                TxOtherCityAdapter adapterOtherCity = new TxOtherCityAdapter(otherCityList);//适配器对象
                recyclerOtherCityView.setAdapter(adapterOtherCity);//设置适配器为上面的对象
                recyclerOtherCityView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));*/
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TodayWeatherViewModel.class);
        // TODO: Use the ViewModel
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<Tx> initTxs(Forecast todayWeather){
        List<Tx> txList = new ArrayList<>();
        Tx weather = new Tx("天气",todayWeather.getType(), R.drawable.weather);
        txList.add(weather);//加入到链表
        Tx temperature = new Tx("温度",todayWeather.getHigh().substring(3)+"/"+todayWeather.getLow().substring(3), R.drawable.temperature);
        txList.add(temperature);//加入到链表
        LocalDate date = LocalDate.now(); // get the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //Tx calendar = new Tx("日期", todayWeather.getDate(), R.drawable.calendar);
        Tx calendar = new Tx("日期", date.format(formatter), R.drawable.calendar);
        txList.add(calendar);//加入到链表
        Tx windDirection = new Tx("风向", todayWeather.getFengxiang(), R.drawable.winddirection);
        txList.add(windDirection);//加入到链表
        Tx windForce = new Tx("风速", todayWeather.getFengli().substring(9,11), R.drawable.windforce);
        txList.add(windForce);//加入到链表
        return txList;
    }
    /*private List<Tx> initOtherCityTxs(List<InterestedCity> s){
        List<Tx> txList = new ArrayList<>();
        for(InterestedCity perCity:s){
            Tx city = new Tx(perCity.getInterestedCityName(),"晴8/-5°", R.drawable.city);
            txList.add(city);//加入到链表
        }
        return txList;
    }*/
    private static List<Tx> initOtherCityTxs(List<Weather> cityWeathers){
        List<Tx> txList = new ArrayList<>();
        for(Weather weather:cityWeathers){
            Forecast todayWeather=weather.getData().getForecast().get(0);
            Tx city = new Tx(weather.getData().getCity(),todayWeather.getHigh().substring(3)+"/"+todayWeather.getLow().substring(3), R.drawable.city);
            txList.add(city);//加入到链表
        }
        /*for(InterestedCity perCity:s){
            Tx city = new Tx(perCity.getInterestedCityName(),"晴8/-5°", R.drawable.city);
            txList.add(city);//加入到链表
        }*/
        return txList;
    }

}