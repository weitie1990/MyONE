package com.zhy.autolayout.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhy on 15/12/4.<br/>
 * form http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels/15699681#15699681
 */
public class ScreenUtils
{
    public static int windowHeight;
    public static int windowWidth;
    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        try
        {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e)
        {
            e.printStackTrace();
        }
        return result;
    }


    public static int[] getScreenSize(Context context, boolean useDeviceSize)
    {

        int[] size = new int[2];

        WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
// since SDK_INT = 1;
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        if (!useDeviceSize)
        {
            size[0] = widthPixels;
            size[1] = heightPixels - getStatusBarHeight(context);
            Log.i("size[0]:size[1]",size[0]+","+size[1]);
            return size;
        }

// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try
            {
                widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored)
            {
            }
// includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17)
            try
            {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ignored)
            {
            }
        size[0] = widthPixels;
        size[1] = heightPixels;
        return size;
    }
    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    /**
     * 判断底部navigator是否已经显示
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static boolean hasSoftKeys(WindowManager windowManager){
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }
}
