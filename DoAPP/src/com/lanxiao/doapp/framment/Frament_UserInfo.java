package com.lanxiao.doapp.framment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.untils.CinemaUtil;
import com.lanxiao.doapp.untils.DateUntils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;


/**
 * Created by Thinkpad on 2015/10/26.
 * 用户信息Frament
 */
public class Frament_UserInfo extends BaseFrament {
    private CircularImage iv_usrPic;
    private TextView iv_userName;
    private Bitmap bitmap;
    private SharedPreferences sp;
    private Boolean isWXLogin;
    public static final int REQUST_UserInfo_SYSTEM_PIC=0x22;
    public static final int REQUST_USERINFO_SYSTEM_CINEMA = 0x23;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                Bundle bundle=msg.getData();
                bitmap=bundle.getParcelable("bitmap");
                LogUtils.i((bitmap==null)+"");
                LogUtils.i((iv_usrPic == null) + "");
                iv_usrPic.setImageBitmap(bitmap);

            }
        }
    };

    public static Frament_UserInfo NewInstance(){
        Frament_UserInfo frament_userInfo=new Frament_UserInfo();
        return  frament_userInfo;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
                Log.i("TAG", "onAttach");
        sp=getActivity().getSharedPreferences("config",Context.MODE_PRIVATE);
        isWXLogin = sp.getBoolean("wxlogin", false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("TAG", "onViewCreated");

        Bundle bundle=getArguments();
       // bitmap=getArguments().getParcelable("bitmap");

        if(bundle!=null) {
            bitmap=bundle.getParcelable("bitmap");
            bitmap = DateUntils.toRoundBitmap(bitmap);
            iv_usrPic.setImageBitmap(bitmap);
        }
        iv_usrPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImage();
            }
        });
    }

    /**
     *  更换图片
     */
    private void changeImage() {
        new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT).setTitle("更换图像").setItems(new String[]{"拍照上传", "本地图片"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    Intent intent1 = CinemaUtil.IntoSystemPic(getActivity());//进入系统拍照
                    startActivityForResult(intent1, REQUST_USERINFO_SYSTEM_CINEMA);
                }
                if(i==1){
                    getSystemPic();
                }
            }
        }).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("TAG", "onCreateView");
        View v=inflater.inflate(R.layout.frament_userinfo,null);
        iv_usrPic= (CircularImage) v.findViewById(R.id.iv_userPic);
        iv_userName= (TextView) v.findViewById(R.id.iv_userName);

            String userName= Conversion.getInstance().getNickName();
            String aver=Conversion.getInstance().getHeadUrl();
            iv_userName.setText(userName);
            Utils.setAver(aver, iv_usrPic);
        return v;
    }
    /**
     * 获取系统相册资源
     */
    private void getSystemPic(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUST_UserInfo_SYSTEM_PIC);
    }
    public Handler getmHandler(){
        return mHandler;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i("Frament_UserInfo");
        if(requestCode==131106&&resultCode==getActivity().RESULT_OK){
            changeUserPic(data);
        }
        if(requestCode==REQUST_USERINFO_SYSTEM_CINEMA&&resultCode==getActivity().RESULT_OK){
            changeUserPicIsChmina();
        }
    }
    /**
     * 更改用户头像--通过本地相册
     * @param data
     */
    private void changeUserPic(Intent data){
        Uri uri=data.getData();
        String imagepath= CinemaUtil.getPath(getActivity(), uri);
        Bitmap cremaBitmap=CinemaUtil.getBitmap(imagepath);
        Bitmap bp=CinemaUtil.zoomImg(cremaBitmap, 300, 300);
        LogUtils.i((iv_usrPic == null) + "");
        iv_usrPic.setImageBitmap(bp);
    }
    /**
     * 更改用户头像--通过本地相册
     */
    private void changeUserPicIsChmina(){
        File file=CinemaUtil.getFile();
        Bitmap bitmap=CinemaUtil.getBitmap(file.getPath());
        Bitmap bp=CinemaUtil.zoomImg(bitmap,300,300);
        iv_usrPic.setImageBitmap(bp);
    }
}
