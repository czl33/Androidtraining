package com.newczl.androidtraining1.provider;

import android.content.Context;
import android.graphics.Color;
import androidx.core.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;

import com.newczl.androidtraining1.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

/**
 * 表格的提供者
 */

public class chartProvider extends ActionProvider {
    /**
     * Creates a new instance. ActionProvider classes should always implement a
     * constructor that takes a single Context parameter for inflating from menu XML.
     */

    public interface caseClick{//接口
        void onBoomButtonClick(int index);
    }

    private caseClick caseClick;//每项的点击事件
    private int[] imgs;//图片id
    private String[] str;//标题
    private String[] str1;//描述
    private PiecePlaceEnum piecePlaceEnum;//位置
    private ButtonPlaceEnum buttonPlaceEnum;//按钮

    public chartProvider(Context context, int[] imgs, String[] str, String[] str1, com.nightonke.boommenu.Piece.PiecePlaceEnum piecePlaceEnum, ButtonPlaceEnum buttonPlaceEnum) {
        super(context);
        this.imgs = imgs;
        this.str = str;
        this.str1 = str1;
        this.piecePlaceEnum = piecePlaceEnum;
        this.buttonPlaceEnum = buttonPlaceEnum;
    }

    public void setCaseClick(chartProvider.caseClick caseClick) {
        this.caseClick = caseClick;
    }

    @Override
    public View onCreateActionView() {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.bmb_toolbar,null);
        BoomMenuButton rightBmb = view.findViewById(R.id.action_bar_right_bmb);
        rightBmb.setButtonEnum(ButtonEnum.Ham);
        rightBmb.setPiecePlaceEnum(piecePlaceEnum);
        rightBmb.setButtonPlaceEnum(buttonPlaceEnum);
        //rightBmb.setBackgroundEffect(false);
        for (int i = 0; i < rightBmb.getPiecePlaceEnum().pieceNumber(); i++){
            rightBmb.addBuilder(new HamButton.Builder()
                    .normalImageRes(imgs[i])
                    .normalText(str[i])
                    .subNormalText(str1[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                                caseClick.onBoomButtonClick(index);//回调
                        }
                    })
                    .pieceColor(Color.WHITE));
        }
        return view;
    }
}
