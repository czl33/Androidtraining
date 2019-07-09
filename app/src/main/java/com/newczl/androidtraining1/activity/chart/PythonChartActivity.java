package com.newczl.androidtraining1.activity.chart;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;
import com.newczl.androidtraining1.provider.chartProvider;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PythonChartActivity extends BaseWebViewActivity {

    private RadarChart chart;//python图标

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python_chart);//设置java图标
        chart=findViewById(R.id.radarChart);//找到图
        TextView title=findViewById(R.id.title);
        title.setText("雷达图");
        draw();




    }

    private void draw() {
        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.RED);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.GRAY);
        //chart.setWebAlpha(0);
        //chart.setRotationEnabled(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                ArrayList<String> as=new ArrayList<>();
                as.add("上手");
                as.add("速度");
                as.add("能力");
                as.add("跨平台");
                as.add("可移植");
                return as.get((int) value);
            }
        });



        xAxis.setTextColor(Color.parseColor("#1E90FF") );

        YAxis yAxis = chart.getYAxis();
        yAxis.setLabelCount(1, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(true);


        Legend l = chart.getLegend();//得到图例
        //Legend l = mBubbleChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);


        setData();
        chart.animateXY(//设置绘制动画
                1400, 1400);
    }

    private void setData() {
        float mult = 110;
        float min = 20;
        //项目数
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mult) + min;
            entries2.add(new RadarEntry(val2));
        }
        //set1为一级图层、set2为二级图层
        RadarDataSet set1 = new RadarDataSet(entries1, "Android");
        set1.setColor(Color.parseColor("#FFD700"));
        set1.setFillColor(Color.parseColor("#FFD700"));//一级图层填充色
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "IOS");
        set2.setColor(Color.parseColor("#40E0D0"));
        set2.setFillColor(Color.parseColor("#40E0D0"));//二级图层填充色
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set2);
        sets.add(set1);

        RadarData data = new RadarData(sets);
//        data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        //data.setDrawValues(false);
        data.setValueTextColor(Color.parseColor("#FF8C00"));

        data.setDrawValues(true);

        chart.setData(data);

        chart.invalidate();
    }

    @Override
    protected void menuHandle() {
        toolbar.inflateMenu(R.menu.chart_menu);//实例化工具
        MenuItem tool = toolbar.getMenu().findItem(R.id.tool);//找到工具
        int[] imgs={R.drawable.eagle,R.drawable.horse};
        String[] str={"重新绘制","修改数据"};
        String[] str1={"点击重新绘制","点击修改数据"};
        chartProvider cp=new chartProvider(PythonChartActivity.this
                ,imgs,str,str1, PiecePlaceEnum.HAM_2, ButtonPlaceEnum.HAM_2);
        cp.setCaseClick(new chartProvider.caseClick() {
            @Override
            public void onBoomButtonClick(int index) {//按钮的点击事件
                switch (index){
                    case 0://做出修改
                        draw();//重新绘制
                        //Toast.makeText(JavaLineChartActivity.this, "我是一", Toast.LENGTH_SHORT).show();
                        break;
                    case 1://做出修改
                        draw();//重新绘制
                        Toasty.info(PythonChartActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        MenuItemCompat.setActionProvider(tool,cp);//设置提供者
    }
}
