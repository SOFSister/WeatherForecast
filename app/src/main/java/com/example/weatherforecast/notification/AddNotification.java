package com.example.weatherforecast.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import com.example.weatherforecast.R;

public class AddNotification {
    public static void showCityChanged(Resources res,Context context,String title,String content){
        Notification notification;
        Notification.Builder builder;
        //创建一个通知管理器
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder=new Notification.Builder(context,"5996773");
        }else {
            builder=new Notification.Builder(context);
        }
        //设置标题
        builder.setContentTitle(title);
        //设置内容
        builder.setContentText(content);
        //设置状态栏显示的图标，建议图标颜色透明
        builder.setSmallIcon(R.drawable.logo);
        // 设置通知灯光（LIGHTS）、铃声（SOUND）、震动（VIBRATE）、（ALL 表示都设置）
        builder.setDefaults(Notification.DEFAULT_ALL);
        //灯光三个参数，颜色（argb）、亮时间（毫秒）、暗时间（毫秒）,灯光与设备有关
        builder.setLights(Color.RED, 200, 200);
        // 铃声,传入铃声的 Uri（可以本地或网上）我这没有铃声就不传了
        builder.setSound(Uri.parse("")) ;
        // 震动，传入一个 long 型数组，表示 停、震、停、震 ... （毫秒）
        builder.setVibrate(new long[]{0, 200, 200, 200, 200, 200});
        // 通知栏点击后自动消失
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);

        //设置下拉之后显示的图片
        builder.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nowposition));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("5996773", "安卓10a", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);//是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN);//小红点颜色
            channel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }
        notification=builder.build();
        notificationManager.notify(1,notification);
    }
}
