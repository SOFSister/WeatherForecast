package com.example.weatherforecast.queryWeather;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.weatherforecast.R;
import com.example.weatherforecast.ui.weather.today.TodayWeatherFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryWeather {
    public static Weather handleWeatherResponse(String response){
        try{
            return new Gson().fromJson(response,Weather.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static void queryWeather(String city){
        Log.d("debug666","111");
        final Weather[] cityWeather = new Weather[1];
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS).build();
        Request request=new Request.Builder()
                .url("http://wthrcdn.etouch.cn/weather_mini?city="+city)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("https","fail");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String jsonStr=response.body().string();
                cityWeather[0] =handleWeatherResponse(jsonStr);
                Message msg=Message.obtain();
                msg.what=1;
                // 实例化一个Bundle
                Bundle bundle = new Bundle();
                // 把Persion数据放入到bundle中
                bundle.putSerializable("weather", cityWeather[0]);
                msg.setData(bundle);
                TodayWeatherFragment.handler.sendMessage(msg);
            }
        });
    }
}
