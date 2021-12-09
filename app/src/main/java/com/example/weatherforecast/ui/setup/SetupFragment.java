package com.example.weatherforecast.ui.setup;

import androidx.lifecycle.Observer;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecast.DB.AppDatabase;
import com.example.weatherforecast.DB.InterestedCity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.SPUtils;
import com.example.weatherforecast.WeatherViewModel;
import com.example.weatherforecast.queryWeather.QueryWeather;
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
    private Button addInterestedCity;
    private TextView accountTv;
    private EditText passwordEt;
    private CheckBox autoLogin;
    private View view;
    private SPUtils sp;
    private TextView maxItemTv;
    private Button itemAddBtn;
    private Button itemReduceBtn;
    private Button resetBtn;

    public static SetupFragment newInstance() {
        return new SetupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.setup_fragment, container, false);
        addInterestedCity=view.findViewById(R.id.add_interested_city);
        localCityText=view.findViewById(R.id.local_city_text);
        localCityBtn=view.findViewById(R.id.change_local_city);
        accountTv=view.findViewById(R.id.account_tx);
        passwordEt=view.findViewById(R.id.password_et);
        autoLogin=view.findViewById(R.id.auto_login);
        maxItemTv=view.findViewById(R.id.max_item_tv);
        itemAddBtn=view.findViewById(R.id.item_add_btn);
        itemReduceBtn=view.findViewById(R.id.item_reduce_btn);
        resetBtn=view.findViewById(R.id.reset_btn);
        sp=new SPUtils(getContext(),"weather");
        WeatherViewModel weatherViewModel = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);

        //初始 账号 密码 自动登录 修改密码
        initAccount();
        //初始化历史显示个数
        initMaxItem();

        txList=initTxs();//下面的初始化方法
        RecyclerView recyclerView = view.findViewById(R.id.interested_city_recycler);//找到RecyclerView控件
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//布局管理器
        recyclerView.setLayoutManager(layoutManager);
        CityTxAdapter adapter = new CityTxAdapter(txList);//适配器对象
        recyclerView.setAdapter(adapter);//设置适配器为上面的对象*/
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        //初始化选择城市
        initChooseLocalCity();
        //初始化添加兴趣城市
        initAddInterestedCity();

        //监听当前城市
        weatherViewModel.getCityMsgEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //查询天气
                localCityText.setText(s);
            }
        });

        //
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
    private void initChooseLocalCity(){
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
        localCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPicker.showCityPicker();
            }
        });
    }
    private void initAddInterestedCity(){
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
                insertData(view,city.toString());
                //localCityText.setText(aim);
            }

            @Override
            public void onCancel() {

            }
        });
        addInterestedCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPicker.showCityPicker();
            }
        });
    }
    private void initAccount(){
        String account=sp.getString("account");
        String password=sp.getString("password");
        boolean isLogin=sp.getBoolean("isLogin");
        accountTv.setText(account);
        passwordEt.setText(password);
        if(isLogin){
            autoLogin.setChecked(true);
        }
        autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLogin=sp.getBoolean("isLogin");
                sp.putBoolean("isLogin",!isLogin);
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword=passwordEt.getText().toString();
                sp.putString("password",newPassword);
            }
        });
    }
    private void initMaxItem(){
        int maxItem=sp.getInt("maxItem");
        maxItemTv.setText(String.valueOf(maxItem));
        itemAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newMaxItem=Math.min(sp.getInt("maxItem")+1,10);
                sp.putInt("maxItem",newMaxItem);
                maxItemTv.setText(String.valueOf(newMaxItem));
            }
        });
        itemReduceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newMaxItem=Math.max(sp.getInt("maxItem")-1,4);
                sp.putInt("maxItem",newMaxItem);
                maxItemTv.setText(String.valueOf(newMaxItem));
            }
        });
    }
    /**
     * 添加数据
     * @param view
     */
    public void insertData(View view,String city) {

        InterestedCity interestedCity=new InterestedCity();
        interestedCity.setInterestedCityName(city);
        AppDatabase.getInstance().interestedCityDao().insertAll(interestedCity);
        Toast.makeText(getContext(),"插入成功",Toast.LENGTH_SHORT).show();
    }
}