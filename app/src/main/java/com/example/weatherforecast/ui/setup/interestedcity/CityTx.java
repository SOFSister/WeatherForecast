package com.example.weatherforecast.ui.setup.interestedcity;

public class CityTx {
    private String name;
    private int imageId;//两个成员变量


    public CityTx(String name,int imageId) {//构造方法，用以赋值
        this.name = name;
        this.imageId = imageId;//赋予变量值
    }

    public String getName() {//获得Name的值
        return name;
    }
    public int getImageId() {//用以获得图片ID的值
        return imageId;
    }
}
