package com.example.weatherforecast.queryWeather;

import java.io.Serializable;

public class Weather implements Serializable {
    private Data data;
    private int status;
    private String desc;
    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
    }
}
