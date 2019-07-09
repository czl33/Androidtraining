package com.newczl.androidtraining1.activity.chart;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;
import com.newczl.androidtraining1.provider.chartProvider;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

public class IOSChartActivity extends BaseWebViewActivity {
    private BubbleChart mBubbleChart ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ios_chart);//设置java图标
        mBubbleChart =findViewById(R.id.chart);//找到图
        TextView title=findViewById(R.id.title);
//        Description desc = new Description();
//        desc.setText("Java工程师工作经验对应的薪资情况");
//        desc.setPosition(500,20);
//        desc.setTextAlign(Paint.Align.CENTER);
//        desc.setTextSize(16f);
//        mBubbleChart.setDescription(desc);
        title.setText("气泡图");
        draw();
    }

    private void draw() {

        mBubbleChart.getDescription().setEnabled(false);
        //mBubbleChart.setOnChartValueSelectedListener(this);
        mBubbleChart.setDrawGridBackground(false);
        mBubbleChart.setTouchEnabled(true);
        mBubbleChart.setDragEnabled(true);
        mBubbleChart.setScaleEnabled(true);
        mBubbleChart.setMaxVisibleValueCount(200);
        mBubbleChart.setPinchZoom(true);

        Legend l = mBubbleChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);

        l.setDrawInside(false);
        YAxis yl = mBubbleChart.getAxisLeft();
        yl.setSpaceTop(30f);
        yl.setSpaceBottom(30f);
        yl.setDrawZeroLine(false);
        mBubbleChart.getAxisRight().setEnabled(false);
        XAxis xl = mBubbleChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        setData();
    }

    private void setData() {
        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();
        ArrayList<BubbleEntry> yVals2 = new ArrayList<BubbleEntry>();
        ArrayList<BubbleEntry> yVals3 = new ArrayList<BubbleEntry>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 30);
            float size = (float) (Math.random() * 40);
            yVals1.add(new BubbleEntry(i, val, size));
        }

        for (int i = 0; i < 20; i++) {
            float val = (float) (Math.random() * 40);
            float size = (float) (Math.random() * 50);
            yVals2.add(new BubbleEntry(i, val, size));
        }

        for (int i = 0; i < 30; i++) {
            float val = (float) (Math.random() * 50);
            float size = (float) (Math.random() * 60);
            yVals3.add(new BubbleEntry(i, val, size));
        }

        BubbleDataSet set1 = new BubbleDataSet(yVals1, "JAVA");
        //可以谁知alpha
        set1.setColor(Color.parseColor("#00BFFF"));
        set1.setDrawValues(true);
        BubbleDataSet set2 = new BubbleDataSet(yVals2, "Python");
        set2.setColor(Color.parseColor("#40E0D0"));
        set2.setDrawValues(true);
        BubbleDataSet set3 = new BubbleDataSet(yVals3, "Android");
        set3.setColor(Color.parseColor("#00FA9A"));
        set3.setDrawValues(true);

        ArrayList<IBubbleDataSet> dataSets = new ArrayList<IBubbleDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);

        BubbleData data = new BubbleData(dataSets);
        data.setDrawValues(false);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        data.setHighlightCircleWidth(20f);

        mBubbleChart.setData(data);
        mBubbleChart.invalidate();

        //默认动画
        mBubbleChart.animateXY(3000, 3000);
    }

    @Override
    protected void menuHandle() {        toolbar.inflateMenu(R.menu.chart_menu);//实例化工具
        MenuItem tool = toolbar.getMenu().findItem(R.id.tool);//找到工具
        int[] imgs={R.drawable.eagle,R.drawable.horse};
        String[] str={"显示顶点值","修改数据"};
        String[] str1={"点击显示顶点值","点击修改数据"};
        chartProvider cp=new chartProvider(IOSChartActivity.this
                ,imgs,str,str1, PiecePlaceEnum.HAM_2, ButtonPlaceEnum.HAM_2);
        cp.setCaseClick(new chartProvider.caseClick() {
            @Override
            public void onBoomButtonClick(int index) {//按钮的点击事件
                switch (index){
                    case 0://做出修改
                        //draw();//重新绘制
                        //Toast.makeText(JavaLineChartActivity.this, "我是一", Toast.LENGTH_SHORT).show();
                        for (IDataSet set : mBubbleChart.getData().getDataSets())
                            set.setDrawValues(!set.isDrawValuesEnabled());
                        mBubbleChart.invalidate();
                        break;
                    case 1://做出修改
                        //years[0]="我是一啊啊啊";
                        draw();//重新绘制
                        Toast.makeText(IOSChartActivity.this, "我是一", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        MenuItemCompat.setActionProvider(tool,cp);//设置提供者

    }
}
