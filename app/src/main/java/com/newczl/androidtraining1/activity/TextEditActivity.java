package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.newczl.androidtraining1.R;

import es.dmoral.toasty.Toasty;

public class TextEditActivity extends BaseWebViewActivity {
    private EditText editText;
    private TextView textView;//文本

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_edit);//绑定布局
        TextView titleView=findViewById(R.id.title);
        titleView.setText("修改");
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String value=intent.getStringExtra("value");
        textView=findViewById(R.id.textView);
        textView.setText(name);;
        editText=findViewById(R.id.editText);
        editText.setText(value);
    }

    @Override
    protected void menuHandle() {

    }

    public void save(View view) {
        if(textView.getText().toString().equals("性别")){

            String value=editText.getText().toString();
            String fina;
            Intent intent=new Intent();
            if(value.equals("男")){
                fina="男";
            }else if(value.equals("女")){
                fina="女";
            }else{
                Toasty.info(TextEditActivity.this,"请填写‘男’或‘女’！",Toasty.LENGTH_SHORT).show();
                editText.setText("男");
                return;
            }
            intent.putExtra("value",fina);
            setResult(RESULT_OK,intent);
            finish();
        }else{
            String value=editText.getText().toString();
            Intent intent=new Intent();
            intent.putExtra("value",value);
            setResult(RESULT_OK,intent);
            finish();

        }

    }
}
