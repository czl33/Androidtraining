package com.newczl.androidtraining1.activity.chart;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class CCandleStickChartActivity extends BaseWebViewActivity {
    private CandleStickChart  mChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_chart);//设置java图标
        mChart=findViewById(R.id.candleStickChart);//找到图
        TextView title=findViewById(R.id.title);//
        title.setText("烛状图");
        draw();
    }

    private void draw() {

        //mChart.setDescription("");// 数据描述
        mChart.setDrawGridBackground(false); // 是否显示表格颜色  
        //mChart.setBackgroundColor(color);// 设置背景
        //mChart.setGridBackgroundColor(color);//设置表格背景色
        mChart.setTouchEnabled(true); // enable touch gestures 
        mChart.setDragEnabled(true);// 是否可以拖拽  
        mChart.setScaleEnabled(true);// 是否可以缩放  
        mChart.setPinchZoom(true);// if disabled, scaling can be done on x- and y-axis separately
        mChart.setScaleYEnabled(true);// if disabled, scaling can be done on x-axis
        mChart.setScaleXEnabled(true);// if disabled, scaling can be done on  y-axis

        Legend l = mChart.getLegend();  // 设置比例图标示
        l.setForm(Legend.LegendForm.SQUARE);// 样式
        l.setFormSize(6f);// 字号
        l.setTextColor(Color.WHITE);// 颜色

        List<String> labels=new ArrayList<>();
        labels.add("红涨");
        labels.add("绿跌");
        List<Integer> colors=new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        l.setEnabled(false);//决定显不显示标签

        List<CandleEntry> yVals1=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CandleEntry ce=null;
            if(i%2!=0){
                ce=new CandleEntry(i,0.2f,0.06f,0.08f,0.03f);
            }else{
                ce=new CandleEntry(i,0.2f,0.02f,0.06f,0.1f);
            }

            //CandleEntry ce=new CandleEntry(0f+i,0.0f,0.0f,0.0f,0.0f);
            yVals1.add(ce);
        }
        ArrayList<ICandleDataSet> iCandleDataSets = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            CandleDataSet set = new CandleDataSet(yVals1,"");
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setShadowColor(Color.DKGRAY);//影线颜色
            set.setShadowColorSameAsCandle(true);//影线颜色与实体一致
            set.setShadowWidth(0.7f);//影线

                set.setDecreasingColor(Color.RED);
                set.setDecreasingPaintStyle(Paint.Style.FILL);//红涨，实体

                set.setIncreasingColor(Color.GREEN);
                set.setIncreasingPaintStyle(Paint.Style.STROKE);//绿跌，空心

            set.setNeutralColor(Color.RED);//当天价格不涨不跌（一字线）颜色

            set.setHighlightLineWidth(1f);//选中蜡烛时的线宽
            set.setDrawValues(false);//在图表中的元素上面是否显示数值
            iCandleDataSets.add(set);
        }

        XAxis xAxis =mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        //xAxis.setSpaceBetweenLabels(12);//轴刻度间的宽度，默认值是4
//        xAxis.setGridColor(colorLine);//X轴刻度线颜色
//        xAxis.setTextColor(colorText);//X轴文字颜色

        CandleData data = new CandleData(iCandleDataSets);
// animate calls invalidate()...

        mChart.setData(data);
        mChart.animateXY(2500,2000); // 立即执行的动画,x轴
    }
    @Override
    protected void menuHandle() {

    }
}
