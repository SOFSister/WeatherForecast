package com.example.weatherforecast.ui.weather.today;

public class Tx {
    private String name;
    private String msg;
    private int imageId;//两个成员变量


    public Tx(String name,String msg,int imageId) {//构造方法，用以赋值
        this.name = name;
        this.msg=msg;
        this.imageId = imageId;//赋予变量值
    }

    public String getName() {//获得Name的值
        return name;
    }
    public String getMsg() {//获得msg的值
        return msg;
    }

    public int getImageId() {//用以获得图片ID的值
        return imageId;
    }
}


