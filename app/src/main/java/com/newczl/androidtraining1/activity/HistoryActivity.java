package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.newczl.androidtraining1.DB.Bean.History;
import com.newczl.androidtraining1.DB.Bean.Star;
import com.newczl.androidtraining1.DB.starDB;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.HistoryAdapter;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.utils.PrefUtil;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;

public class HistoryActivity extends AppCompatActivity {
    protected Toolbar toolbar;//工具栏
    public boolean isShow;//是否显示过菜单
    private starDB stardb;//数据库
    private RecyclerView recyclerView;//recycleView
    private ArrayList<History> data;
    private HistoryAdapter ha;//适配器

    @Override
    protected void onStart() {
        super.onStart();
        if(!isShow) {
            toolbar = findViewById(R.id.toolbars);//找到工具栏
            toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);//设置导航按钮
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();//关闭页面
                }
            });
            isShow=true;
            toolbar.inflateMenu(R.menu.history_clear);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    MessageDialog.show(HistoryActivity.this, "清除数据", "确认清除所有历史?", "取消", "确认")
                            .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v) {
                                    //Toasty.info(HistoryActivity.this,"点了我",Toasty.LENGTH_SHORT).show();
                                    stardb.deleteHis();//删除全部
                                    ha.setNewData(new ArrayList<History>());
                                    return false;                    //位于“取消”位置的按钮点击后无法关闭对话框
                                }
                            });
                    return false;
                }
            });
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = PrefUtil.getint(this, "theme", R.style.AppTheme);//设置默认主题
        setTheme(theme);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        setContentView(R.layout.activity_history);
        stardb = new starDB(this,"star.db");//数据库
        stardb.open();//开启数据库
        TextView textView =findViewById(R.id.title);
        textView.setText("历史记录");

        recyclerView=findViewById(R.id.recyclerView);//recycleView
        data=new ArrayList<>();
        Cursor cursor = null;//根据名字查找记录
        // Cursor cursor = stardb.queryAll();//根据名字查找记录
        try {
            cursor = stardb.queryAllHs();
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String time=cursor.getString(cursor.getColumnIndex("time"));
                    data.add(new History(id,name,url,time));
                }
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }

        ha=new HistoryAdapter(R.layout.history_item,data);
        ha.openLoadAnimation();
        ha.isFirstOnly(false);
        recyclerView.setAdapter(ha);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(ha);//侧滑回调
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



        View emptyView = LayoutInflater.from(this).inflate(R.layout.emptyhisview, null);
        ha.setEmptyView(emptyView);
        ha.enableSwipeItem();//启用滑动

        ha.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    Intent intent=new Intent(HistoryActivity.this,NewsDetailActivity.class);
                    intent.putExtra("url",data.get(position).getUrl());//获取链接进行传递。
                    startActivity(intent);//跳转
            }
        });
        ha.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {
               // Log.i("dbtest", "onItemSwiped: "+data.get(i).getId());
                int s = stardb.deleteHisByID(data.get(i).getId());
                if (s==0){
                    Toasty.error(HistoryActivity.this,"删除失败！",Toasty.LENGTH_SHORT).show();//显示删除成功
                }else {
                    Toasty.success(HistoryActivity.this,"删除成功！",Toasty.LENGTH_SHORT).show();//显示删除成功
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {

            }
        });


    }

    @Override
    protected void onResume()
    {   stardb = new starDB(this,"star.db");//数据库
        stardb.open();//开启数据库
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        stardb.close();
    }

}
