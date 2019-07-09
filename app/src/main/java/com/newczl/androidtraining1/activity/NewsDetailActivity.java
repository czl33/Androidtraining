package com.newczl.androidtraining1.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnMenuItemClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.ShareDialog;
import com.newczl.androidtraining1.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class NewsDetailActivity extends BaseWebViewActivity {
    private AgentWeb mAgentWeb;//视图

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);//存入视图
        final TextView textView=findViewById(R.id.title);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogKl(1);
            }
        });
        String url=getIntent().getStringExtra("url");
        LinearLayout linearLayout=findViewById(R.id.linearLayout);//找到线性布局
        mAgentWeb=AgentWeb.with(this)
                .setAgentWebParent(linearLayout,
                        new LinearLayout.LayoutParams(-1,-1))
                .useDefaultIndicator()
                .setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);
                        textView.setText(title);
                    }
                })
                .createAgentWeb()
                .ready()
                .go(url);
    }


    @Override
    protected void menuHandle() {//菜单操作
        toolbar.inflateMenu(R.menu.webviewmenu);//实例化菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.refresh://刷新
                        //Toasty.info(NewsDetailActivity.this,"shuaxin", Toast.LENGTH_SHORT).show();
                        mAgentWeb.clearWebCache();//清楚缓存
                        mAgentWeb.getWebCreator().getWebView().reload();//刷新页面
                        break;
                    case R.id.more://更多
                        BottomMenu.show(NewsDetailActivity.this, new String[]{"重定向网址", "前进", "后退","刷新","分享"}, new OnMenuItemClickListener() {
                            @Override
                            public void onClick(String text, int index) {
                                //返回参数 text 即菜单名称，index 即菜单索引
                                switch (index){
                                    case 0:
                                        DialogKl(1);//输入网址
                                        break;
                                    case 1:
                                        mAgentWeb.getWebCreator().getWebView().goForward();
                                        break;
                                    case 2:
                                        mAgentWeb.getWebCreator().getWebView().goBack();
                                        break;
                                    case 3:
                                        mAgentWeb.clearWebCache();//清楚缓存
                                        mAgentWeb.getWebCreator().getWebView().reload();//刷新页面
                                        break;
                                    case 4:
                                        DialogKl(2);//分享
                                        break;
//                                    case 5:
//                                        DialogKl(3);//收藏
//                                        break;
                                }
                            }
                        });

//                        Toasty.info(NewsDetailActivity.this,"更多", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mAgentWeb.handleKeyEvent(keyCode,event)){
            return true;//中断掉
        }
        return super.onKeyDown(keyCode, event);
    }

    private void DialogKl(int s) {//反射代码
        switch (s){

            case 1://跳转网址
            InputDialog.show(NewsDetailActivity.this, "跳转", "输入网址", "确定", "取消")
                    .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                        @Override
                        public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                            //inputStr 即当前输入的文本
                            String url = inputStr.contains("http") ? inputStr : "https://" + inputStr;
                            mAgentWeb.getWebCreator().getWebView().loadUrl(url);//跳转网页
                            return false;
                        }
                    }).setInputText(mAgentWeb.getWebCreator().getWebView().getUrl());
            break;
            case 2://分享
                List<ShareDialog.Item> itemList = new ArrayList<>();
            itemList.add(new ShareDialog.Item(NewsDetailActivity.this ,R.mipmap.img_qq_material,"QQ"));
            itemList.add(new ShareDialog.Item(NewsDetailActivity.this ,R.mipmap.img_wechat_material,"微信"));
            itemList.add(new ShareDialog.Item(NewsDetailActivity.this ,R.mipmap.img_weibo_material,"微博"));
            itemList.add(new ShareDialog.Item(NewsDetailActivity.this ,R.mipmap.img_remind_material,"添加到“备忘录”"));
                ShareDialog.show(NewsDetailActivity.this, itemList, new ShareDialog.OnItemClickListener() {
                    @Override
                    public boolean onClick(ShareDialog shareDialog, int index, ShareDialog.Item item) {
                        switch (index){
                            case 0:
                                Toasty.success(NewsDetailActivity.this,"QQ功能待完善。", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toasty.success(NewsDetailActivity.this,"WX功能待完善。", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toasty.success(NewsDetailActivity.this,"WEIBO功能待完善。", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toasty.success(NewsDetailActivity.this,"备忘录功能待完善。", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return false;
                    }
                });
                break;
            case 3://收藏

                break;

        }

    }
}
