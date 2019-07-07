package com.newczl.androidtraining1.activity.chart;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;
import com.newczl.androidtraining1.provider.chartProvider;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * java图标
 */
public class JavaLineChartActivity extends BaseWebViewActivity {

    private int[] salaries;//数据
    private String[] years;//年份数据

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_chart);//设置java图标
        years = new String[]{"应届生","1-2年","2-3年",
                "3-5年","5-8年","8-10年","10年"};
        salaries = new int[]{6000,13000,20000,35000,50000,80000,100000};
        TextView titleView = findViewById(R.id.title);
        titleView.setText("Java统计");
        draw();//绘制

    }

    private void draw() {//开始画画
        LineChart chart = findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < salaries.length; i++) {
            entries.add(new Entry(i,salaries[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries,"工资");
        //线条颜色
        dataSet.setColor(Color.parseColor("#39afff"));
        //圆点颜色
        dataSet.setCircleColor(Color.parseColor("#39afff"));
        //线条宽度
        dataSet.setLineWidth(3f);
        // 模式为贝塞尔曲线
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//模式为曲线

        dataSet.setValueTextColor(Color.RED);
        dataSet.setValueTextSize(12f);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineWidth(2f);
        xAxis.enableGridDashedLine(10f,10f,0f);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return years[(int)value];
            }
        });
        //设置图表右边的y轴禁用
        chart.getAxisRight().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setAxisLineWidth(2f);
        yAxis.enableGridDashedLine(10f,10f,0f);
        yAxis.setSpaceTop(20f);

        LimitLine limitLine = new LimitLine(15000f,"平均工资");
        limitLine.setLineColor(Color.parseColor("#7d7d7d"));
        limitLine.setLineWidth(2f);
        yAxis.addLimitLine(limitLine);
        Description desc = new Description();
        desc.setText("Java工程师工作经验对应的薪资情况");
        desc.setPosition(500,50);
        desc.setTextAlign(Paint.Align.CENTER);
        desc.setTextSize(16f);
        chart.setDescription(desc);
        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        // 设置是否可以缩放图表
        chart.setScaleEnabled(true);
        // 设置是否可以用手指移动图表
        chart.setDragEnabled(true);
        Matrix matrix = new Matrix();
        // x轴放大1.2倍，y不变
        matrix.postScale(1.2f, 1.0f);
        // 在图表动画显示之前进行缩放
        chart.getViewPortHandler().refresh(matrix, chart, false);
        //动画
//        chart.animateX(2000);
//        chart.animateX(2000);
//        chart.animateY(2000);
        chart.animateXY(2000,2000);

        chart.invalidate();
    }

    @Override
    protected void menuHandle() {//左上角菜单选项
        toolbar.inflateMenu(R.menu.chart_menu);//实例化工具
        MenuItem tool = toolbar.getMenu().findItem(R.id.tool);//找到工具
        int[] imgs={R.drawable.eagle,R.drawable.horse};
        String[] str={"重新绘制","修改数据"};
        String[] str1={"点击重新绘制","点击修改数据"};
        chartProvider cp=new chartProvider(JavaLineChartActivity.this
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
                        years[0]="我是一啊啊啊";
                        draw();//重新绘制
                        Toast.makeText(JavaLineChartActivity.this, "我是一", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        MenuItemCompat.setActionProvider(tool,cp);//设置提供者
    }


}
