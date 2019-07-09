package com.newczl.androidtraining1.activity.homeDetail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.activity.BaseWebViewActivity;
import com.newczl.androidtraining1.adapter.PythonAdapter;
import com.newczl.androidtraining1.bean.PythonBean;
import com.newczl.androidtraining1.utils.ConstantUtils;
import com.newczl.androidtraining1.utils.JsonParseUtils;
import com.newczl.androidtraining1.utils.NetUtils;

import java.util.List;

public class PythonActivity extends BaseWebViewActivity {
    private PythonAdapter pythonAdapter;//适配器

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python);
        TextView textView=findViewById(R.id.title);//标题
        textView.setText("Python学科");//存入标题
        RecyclerView recyclerView=findViewById(R.id.recyclerView);//找到recycleView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));//设置分割线
        pythonAdapter=new PythonAdapter(R.layout.item_python);//存入子布局
        recyclerView.setAdapter(pythonAdapter);//设置适配器

        getPythonData();//从服务器异步获得数据

    }

    private void getPythonData() {
        NetUtils.getDataAsyn(ConstantUtils.REQUEST_PYTHON_URL, new NetUtils.MyCallBack() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onResponse(final String json) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<PythonBean> list = JsonParseUtils.getList(PythonBean.class, json);
                        pythonAdapter.setNewData(list);//设置数据
                    }
                });
            }
        });
    }

    @Override
    protected void menuHandle() {//实例化菜单可以在这里做

    }
}
