package com.lanxiao.doapp.untils.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.doapp.R;
import com.lanxiao.doapp.entity.MyFile;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Thinkpad on 2015/12/3.
 */
public class FileUtils {
    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent); //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    public static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }
    public static final String[][] MIME_MapTable={
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };
    private static ArrayList<MyFile> items=new ArrayList<MyFile>();

    public static File currentDir;
    /**
     * 遍历文件目录
     *
     * @param dir
     */
    public static ArrayList<MyFile> browserTo(File dir,Context context) {
        String fileTime = null;
        if (hasSDCard()) {
            if (dir.isDirectory()) {
                currentDir = dir;
                Log.i("text", currentDir.getPath());
            }
            File[] files = dir.listFiles();
            if (items == null) {
                items = new ArrayList<MyFile>();
            }
            items.clear();
            Resources res = context.getResources();
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    Drawable icon = null;
                    if (file.isDirectory()) {
                        String num = file.listFiles().length + "";
                        long lastModified = file.lastModified();
                        fileTime = getFileTime(lastModified);
                        MyFile item = new MyFile();
                        item.setFile_Name(fileName);
                        item.setIcon(R.drawable.folder_img);
                        item.setFile_Path(file.getAbsolutePath());
                        item.setFile_lenth(num);
                        item.setFile_CreateTime(fileTime);
                        items.add(item);
                    } else {
                        icon = res.getDrawable(R.drawable.file_txt);
                        long lastModified = file.lastModified();
                        fileTime = getFileTime(lastModified);
                        MyFile item = new MyFile();
                        item.setFile_Name(fileName);
                        item.setIcon(R.drawable.file_txt);
                        item.setFile_Path(file.getAbsolutePath());
                        item.setFile_lenth(FileSize(file.length()));
                        item.setFile_CreateTime(fileTime);
                        items.add(item);
                    }
                }
            }
        }
        return items;
    }
    /**
     * 将文件大小转成字符串
     * @param fileSize
     * @return
     */
    public static String FileSize(long fileSize){
        DecimalFormat df=new DecimalFormat("#.00");
        String FileSizeString="";
        if(fileSize<1024){
            FileSizeString=df.format((double)fileSize)+"B";
        }else if(fileSize < 1048576){
            FileSizeString=df.format((double)fileSize/1024)+"K";
        }else if(fileSize< 1073741824){
            FileSizeString=df.format((double)fileSize/1048576)+"M";
        }else{
            FileSizeString=df.format((double)fileSize/1073741824)+"G";
        }
        return FileSizeString;
    }
    /**
     * 将当前时间长按格式转化
     * @param time
     * @return
     */
    public static String getFileTime(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(time));
        return date;
    }
    /**
     * 判断sd卡是否存在
     * @return
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }
    /**
     * 判断是否为图片
     * @return
     */
    public static boolean isImage(String path) {
        Bitmap drawable = BitmapFactory.decodeFile(path);
        if(drawable == null){
            return false;
        }
        return true;
    }
}
