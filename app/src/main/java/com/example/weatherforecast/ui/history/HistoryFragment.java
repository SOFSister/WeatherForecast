package com.example.weatherforecast.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.b.s;
import com.example.weatherforecast.DB.AppDatabase;
import com.example.weatherforecast.DB.InterestedCity;
import com.example.weatherforecast.DB.WeatherHistory;
import com.example.weatherforecast.R;
import com.example.weatherforecast.SPUtils;
import com.example.weatherforecast.ui.weather.today.Tx;
import com.example.weatherforecast.ui.weather.today.TxOtherCityAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {
    private static List<HistoryTX> historyList = new ArrayList<>();
    private List<WeatherHistory>weatherHistoryList;
    private View view;
    private SPUtils sp;
    private HistoryViewModel mViewModel;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("lishi","onCreateView");
        view=inflater.inflate(R.layout.history_fragment, container, false);
        weatherHistoryList=selectHistoryData(view);
        Map<String, Boolean>map=new HashMap<>();
        historyList.clear();
        sp=new SPUtils(getContext(),"weather");
        int maxItem=sp.getInt("maxItem");
        for(WeatherHistory weatherHistory:weatherHistoryList){
            if(maxItem==0){
                break;
            }
            String cityName=weatherHistory.getCityName();
            if(map.containsKey(cityName)){
                continue;
            }
            map.put(cityName,true);
            maxItem--;
            historyList.add(initHistoryTxs(cityName));
        }
        RecyclerView recyclerHistoryView = view.findViewById(R.id.recycler);//找到RecyclerView控件
        LinearLayoutManager layoutOtherCityManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerHistoryView.setLayoutManager(layoutOtherCityManager);
        HistoryTxAdapter adapterOtherCity = new HistoryTxAdapter(historyList);//适配器对象
        recyclerHistoryView.setAdapter(adapterOtherCity);//设置适配器为上面的对象
        recyclerHistoryView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        // TODO: Use the ViewModel
    }
    public List<WeatherHistory> selectHistoryData(View view)
    {
        return AppDatabase.getInstance().weatherHistoryDao().loadAll();
    }
    private HistoryTX initHistoryTxs(String cityName){
        String[] date;//X轴的标注
        int[] high;//图表的数据点
        int[] low;//图表的数据点
        List<String>dates=new ArrayList<>();
        List<Integer>highs=new ArrayList<>();
        List<Integer>lows=new ArrayList<>();
        List<WeatherHistory> historyList=AppDatabase.getInstance().weatherHistoryDao().loadHistory(cityName);
        for(WeatherHistory weatherHistory:historyList){
            dates.add(weatherHistory.getDate());
            highs.add(weatherHistory.getHigh());
            lows.add(weatherHistory.getLow());
        }
        date=dates.toArray(new String[dates.size()]);
        high = new int[highs.size()];
        for(int i=0;i<highs.size();i++) {
            high[i] = highs.get(i);
        }
        low = new int[lows.size()];
        for(int i=0;i<lows.size();i++) {
            low[i] = lows.get(i);
        }
        HistoryTX historyTX=new HistoryTX(cityName,date,high,low);
        return historyTX;
    }
    @Override
    public void onResume() {
        Log.i("lishi","onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("lishi","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("lishi","onDestroy");
        super.onDestroy();
    }
}