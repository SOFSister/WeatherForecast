package com.example.weatherforecast.ui.setup;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.ui.setup.interestedcity.CityTx;
import com.example.weatherforecast.ui.setup.interestedcity.CityTxAdapter;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;


import java.util.ArrayList;
import java.util.List;

public class SetupFragment extends Fragment {

    private SetupViewModel mViewModel;

    private List<CityTx> txList = new ArrayList<>();//一个全局的链表 详细天气

    private Button localCityBtn;
    private TextView localCityText;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.setup_fragment, container, false);
        txList=initTxs();//下面的初始化方法
        RecyclerView recyclerView = view.findViewById(R.id.interested_city_recycler);//找到RecyclerView控件
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerView.setLayoutManager(layoutManager);
        CityTxAdapter adapter = new CityTxAdapter(txList);//适配器对象
        recyclerView.setAdapter(adapter);//设置适配器为上面的对象*/
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        //初始化选择城市
        localCityText=view.findViewById(R.id.local_city_text);
        CityPickerView mPicker=new CityPickerView();
        mPicker.init(getContext());
        //添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        //监听选择点击事件及返回结果
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //省份province
                //城市city
                //地区district
                String aim="";
                aim=province.toString()+city.toString()+district.toString();
                localCityText.setText(aim);
            }

            @Override
            public void onCancel() {

            }
        });
        localCityBtn=view.findViewById(R.id.change_local_city);
        localCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPicker.showCityPicker();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetupViewModel.class);
        // TODO: Use the ViewModel
    }

    private List<CityTx> initTxs(){
        List<CityTx> txList = new ArrayList<>();
        CityTx city1 = new CityTx("北京", R.drawable.interested);
        txList.add(city1);

        CityTx city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);

        city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);city2 = new CityTx("重庆", R.drawable.interested);
        txList.add(city2);
        return txList;
    }
}