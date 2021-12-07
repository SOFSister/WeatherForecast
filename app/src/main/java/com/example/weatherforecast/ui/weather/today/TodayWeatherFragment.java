package com.example.weatherforecast.ui.weather.today;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

public class TodayWeatherFragment extends Fragment {
    private List<Tx> txList = new ArrayList<>();//一个全局的链表 详细天气
    private List<Tx> otherCityList = new ArrayList<>();//一个全局的链表 其他城市信息

    private TodayWeatherViewModel mViewModel;

    public static TodayWeatherFragment newInstance() {
        return new TodayWeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.today_weather_fragment, container, false);
        txList=initTxs();//下面的初始化方法
        RecyclerView recyclerView = view.findViewById(R.id.recycler);//找到RecyclerView控件
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerView.setLayoutManager(layoutManager);
        TxAdapter adapter = new TxAdapter(txList);//适配器对象
        recyclerView.setAdapter(adapter);//设置适配器为上面的对象
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        //其他城市
        otherCityList=initOtherCityTxs();
        RecyclerView recyclerOtherCityView = view.findViewById(R.id.other_city_recycler);//找到RecyclerView控件
        LinearLayoutManager layoutOtherCityManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerOtherCityView.setLayoutManager(layoutOtherCityManager);
        TxAdapter adapterOtherCity = new TxAdapter(otherCityList);//适配器对象
        recyclerOtherCityView.setAdapter(adapterOtherCity);//设置适配器为上面的对象
        recyclerOtherCityView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TodayWeatherViewModel.class);
        // TODO: Use the ViewModel
    }
    private List<Tx> initTxs(){
        List<Tx> txList = new ArrayList<>();
        Tx weather = new Tx("天气        晴", R.drawable.weather);
        txList.add(weather);//加入到链表
        Tx temperature = new Tx("温度        15/5°", R.drawable.temperature);
        txList.add(temperature);//加入到链表
        Tx calendar = new Tx("日期        2021/12/6", R.drawable.calendar);
        txList.add(calendar);//加入到链表
        Tx windDirection = new Tx("风向        西北风", R.drawable.winddirection);
        txList.add(windDirection);//加入到链表
        Tx windForce = new Tx("风速        3级", R.drawable.windforce);
        txList.add(windForce);//加入到链表
        return txList;
    }
    private List<Tx> initOtherCityTxs(){
        List<Tx> txList = new ArrayList<>();
        Tx city = new Tx("北京        晴8/-5°", R.drawable.city);
        txList.add(city);//加入到链表
        return txList;
    }
}