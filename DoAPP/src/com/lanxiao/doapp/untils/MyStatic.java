package com.lanxiao.doapp.untils;

import android.os.Environment;
import android.view.View;

import java.io.File;

/**
 * Created by Thinkpad on 2015/11/6.
 */
public class MyStatic {

    public static View v;//保存用户给关注的view
    public static int currentIndex=0;//用来确定 "关注"，"我的"，"朋友的"的TAG
    public static String FileCache="/doapp/";
    public static String VoiceCache="/doapp/";
    public static String IMAGECACHE="doapp";
    /**
     * 判断文件挂载情况
     * @param path
     * @return
     */
    public static File createFile(String path){
        String fileFath=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            fileFath=Environment.getExternalStorageDirectory()+path;
            //各种未加载SD的的情况
        }else if(Environment.getExternalStorageState().equals(Environment.MEDIA_BAD_REMOVAL)||
                Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)||
                Environment.getExternalStorageState().equals(Environment.MEDIA_UNKNOWN)
                ){
            fileFath=Environment.getDataDirectory()+path;
        }
        File file=new File(fileFath);
        return file;
    }


}
