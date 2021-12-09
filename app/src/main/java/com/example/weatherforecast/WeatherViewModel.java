package com.example.weatherforecast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<String> cityEvent;
    private MutableLiveData<String> cityMsgEvent;

    public WeatherViewModel(){
        cityEvent = new MutableLiveData<>();
        cityMsgEvent = new MutableLiveData<>();
    }

    public MutableLiveData<String> getCityEvent() {
        if(cityEvent==null){
            cityEvent=new MutableLiveData<String>("杭州");
        }
        return cityEvent;
    }
    public void setCityEvent(String val){
        cityEvent.setValue(val);
    }

    public MutableLiveData<String> getCityMsgEvent() {
        if(cityMsgEvent==null){
            cityMsgEvent=new MutableLiveData<String>("浙江省杭州市余杭区");
        }
        return cityMsgEvent;
    }
    public void setCityMsgEvent(String val){
        cityMsgEvent.setValue(val);
    }
}
