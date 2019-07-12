package com.newczl.androidtraining1.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.kongzue.dialog.v3.WaitDialog;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.Draw;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.view.PaintView;
import com.yw.game.floatmenu.FloatItem;
import com.yw.game.floatmenu.FloatLogoMenu;
import com.yw.game.floatmenu.FloatMenuView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import es.dmoral.toasty.Toasty;

public class DrawActivity extends AppCompatActivity {
    private PaintView paintView;
    private TextView te;
    private Toolbar toolbar;
    private TextView textView;
    private FloatLogoMenu mFloatMenu;//浮动菜单
    private boolean isxiangpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_draw);
        toolbar=findViewById(R.id.toolbars);
        textView =findViewById(R.id.title);
        textView.setText("涂鸦");
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);//设置导航按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭页面
            }
        });
        initView();
        initMenu();
        toolbar.inflateMenu(R.menu.menu_draw);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });
    }

    /**
     * 初始化
     */
    private void initView() {
        paintView = (PaintView) findViewById(R.id.activity_paint_pv);
    }



    /**
     * 初始化底部菜单
     */
    private void initMenu() {

        List<FloatItem> itemList=new ArrayList<>();
        itemList.add(new FloatItem("撤销",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_chexiao)));
//        itemList.add(new FloatItem("恢复",Color.parseColor("#FFFFFF"),
//                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_beifen)));
        itemList.add(new FloatItem("清空",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_qingkong)));
        itemList.add(new FloatItem("橡皮擦",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_xiangpi)));
        itemList.add(new FloatItem("画笔",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_huabi)));
        itemList.add(new FloatItem("保存",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_save)));
        itemList.add(new FloatItem("上传",Color.parseColor("#FFFFFF"),
                Color.BLUE, BitmapFactory.decodeResource(getResources(),R.drawable.ic_upload)));
        mFloatMenu = new FloatLogoMenu.Builder()
                .withActivity(DrawActivity.this)
                .logo(BitmapFactory.decodeResource(getResources(),R.drawable.setting_ic))
                .backMenuColor(0xffe4e3e1)
                .setBgDrawable(getDrawable(R.drawable.color_drawable))
                .drawCicleMenuBg(true)
                .setFloatItems(itemList)
                .defaultLocation(FloatLogoMenu.RIGHT)
                .drawRedPointNum(false)
                .showWithListener(new FloatMenuView.OnMenuClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        //Toast.makeText(DrawActivity.this, "position " + position + " title:" + title + " is clicked.", Toast.LENGTH_SHORT).show();
                        switch (position){
                            case 0://撤销
                                paintView.undo();
                                break;
//                            case 1://恢复
//                                paintView.redo();
//                                break;
                            case 1://清空
                                paintView.clearAll();
                                break;
                            case 2://橡皮擦
                                isxiangpi=!isxiangpi;
                                paintView.setEraserModel(isxiangpi);
                                break;
                            case 3://画笔
                                showColor();
                                break;
                            case 4://保存



                                //String directoryPath;
                                //directoryPath = getFilePath(getContext(), filesize, fileName, j);
                                String path =  Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"/DCIM/";
                                  String imgName = "paint"+ SystemClock.currentThreadTimeMillis() +".jpg";
                                 if(paintView.saveImg(path,imgName)!=null) {
                                    Toasty.success(DrawActivity.this,"保存成功，路径："+path+imgName,Toasty.LENGTH_SHORT).show();
                                    }else{
                                     Toasty.error(DrawActivity.this,"保存失败，还没画东西吧").show();
                                 }

                                break;
                            case 5://上传
                                if(BmobUser.isLogin()){
                                    final Draw draw=new Draw();//文件
                                    String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"/DCIM/";
//                                    String path2 ="/1/2";
                                    String imgName2 = "paint"+ SystemClock.currentThreadTimeMillis() +".jpg";
                                    File file = paintView.saveImg(path2, imgName2);
                                    Log.i("File", "onItemClick: "+file.getAbsolutePath());
//                                    BmobFile bmobFile = null;//new File(file.getAbsolutePath())
//                                        bmobFile = new BmobFile(file.getParentFile());//file.getAbsoluteFile()

                                    final BmobFile bmobFile = new BmobFile(new File(file.getPath()));
                                    bmobFile.uploadblock(new UploadFileListener() {

                                        @Override
                                        public void done(BmobException e) {
                                            WaitDialog.dismiss();
                                            if(e==null){
                                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                                draw.setImg(bmobFile);
                                                draw.setAuthor(BmobUser.getCurrentUser(User.class));

//                                                draw.setImg(bmobFile);
//                                                draw.setAuthor(BmobUser.getCurrentUser(User.class));
                                                draw.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {
                                                        if (e == null) {
                                                            Toasty.success(DrawActivity.this,"成功上传。",Toasty.LENGTH_LONG).show();
                                                        } else {
                                                            //Log.e("BMOB", e.toString());
                                                            Toasty.error(DrawActivity.this, e.getMessage(),Toasty.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });

                                            }else{
                                                Toasty.error(DrawActivity.this, e.getMessage(),Toasty.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onProgress(Integer value) {
                                            // 返回的上传进度（百分比）
                                            WaitDialog.show(DrawActivity.this, "上传中..."+value+"%");
                                        }
                                    });
                                    //bmobFile.getFileUrl();
//                                    draw.setImg(bmobFile);
//                                    draw.setAuthor(BmobUser.getCurrentUser(User.class));
//                                    draw.save(new SaveListener<String>() {
////                                        @Override
////                                        public void done(String s, BmobException e) {
////                                            if (e == null) {
////                                                Toasty.success(DrawActivity.this,"成功上传。",Toasty.LENGTH_LONG).show();
////                                            } else {
////                                                //Log.e("BMOB", e.toString());
////                                                Toasty.error(DrawActivity.this, e.getMessage(),Toasty.LENGTH_LONG).show();
////                                            }
////                                        }
//
//                                    });

                                }else{
                                    Toasty.error(DrawActivity.this,"你还没有登录。无法上传至你的画册。").show();
                                }



                                break;
                        }
                    }

                    @Override
                    public void dismiss() {

                    }
                });

//        //撤销
//        menuItemSelected(R.id.activity_paint_undo, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//               // ToastUtils.show(PaintViewActivity.this, "撤销");
//                paintView.undo();
//            }
//        });
//        //恢复
//        menuItemSelected(R.id.activity_paint_redo, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//              //  ToastUtils.show(PaintViewActivity.this, "恢复");
//                paintView.redo();
//            }
//        });
//
//        //颜色
//        menuItemSelected(R.id.activity_paint_color, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//              //  ToastUtils.show(PaintViewActivity.this, "红色");
//                paintView.setPaintColor(Color.RED);
//            }
//        });
//        //清空
//        menuItemSelected(R.id.activity_paint_clear, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//                //ToastUtils.show(PaintViewActivity.this, "清空");
//                paintView.clearAll();
//            }
//        });
//
//        //橡皮擦
//        menuItemSelected(R.id.activity_paint_eraser, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//               // ToastUtils.show(PaintViewActivity.this, "橡皮擦");
//                paintView.setEraserModel(true);
//            }
//        });
//
//        //保存
//        menuItemSelected(R.id.activity_paint_save, new MenuSelectedListener() {
//            @Override
//            public void onMenuSelected() {
//                String path = Environment.getExternalStorageDirectory().getPath()
//                        + File.separator;
//                String imgName = "paint.jpg";
//                if (paintView.saveImg(path,imgName)) {
//                   // ToastUtils.show(PaintViewActivity.this, "保存成功");
//                }
//            }
//        });
    }

    /**
     * 选中底部 Menu 菜单项
     */
    public void showColor(){
        ColorPickerDialogBuilder
                .with(DrawActivity.this)
                .setTitle("颜色选择")
                .initialColor(paintView.getMycolor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //Toasty.success(DrawActivity.this,"onColorSelected: 0x" + Integer.toHexString(selectedColor)).show();
                    }
                })
                .setPositiveButton("确认", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        paintView.setPaintColor(selectedColor);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .build()
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // ToastUtils.cancel();
    }

}
