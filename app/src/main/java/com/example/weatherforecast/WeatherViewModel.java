package com.example.weatherforecast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WeatherViewModel extends ViewModel {
    private MutableLiveData<String> cityEvent;

    public WeatherViewModel(){
        cityEvent = new MutableLiveData<>();
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
}
