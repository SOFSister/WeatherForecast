package com.example.weatherforecast.ui.history;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class HistoryTxAdapter extends RecyclerView.Adapter<HistoryTxAdapter.ViewHolder> {
    private List<HistoryTX> mTxList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LineChartView lineChart;
        View txView;//这个是用于整个子项的控制的控件
        public ViewHolder(@NonNull View view) {
            super(view);
            txView = view;//这个是整个子项的控件
            title = view.findViewById(R.id.title);
            lineChart = view.findViewById(R.id.line_chart);
        }
    }

    public HistoryTxAdapter(List<HistoryTX> mTxList){
        this.mTxList=mTxList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_chart_msg, parent, false);
        final HistoryTxAdapter.ViewHolder holder = new HistoryTxAdapter.ViewHolder(view);
        holder.txView.setOnClickListener(new View.OnClickListener() {
            //这里是子项的点击事件，RecyclerView的特点就是可以对子项里面单个控件注册监听，这也是为什么RecyclerView要摒弃ListView的setOnItemClickListener方法的原因
            @Override
            public void onClick(View v) {
                //Tx tx = mTxList.get(holder.getAdapterPosition());
                //Toast.makeText(v.getContext(), tx.getName(), Toast.LENGTH_LONG).show();
                //mTxList.remove(tx);//所谓的删除就是将子项从链表中remove
            }
        });
        return holder;//返回一个holder对象，给下个方法使用
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryTX tx=mTxList.get(position);
        holder.title.setText(tx.getCityName());
        LineChartView lineChart= holder.lineChart;
        String[] date = tx.getDate();//X轴的标注
        int[] high= tx.getHigh();//图表的数据点
        int[] low= tx.getLow();//图表的数据点
        List<PointValue> highValues = new ArrayList<PointValue>();
        List<PointValue> lowValues = new ArrayList<PointValue>();
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        //x轴
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
        //y轴
        for (int i = 0; i < high.length; i++) {
            highValues.add(new PointValue(i, high[i]));
            lowValues.add(new PointValue(i, low[i]));
        }
        //初始化折线图
        Line line = new Line(highValues).setColor(Color.parseColor("#e45914"));  //折线的颜色（红色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);

        Line line2 = new Line(lowValues).setColor(Color.parseColor("#00b3ff"));  //折线的颜色（蓝色）
        line2.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line2.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line2.setFilled(false);//是否填充曲线的面积
        line2.setHasLabels(true);//曲线的数据坐标是否加上备注//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line2.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line2.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line2);
        LineChartData data = new LineChartData();
        data.setLines(lines);


        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线
        Axis axisY = new Axis();  //Y轴
        axisY.setName("温度");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    @Override
    public int getItemCount() {
        return mTxList.size();
    }

}
