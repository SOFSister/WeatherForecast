package com.example.weatherforecast.ui.map;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.weatherforecast.R;
import com.example.weatherforecast.WeatherViewModel;

public class MapFragment extends Fragment {
    private WeatherViewModel weatherViewModel;
    private MapViewModel mViewModel;
    private MapView mMapView;
    private TextView mtextView;
    private static BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private static Boolean isFirstLocate=true;
    public static Boolean flag=true;
    private MyLocationConfiguration.LocationMode locationMode;
    public static String nowCity="";
    public static String nowCityMsg="";
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.map_fragment, container, false);
        weatherViewModel=new ViewModelProvider(getActivity(),new ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel.class);
        //初始化地图
        //获取地图控件引用
        mMapView = view.findViewById(R.id.bmapView);
        //获取文本显示控件
        mtextView = view.findViewById(R.id.text_tishi);
        // 得到地图
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位初始化
        mLocationClient = new LocationClient(getContext());

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        // 可选，设置地址信息
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }
    // 继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null || !flag){
                return;
            }
            flag=false;
            // 如果是第一次定位
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirstLocate) {
                isFirstLocate = false;
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);

                /*// ------------------  以下是可选部分 ------------------
                // 自定义地图样式，可选
                // 更换定位图标，这里的图片是放在 drawble 文件下的
                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.ic_map);
                // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
                locationMode = MyLocationConfiguration.LocationMode.NORMAL;
                // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
                MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(locationMode,true,mCurrentMarker);
                // 使自定义的配置生效
                mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);*/
                // ------------------  可选部分结束 ------------------

                mtextView.setText(location.getProvince()+location.getCity()+location.getDistrict());
                Log.i("城市",location.getCity());
                //更新当前城市
                weatherViewModel.setCityEvent(location.getCity());
                nowCity=location.getCity();
                nowCityMsg=location.getProvince()+location.getCity()+location.getDistrict();
                weatherViewModel.setCityMsgEvent(nowCityMsg);
                /*if(!nowCity.equals(location.getCity())){
                    nowCity=location.getCity();
                    weatherViewModel.setCityEvent(location.getCity());
                }
                if(!nowCityMsg.equals(location.getProvince()+location.getCity()+location.getDistrict())){
                    nowCityMsg=location.getProvince()+location.getCity()+location.getDistrict();
                    weatherViewModel.setCityMsgEvent(nowCityMsg);
                }*/
            }
            else{
                Log.i("城市","进来了");
                String address=nowCity;
                // 通过GeoCoder的实例方法得到GerCoder对象
                GeoCoder geoCoder=GeoCoder.newInstance();//根据某个key寻找地理位置的坐标，这个key可以是地址还可以是ip地址
// 得到GenCodeOption对象
                GeoCodeOption geoCodeOption=new GeoCodeOption();//地理编码请求参数
                Log.i("成功",address);
                geoCodeOption.address(address);
                geoCodeOption.city(address); // 这里必须设置城市，否则会报错
                geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                    //将具体的地址转化为坐标
                    @Override
                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                        if (geoCodeResult==null||geoCodeResult.error!= SearchResult.ERRORNO.NO_ERROR){
                            Log.i("error","检索错误" );
                        }else {
                            // 得到具体地址的坐标
                            //LatLng是存储经纬度坐标值的类，单位角度。
                            LatLng latLng=geoCodeResult.getLocation();//使用传入的经纬度构造LatLng 对象，一对经纬度值代表地球上一个地点。
                            Log.i("经度", String.valueOf(latLng));
                            // 得到一个标记的控制器
                            //MarkerOptions设置 Marker 覆盖物的图标，相同图案的 icon 的 marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
                            MarkerOptions markerOptions = new MarkerOptions();
                            BitmapDescriptor mbitmapDescriptor = BitmapDescriptorFactory  //BitmapDescriptor用户自定义图标
                                    .fromResource(R.drawable.nowposition);
                            // 设置标记的图标
                            markerOptions.icon(mbitmapDescriptor);
                            // 设置标记的坐标
                            markerOptions.position(latLng);
                            // 添加标记
                            mBaiduMap.addOverlay(markerOptions);//意思是添加覆盖物
                            // 设置地图跳转的参数
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                                    .newLatLngZoom(latLng, 15);
                            // 设置进行地图跳转
                            mBaiduMap.setMapStatus(mMapStatusUpdate);

                        }
                    }
                    // 这个方法是将坐标转化为具体地址
                    @Override
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                    }
                });

                // 这句话必须写，否则监听事件里面的都不会执行
                geoCoder.geocode(geoCodeOption);
                weatherViewModel.setCityEvent(address);
                weatherViewModel.setCityMsgEvent(nowCityMsg);
                // 显示当前信息
                mtextView.setText(nowCityMsg);
            }
        }
    }

    @Override
    public void onResume() {
        Log.i("城市","onResume");
        flag=true;
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("城市","onPause");
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.i("城市","onDestroy");
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}