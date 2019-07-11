package com.newczl.androidtraining1.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.newczl.androidtraining1.R;
import com.newczl.androidtraining1.bean.User;
import com.newczl.androidtraining1.utils.ImgUtils;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.PickerManager;
import es.dmoral.toasty.Toasty;

/**
 * 设置个人资料
 */
public class UserInfoActivity extends BaseWebViewActivity {


    private static final int REQUEST_PERMISSION_CS = 0x1011;//获得照相机和本地存储权限
    private static final int REQUEST_CODE_TAKE_PHOTO =0x1012 ;//照片的返回码
    private static final int REQUEST_CODE_EDIT_NICKNAME =0x1013 ;//修改昵称
    private static final int REQUEST_CODE_EDIT_SEX = 0x1014;
    private static final int REQUEST_CODE_EDIT_EMAIL =0x1015 ;
    private static final int REQUEST_CODE_EDIT_INFO = 0x1016;


    private CircleImageView circleImageView;//圆角头像
    private TextView textView_userName;//用户名
    private TextView textView_nickname;//昵称
    private TextView textView_sex;//性别
    private TextView textView_email;//邮箱
    private TextView textView_info;//签名



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);//绑定布局
        TextView title=findViewById(R.id.title);//绑定标题
        title.setText("个人资料");
        circleImageView = findViewById(R.id.circleImageView);//圆角头像视图
        textView_userName=findViewById(R.id.textView_userName);//用户名
        textView_nickname=findViewById(R.id.textView_nickname);//昵称
        textView_sex=findViewById(R.id.textView_sex);//性别
        textView_email=findViewById(R.id.textView_email);//邮箱
        textView_info=findViewById(R.id.textView_info);//签名
        if(BmobUser.isLogin()){
            User user=BmobUser.getCurrentUser(User.class);//得到当前登录的用户
            textView_userName.setText(user.getUsername());//存入用户名
            textView_nickname.setText(user.getNickName());//存入昵称
            textView_sex.setText(user.getSex()?"男":"女");//存入性别
            textView_email.setText(user.getEmail());//存入邮箱
            textView_info.setText(user.getInfo());//存入个人签名

            ImgUtils.setImage(this,user.getHeadImage().getUrl(),circleImageView);
        }
    }




    public void myClick(View view) {//点击事件
        switch (view.getId()){
            case R.id.linearLayout_head:
                if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED  //是否得到照相机
                &&checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED//是否得到外部存储权限
                ){
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_CS);
                }else{
                    takePhoto();//设置照片
                }
                break;
            case R.id.linearLayout_password://密码
                modifyPass();//修改密码
                break;
            case R.id.linearLayout_nickname://昵称
                textEdit("昵称",textView_nickname.getText().toString(),REQUEST_CODE_EDIT_NICKNAME);
                break;
            case R.id.linearLayout_sex://性别
                textEdit("性别",textView_sex.getText().toString(),REQUEST_CODE_EDIT_SEX);
                break;
            case R.id.linearLayout_email://邮箱
                textEdit("邮箱",textView_email.getText().toString(),REQUEST_CODE_EDIT_EMAIL);
                break;
            case R.id.linearLayout_info://信息
                textEdit("信息",textView_info.getText().toString(),REQUEST_CODE_EDIT_INFO);
                break;

        }
    }

    private void modifyPass() {//修改密码并进行校验
        InputDialog.show(UserInfoActivity.this, "提示", "请输入原密码", "确定", "取消")
                .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v, final String inputStr1) {
                        //inputStr 即当前输入的文本
                        InputDialog.show(UserInfoActivity.this, "提示", "请输入新密码", "确定", "取消")
                                .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v, String inputStr2) {
                                        //inputStr 即当前输入的文本
                                        BmobUser.updateCurrentUserPassword(inputStr1, inputStr2, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    TipDialog.show(UserInfoActivity.this, "修改密码成功，下次登录请使用新密码！", TipDialog.TYPE.SUCCESS)
                                                            .setTipTime(2000);
                                                } else {
                                                    TipDialog.show(UserInfoActivity.this, "出现错误，请联系作者！错误："+e.getMessage(), TipDialog.TYPE.ERROR)
                                                            .setTipTime(3000);
                                                }
                                            }
                                        });
                                        return false;
                                    }
                                });
                        return false;
                    }
                });
    }

    private void textEdit(String name, String toString, int requestCodeEditNickname) {
        Intent intent=new Intent(this,TextEditActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("value",toString);
        startActivityForResult(intent,requestCodeEditNickname);
    }


    private void takePhoto() {//选择照片
        ArrayList<String> filePath=new ArrayList<>();
        filePath.add(FilePickerConst.KEY_SELECTED_MEDIA);//
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(filePath)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(this,REQUEST_CODE_TAKE_PHOTO);//存入照片的返回码
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_CS){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                //都是得到状态
                takePhoto();//执行选择照片
            }else{
                Toasty.error(UserInfoActivity.this,"你拒绝了权限，请手动到应用中开启权限",Toasty.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//所有的返回码请求操作在这
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            if(requestCode==REQUEST_CODE_TAKE_PHOTO){//找照片
                String s= PickerManager.INSTANCE.getSelectedPhotos().get(0);
                ImgUtils.setImage(this,s,circleImageView);//直接绑定至圆角图片上
                final BmobFile bmobFile=new BmobFile(new File(s));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        WaitDialog.dismiss();//使上传框失效
                        if(e==null){
                            bmobFile.getFileUrl();
                            User user=BmobUser.getCurrentUser(User.class);
                            user.setHeadImage(bmobFile);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        TipDialog.show(UserInfoActivity.this, "信息同步成功！", TipDialog.TYPE.SUCCESS)
                                                .setTipTime(1000);
                                    }else{
                                        TipDialog.show(UserInfoActivity.this, "同步失败，请联系作者！错误："+e.getMessage(), TipDialog.TYPE.ERROR)
                                                .setTipTime(3000);
                                    }
                                }
                            });


                        }else{
                            TipDialog.show(UserInfoActivity.this, "文件上传失败，请联系作者！", TipDialog.TYPE.ERROR)
                                    .setTipTime(3000);

                        }
                    }

                    @Override
                    public void onProgress(Integer value) {
                        super.onProgress(value);
                        WaitDialog.show(UserInfoActivity.this, "同步中..."+value+"%");
                    }
                });//上传回调

            }else{//其他的操作
                String value=data.getStringExtra("value");//得到数据
                User user=BmobUser.getCurrentUser(User.class);
                if(requestCode==REQUEST_CODE_EDIT_NICKNAME){
                    textView_nickname.setText(value);//设置文本
                    user.setNickName(value);//设置昵称
                }
                if(requestCode==REQUEST_CODE_EDIT_SEX){
                    if(value.equals("男")){
                        textView_sex.setText(value);
                        user.setSex(true);
                    }else{
                        textView_sex.setText("女");
                        user.setSex(false);
                    }
                }
                if(requestCode==REQUEST_CODE_EDIT_EMAIL){
                    textView_email.setText(value);
                    user.setEmail(value);
                }
                if(requestCode==REQUEST_CODE_EDIT_INFO){
                    textView_info.setText(value);
                    user.setInfo(value);
                }
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            TipDialog.show(UserInfoActivity.this, "信息同步成功！", TipDialog.TYPE.SUCCESS)
                                    .setTipTime(1000);
                        }else{
                            TipDialog.show(UserInfoActivity.this, "同步失败，请联系作者！错误："+e.getMessage(), TipDialog.TYPE.ERROR)
                                    .setTipTime(3000);
                        }

                    }
                });




            }

        }

    }

    @Override
    protected void menuHandle() {//实例化菜单

    }
}
