package com.example.weatherforecast.queryWeather;

import java.io.Serializable;
import java.util.List;

public class Weathers implements Serializable {
    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    private List<Weather>weathers;
    public Weathers(List<Weather>weathers){
        this.weathers=weathers;
    }
}
