package com.example.weatherforecast.DB;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InterestedCity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getInterestedCityName() {
        return interestedCityName;
    }

    public void setInterestedCityName(String interestedCityName) {
        this.interestedCityName = interestedCityName;
    }

    @ColumnInfo(name = "interested_city_name")
    public String interestedCityName;

}
