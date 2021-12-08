package com.example.weatherforecast.ui.weather.today;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.weatherforecast.MainActivity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherViewModel;
import com.example.weatherforecast.queryWeather.Forecast;
import com.example.weatherforecast.queryWeather.QueryWeather;
import com.example.weatherforecast.queryWeather.Weather;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TodayWeatherFragment extends Fragment {
    private static List<Tx> txList = new ArrayList<>();//一个全局的链表 详细天气
    private List<Tx> otherCityList = new ArrayList<>();//一个全局的链表 其他城市信息
    private static RecyclerView recyclerView;
    private static TextView titleNowCity;
    private static LinearLayoutManager layoutManager;

    private TodayWeatherViewModel mViewModel;
    public static Handler handler=new Handler(Looper.getMainLooper()){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    Weather cityWeather = (Weather) bundle.getSerializable("weather");
                    Forecast todayWeather=cityWeather.getData().getForecast().get(0);
                    String hasText=titleNowCity.getText().toString();
                    titleNowCity.setText(hasText+"  "+todayWeather.getHigh().substring(3)+"/"+todayWeather.getLow().substring(3));

                    txList=initTxs(todayWeather);//下面的初始化方法
                    recyclerView.setLayoutManager(layoutManager);
                    TxAdapter adapter = new TxAdapter(txList);//适配器对象
                    recyclerView.setAdapter(adapter);//设置适配器为上面的对象
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
        View view=inflater.inflate(R.layout.today_weather_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler);//找到RecyclerView控件
        layoutManager = new LinearLayoutManager(getContext());//布局管理器
        //其他城市
        otherCityList=initOtherCityTxs();
        RecyclerView recyclerOtherCityView = view.findViewById(R.id.other_city_recycler);//找到RecyclerView控件
        LinearLayoutManager layoutOtherCityManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerOtherCityView.setLayoutManager(layoutOtherCityManager);
        TxOtherCityAdapter adapterOtherCity = new TxOtherCityAdapter(otherCityList);//适配器对象
        recyclerOtherCityView.setAdapter(adapterOtherCity);//设置适配器为上面的对象
        recyclerOtherCityView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        //初始化变量
        titleNowCity=view.findViewById(R.id.title_now_city);

        //监听当前城市
        WeatherViewModel weatherViewModel = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);
        weatherViewModel.getCityEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //查询天气
                titleNowCity.setText(s);
                Log.i("debug666","xxx");
                QueryWeather.queryWeather(s);
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
    private List<Tx> initOtherCityTxs(){
        List<Tx> txList = new ArrayList<>();
        Tx city = new Tx("北京","晴8/-5°", R.drawable.city);
        txList.add(city);//加入到链表
        return txList;
    }
}