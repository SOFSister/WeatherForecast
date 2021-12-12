package com.example.weatherforecast.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WeatherHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @ColumnInfo(name = "city_name")
    public String cityName;

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    @ColumnInfo(name = "high")
    public int high;

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    @ColumnInfo(name = "low")
    public int low;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @ColumnInfo(name = "date")
    public String date;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @ColumnInfo(name = "time")
    public String time;
}
