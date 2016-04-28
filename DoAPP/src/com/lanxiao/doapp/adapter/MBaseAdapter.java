package com.lanxiao.doapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.BaseAdapter;

import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Thinkpad on 2015/11/9.
 */
public abstract class MBaseAdapter extends BaseAdapter {
    public  int getScreenHeight( FragmentActivity mContext){
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.heightPixels;
    }
    public int getScreenWidth(Activity mContext){
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        return metrics.widthPixels;
    }
    public SpannableStringBuilder addClickablePart(String str, final String userid,int lenth, final Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        if (str.length() > 0) {
            // 最后一个
                ssb.setSpan(new ClickableSpan() {

                    @Override
                    public void onClick(View widget) {
                        LogUtils.i("点击了人名");
                        Utils.intoInfo(context, userid);

                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        // ds.setColor(Color.RED); // 设置文本颜色
                        ds.setColor(Color.BLUE);
                        // 去掉下划线
                        ds.setUnderlineText(false);
                    }

                }, 3, 3 + lenth, 0);
            }
        return ssb;
    }
}
