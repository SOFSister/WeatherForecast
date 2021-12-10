package com.example.weatherforecast.ui.weather.today;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.queryWeather.Forecast;
import com.example.weatherforecast.queryWeather.Weather;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TxOtherCityAdapter extends RecyclerView.Adapter<TxOtherCityAdapter.ViewHolder> {//该适配器继承字RecyclerView的Adapter适配器，因其具有可以指定
    private List<Tx> mTxList;//用以将适配完的子项储存的链表，它的泛型是之前的实体类

    static class ViewHolder extends RecyclerView.ViewHolder {
        //内部静态类，用以定义TxApter.View的泛型
        ImageView txImage;
        TextView txName;//这两个是在子项布局里面具体的控件
        TextView txMsg;//这两个是在子项布局里面具体的控件
        View txView;//这个是用于整个子项的控制的控件

        public ViewHolder(View view) {
            super(view);
            txView = view;//这个是整个子项的控件
            txImage = view.findViewById(R.id.tx_image);
            txName = view.findViewById(R.id.tx_name);//通过R文件的id查找，找出子项的具体控件
            txMsg = view.findViewById(R.id.tx_msg);//通过R文件的id查找，找出子项的具体控件
        }
    }

    public TxOtherCityAdapter(List<Tx> txList) {
        //链表的赋值
        mTxList = txList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //ViewHode方法，我的理解就是对某个具体子项的操作，包括对具体控件的设置，包括且不限于的点击动作两个参数
        //A:ViewGroup parent主要用于调用其整个RecyclerView的上下文
        //B:第二个参数因为在方法里面没有调用，所以我也没看懂，从字面上看，这个参数是一个整型的控件类型？？？
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_weather_msg, parent, false);
        //将子项的布局通过LayoutInflater引入
        final ViewHolder holder = new ViewHolder(view);
        holder.txView.setOnClickListener(new View.OnClickListener() {
            //这里是子项的点击事件，RecyclerView的特点就是可以对子项里面单个控件注册监听，这也是为什么RecyclerView要摒弃ListView的setOnItemClickListener方法的原因
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Tx tx = mTxList.get(holder.getAdapterPosition());
                //Toast.makeText(v.getContext(), tx.getName(), Toast.LENGTH_LONG).show();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                View view = View.inflate(v.getContext(), R.layout.other_city_main, null);
                List<Tx> txList = new ArrayList<>();
                txList=initTxs(TodayWeatherFragment.cityWeathers,tx.getName());
                RecyclerView recyclerView = view.findViewById(R.id.recycler);//找到RecyclerView控件
                LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());//布局管理器
                recyclerView.setLayoutManager(layoutManager);
                TxAdapter adapter = new TxAdapter(txList);//适配器对象
                recyclerView.setAdapter(adapter);//设置适配器为上面的对象
                //recyclerView.addItemDecoration(new DividerItemDecoration(v.getContext(),DividerItemDecoration.VERTICAL));
                alertDialog
                        .setTitle("详细信息 "+tx.getName())
                        .setIcon(R.drawable.ic_main)
                        .setView(view)
                        .create();
                alertDialog.show();
            }
        });
        return holder;//返回一个holder对象，给下个方法使用
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Tx> initTxs(List<Weather>cityWeathers, String aimCity){
        Forecast todayWeather=null;
        for(Weather weather:cityWeathers){
            if(weather.getData().getCity().equals(aimCity)){
                todayWeather=weather.getData().getForecast().get(0);
                break;
            }
        }
        List<Tx> txList = new ArrayList<>();
        Tx weather = new Tx("天气",todayWeather.getType(), R.drawable.weather);
        txList.add(weather);//加入到链表
        Tx temperature = new Tx("温度",todayWeather.getHigh().substring(3)+"/"+todayWeather.getLow().substring(3), R.drawable.temperature);
        txList.add(temperature);//加入到链表
        LocalDate date = LocalDate.now(); // get the current date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //Tx calendar = new Tx("日期", todayWeather.getDate(), R.drawable.calendar);
        Tx calendar = new Tx("日期", date.format(formatter), R.drawable.calendar);
        txList.add(calendar);//加入到链表
        Tx windDirection = new Tx("风向", todayWeather.getFengxiang(), R.drawable.winddirection);
        txList.add(windDirection);//加入到链表
        Tx windForce = new Tx("风速", todayWeather.getFengli().substring(9,11), R.drawable.windforce);
        txList.add(windForce);//加入到链表
        return txList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //用以将滚入屏幕的子项加载图片等的方法，两个参数
        // A:前面方法ViewHolder的对象；
        //B:子项的id
        Tx tx = mTxList.get(position);//创建前面实体类的对象
        holder.txImage.setImageResource(tx.getImageId());
        holder.txName.setText(tx.getName());//将具体值赋与子项对应的控件
        holder.txMsg.setText(tx.getMsg());//将具体值赋与子项对应的控件
    }

    @Override
    public int getItemCount() {
        //用以返回RecyclerView的总共长度，这里直接使用了链表的长度（size）
        return mTxList.size();
    }
}
