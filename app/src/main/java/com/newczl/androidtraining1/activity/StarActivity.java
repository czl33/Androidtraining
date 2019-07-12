package com.newczl.androidtraining1.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.newczl.androidtraining1.DB.Bean.Star;
import com.newczl.androidtraining1.DB.starDB;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.adapter.starAdapter;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.utils.PrefUtil;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import es.dmoral.toasty.Toasty;

public class StarActivity extends AppCompatActivity {
    private starAdapter sa;//适配器
    private ArrayList<Star>  data;//数据
    private RecyclerView  recyclerView;

    protected Toolbar toolbar;//工具栏
    public boolean isShow;//是否显示过菜单
    private starDB stardb;//数据库

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
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = PrefUtil.getint(this, "theme", R.style.AppTheme);//设置默认主题
        setTheme(theme);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题栏
        setContentView(R.layout.activity_star);
        stardb = new starDB(this,"star.db");//数据库
        stardb.open();//开启数据库

        TextView textView =findViewById(R.id.title);

        User currentUser = BmobUser.getCurrentUser(User.class);
        String username = currentUser.getUsername();//获得局部变量
        textView.setText(currentUser.getNickName()+"的收藏夹");
        recyclerView=findViewById(R.id.recyclerView);//recycleView
        data=new ArrayList<>();
        Cursor cursor = null;//根据名字查找记录
       // Cursor cursor = stardb.queryAll();//根据名字查找记录
        try {
            cursor = stardb.queryByCP(username);
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String url = cursor.getString(cursor.getColumnIndex("url"));
                    String createP = cursor.getString(cursor.getColumnIndex("createP"));
                    int videoId = cursor.getInt(cursor.getColumnIndex("videoId"));
                    int type = cursor.getInt(cursor.getColumnIndex("type"));
                    data.add(new Star(id,name,url,createP,videoId,type));
                }
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }

        sa=new starAdapter(data);//数据填入
        sa.openLoadAnimation();
        sa.isFirstOnly(false);
        recyclerView.setAdapter(sa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(sa);//侧滑回调
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);



        View emptyView = LayoutInflater.from(this).inflate(R.layout.emptystarview, null);
        sa.setEmptyView(emptyView);
        sa.enableSwipeItem();//启用滑动

        sa.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(data.get(position).itemType==Star.VIDEO){
                    Toasty.info(StarActivity.this,"暂时无法跳转视频",Toasty.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(StarActivity.this,NewsDetailActivity.class);
                    intent.putExtra("url",data.get(position).getUrl());//获取链接进行传递。
                    startActivity(intent);//跳转
                }

            }
        });

        sa.setOnItemSwipeListener(new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int i) {
                Log.i("swwww", "onItemSwipeStart: ");
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int i) {
                Log.i("swwww", "clearView: "+i+"-");
                //清楚视图

            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int i) {

                //Log.i("swwww", "onItemSwiped: "+data.get(i).getName());
                int s = stardb.deleteByID(data.get(i).getId());
                if (s==0){
                    Toasty.error(StarActivity.this,"删除失败！",Toasty.LENGTH_SHORT).show();//显示删除成功
                }else {
                    Toasty.success(StarActivity.this,"删除成功！",Toasty.LENGTH_SHORT).show();//显示删除成功
                }
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float v, float v1, boolean b) {
                Log.i("swwww", "onItemSwipeMoving: ");

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
