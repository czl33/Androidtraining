package com.newczl.androidtraining1.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Toast;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.chart.CCandleStickChartActivity;
import com.newczl.androidtraining1.activity.chart.IOSChartActivity;
import com.newczl.androidtraining1.activity.chart.JavaLineChartActivity;
import com.newczl.androidtraining1.activity.chart.PythonChartActivity;
import com.newczl.androidtraining1.activity.chart.UIPieChartActivity;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import es.dmoral.toasty.Toasty;

/**
 * 图表页面的Fragment
 * author:czl
 */
public class ChartFragment extends BaseFragment {
    @Override
    protected int setLayoutID() {//添加视图
        return R.layout.fragment_chart;
    }
    @Override
    protected void initData() {//初始化数据
        super.initData();
    }

    @Override
    protected void initView(View view) {//初始化视图
        super.initView(view);
//        int[] colos={Color.rgb(67,147,255)
//        };//添加颜色
        int[] imagse_id={R.drawable.bat,R.drawable.bear,R.drawable.bee,R.drawable.butterfly,
                R.drawable.cat,R.drawable.dolphin,R.drawable.elephant,R.drawable.horse,
                R.drawable.eagle};//数据
        String[] texts={"Android","Java","PHP","Python","C/C++","IOS","前端","UI","网络"};//文字
        BoomMenuButton bmb=view.findViewById(R.id.bmb);//找到爆炸菜单
        for (int i = 0; i <bmb.getPiecePlaceEnum().pieceNumber() ; i++) {
            TextInsideCircleButton.Builder builder=new TextInsideCircleButton.Builder()
                    .normalImageRes(imagse_id[i])
                    .normalText(texts[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            //Toasty.info(activity,"我是"+index,Toast.LENGTH_SHORT).show();
                            switch (index){
                                case 0://android
                                    break;
                                case 1://Java
                                    Intent intent1=new Intent(activity,JavaLineChartActivity.class);//Java
                                    activity.startActivity(intent1);
                                    break;
                                case 2://PHP

                                    break;
                                case 3://Python
                                    Intent intent3=new Intent(activity, PythonChartActivity.class);//Python
                                    activity.startActivity(intent3);
                                    break;
                                case 4://C/C++
                                    Intent intent4=new Intent(activity, CCandleStickChartActivity.class);//C
                                    activity.startActivity(intent4);
                                    break;
                                case 5://IOS
                                    Intent intent5=new Intent(activity, IOSChartActivity.class);//IOS
                                    activity.startActivity(intent5);
                                    break;
                                case 6://UI
                                    Intent intent6=new Intent(activity, UIPieChartActivity.class);//Ui
                                    activity.startActivity(intent6);
                                    break;

                            }
                        }
                    });
            bmb.addBuilder(builder);//构建
            
        }
    }


}
