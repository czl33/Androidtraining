package com.newczl.androidtraining1.fragment;

import android.view.View;
import android.widget.TextView;

import com.newczl.androidtraining1.R;

public class VideoIntroFragment extends BaseFragment {
    @Override
    protected int setLayoutID() {
        return R.layout.fragment_video_intro;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        TextView textView = view.findViewById(R.id.textView);
        String videoIntro = activity.getIntent().getStringExtra("videoIntro");
        textView.setText(videoIntro);
    }
}
