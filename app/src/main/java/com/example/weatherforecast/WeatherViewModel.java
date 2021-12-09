package com.example.weatherforecast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherforecast.DB.InterestedCity;

import java.util.List;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<String> cityEvent;
    private MutableLiveData<String> cityMsgEvent;
    private MutableLiveData<List<InterestedCity>> interestedCity;

    public WeatherViewModel(){
        cityEvent = new MutableLiveData<>();
        cityMsgEvent = new MutableLiveData<>();
        interestedCity = new MutableLiveData<>();
    }
    //--------------------------------------------------------------------------
    public MutableLiveData<String> getCityEvent() {
        if(cityEvent==null){
            cityEvent=new MutableLiveData<String>("杭州");
        }
        return cityEvent;
    }
    public void setCityEvent(String val){
        cityEvent.setValue(val);
    }
    //--------------------------------------------------------------------------
    public MutableLiveData<String> getCityMsgEvent() {
        if(cityMsgEvent==null){
            cityMsgEvent=new MutableLiveData<String>("浙江省杭州市余杭区");
        }
        return cityMsgEvent;
    }
    public void setCityMsgEvent(String val){
        cityMsgEvent.setValue(val);
    }
    //--------------------------------------------------------------------------
    public MutableLiveData<List<InterestedCity>> getInterestedCity() {
        if(interestedCity==null){
            interestedCity=new MutableLiveData<List<InterestedCity>>();
        }
        return interestedCity;
    }
    public void setInterestedCity(List<InterestedCity> val){
        interestedCity.setValue(val);
    }
    //--------------------------------------------------------------------------
}
