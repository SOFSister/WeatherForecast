package com.example.weatherforecast.ui.setup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Paint;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.weatherforecast.DB.AppDatabase;
import com.example.weatherforecast.DB.InterestedCity;
import com.example.weatherforecast.R;
import com.example.weatherforecast.SPUtils;
import com.example.weatherforecast.WeatherViewModel;
import com.example.weatherforecast.notification.AddNotification;
import com.example.weatherforecast.ui.map.MapFragment;
import com.example.weatherforecast.ui.setup.interestedcity.CityTx;
import com.example.weatherforecast.ui.setup.interestedcity.CityTxAdapter;
import com.example.weatherforecast.ui.weather.today.Tx;
import com.example.weatherforecast.ui.weather.today.TxOtherCityAdapter;
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

    private List<CityTx> txList = new ArrayList<>();//????????????????????? ????????????

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
    private WeatherViewModel weatherViewModel;

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
        weatherViewModel = new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);

        //?????? ?????? ?????? ???????????? ????????????
        initAccount();
        //???????????????????????????
        initMaxItem();

        //?????????????????????
        initChooseLocalCity();
        //???????????????????????????
        initAddInterestedCity();
        //??????????????????????????????
        weatherViewModel.getInterestedCity().observe(getViewLifecycleOwner(), new Observer<List<InterestedCity>>() {
            @Override
            public void onChanged(@Nullable List<InterestedCity> s) {
                txList=initTxs(s);
                RecyclerView recyclerView = view.findViewById(R.id.interested_city_recycler);//??????RecyclerView??????
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());//???????????????
                recyclerView.setLayoutManager(layoutManager);
                CityTxAdapter adapter = new CityTxAdapter(txList);//???????????????
                recyclerView.setAdapter(adapter);//?????????????????????????????????*/
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            }
        });


        //??????????????????
        weatherViewModel.getCityMsgEvent().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //????????????
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

    private List<CityTx> initTxs(List<InterestedCity> s){
        List<CityTx> txList = new ArrayList<>();
        for(InterestedCity perCity:s){
            CityTx city = new CityTx(perCity.getInterestedCityName(), R.drawable.interested);
            txList.add(city);//???????????????
        }
        return txList;
    }
    private void initChooseLocalCity(){
        CityPickerView mPicker=new CityPickerView();
        mPicker.init(getContext());
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????demo
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        //???????????????????????????????????????
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //??????province
                //??????city
                //??????district
                String aim="";
                aim=province.toString()+city.toString()+district.toString();
                localCityText.setText(aim);
                MapFragment.nowCity=city.toString();
                MapFragment.nowCityMsg=aim;
                AddNotification.showCityChanged(getResources(),getContext(),"????????????","??????????????????????????????"+aim);
                MapFragment.flag=true;
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

    public List<InterestedCity> selectCityData(View view) {
        return AppDatabase.getInstance().interestedCityDao().loadAll();
    }
    private void initAddInterestedCity(){
        CityPickerView mPicker=new CityPickerView();
        mPicker.init(getContext());
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????demo
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        //???????????????????????????????????????
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                //??????province
                //??????city
                //??????district
                String aim="";
                aim=province.toString()+city.toString()+district.toString();
                insertData(view,city.toString());
                List<InterestedCity> nowInterestedCity=selectCityData(view);
                weatherViewModel.setInterestedCity(nowInterestedCity);
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
                Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
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
     * ????????????
     * @param view
     */
    public void insertData(View view,String city) {

        InterestedCity interestedCity=new InterestedCity();
        interestedCity.setInterestedCityName(city);
        AppDatabase.getInstance().interestedCityDao().insertAll(interestedCity);
        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
    }
}