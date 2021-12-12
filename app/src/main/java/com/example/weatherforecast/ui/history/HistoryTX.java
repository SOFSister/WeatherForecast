package com.example.weatherforecast.ui.history;

public class HistoryTX {
    private String[] date;//X轴的标注

    public String[] getDate() {
        return date;
    }

    public void setDate(String[] date) {
        this.date = date;
    }

    public int[] getHigh() {
        return high;
    }

    public void setHigh(int[] high) {
        this.high = high;
    }

    public int[] getLow() {
        return low;
    }

    public void setLow(int[] low) {
        this.low = low;
    }

    private int[] high;//图表的数据点
    private int[] low;//图表的数据点

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private String cityName;
    public HistoryTX(String cityName,String[] date,int[] high,int[] low){
        this.cityName=cityName;
        this.date=date;
        this.high=high;
        this.low=low;
    }
}
