package com.newczl.androidtraining1.activity.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;
import com.newczl.androidtraining1.provider.chartProvider;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

public class UIPieChartActivity extends BaseWebViewActivity {
    private int[] data;//数据
    private String[] str;//名字
    private PieChart pieChart;//饼图

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_chart);//设置java图标
        str = new String[]{"Vue","Recat","Anglur",
                "ionic","uni-app"};
        data = new int[]{60,20,10,4,6};

        //找到圆
        pieChart=findViewById(R.id.pieChart);

        TextView titleView = findViewById(R.id.title);
        titleView.setText("前端分类");
        draw();//绘制

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Description de=new Description();
                de.setTextSize(16f);
                de.setText(e.getData()+ "分");
                pieChart.setDescription(de);
            }

            @Override
            public void onNothingSelected() {
                Description de=new Description();
                de.setTextSize(16f);
                de.setText("");
                pieChart.setDescription(de);
            }
        });

    }

    private void draw() {
        pieChart.setTouchEnabled(true);
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        //pieChart.setExtraOffsets(15, -20, 15, 0);//left   top  right  bottom
        pieChart.setDrawCenterText(true);
        pieChart.setHoleColor(Color.TRANSPARENT);//设置中间圆盘的颜色
        pieChart.setCenterText("前端分类");
        //pieChart.setTransparentCircleColor();
        pieChart.setCenterTextSize(16f);
        pieChart.setHoleRadius(50); //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.setTransparentCircleRadius(53); //设置中间透明圈的半径,值为所占饼图的百分比

        pieChart.setDrawCenterText(true);//饼状图中间可以添加文字

        //设置圆盘是否转动，默认转动
        pieChart.setRotationEnabled(true);
        //设置初始旋转角度
        pieChart.setRotationAngle(100);

        ArrayList<PieEntry> valueList = new ArrayList<PieEntry>();
        for (int i = 0; i < 5; i++) {
//            nameList.add("部分" + (i + 1));
            PieEntry pe=new PieEntry(data[i], str[i]);
            valueList.add(pe);
        }

        PieDataSet dataSet = new PieDataSet(valueList,"");
        //设置各个饼状图之间的距离
        dataSet.setSliceSpace(0f);
        // 部分区域被选中时多出的长度
        dataSet.setSelectionShift(20f);



        dataSet.setValueLinePart1OffsetPercentage(80f);
        //设置连接线的颜色
        dataSet.setValueLineColor(Color.BLACK);

        // 连接线在饼状图外面
        dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);

        // 设置饼块之间的间隔
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setHighlightEnabled(true);




        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(0xFF12CD92);
        colors.add(0xFFFFB667);
        colors.add(0xFFFC7E7E);
        colors.add(0xFFFFA4A4);
        colors.add(0xFAAAA4A4);
        dataSet.setColors(colors);

        PieData data2 = new PieData();
        data2.setDataSet(dataSet);
        //设置以百分比显示
        data2.setValueFormatter(new PercentFormatter(){
            @Override
            public String getFormattedValue(float value) {
                return data[(int) value]+" %";
            }
        });
        //区域文字的大小
        data2.setValueTextSize(11f);
        //设置区域文字的颜色
        data2.setValueTextColor(Color.WHITE);
        //设置区域文字的字体



        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.animateXY(1800, 1800);


        pieChart.setData(data2);
        pieChart.invalidate();



    }


    @Override
    protected void menuHandle() {
        toolbar.inflateMenu(R.menu.chart_menu);//实例化工具
        MenuItem tool = toolbar.getMenu().findItem(R.id.tool);//找到工具 int[] imgs={R.drawable.eagle,R.drawable.horse};
        int[] imgs={R.drawable.eagle,R.drawable.horse};
        String[] str={"重新绘制1","修改数据1"};
        String[] str1={"点击重新绘制","点击修改数据"};
        chartProvider cp=new chartProvider(UIPieChartActivity.this
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
//                        years[0]="我是一啊啊啊";
                        draw();//重新绘制
                        Toast.makeText(UIPieChartActivity.this, "我是一", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        MenuItemCompat.setActionProvider(tool,cp);//设置提供者
    }
}
