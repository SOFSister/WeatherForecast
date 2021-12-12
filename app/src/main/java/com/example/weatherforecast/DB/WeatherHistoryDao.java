package com.example.weatherforecast.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(WeatherHistory... weatherHistories);

    @Query("SELECT * FROM WeatherHistory ORDER BY date DESC,time DESC")
    List<WeatherHistory> loadAll();

    @Query("SELECT * FROM WeatherHistory WHERE city_name LIKE :cityName AND date LIKE :date")
    List<WeatherHistory> findHasHistory(String cityName,String date);

    @Query("SELECT * FROM WeatherHistory WHERE city_name LIKE :cityName ORDER BY date,time")
    List<WeatherHistory> loadHistory(String cityName);
}
