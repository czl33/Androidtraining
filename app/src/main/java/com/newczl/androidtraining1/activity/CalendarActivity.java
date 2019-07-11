package com.newczl.androidtraining1.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.newczl.androidtraining1.R;

public class CalendarActivity extends BaseWebViewActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        TextView textView=findViewById(R.id.title);//
        textView.setText("日历");
    }



    @Override
    protected void menuHandle() {

    }

}
