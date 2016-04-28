package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.untils.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.bitlet.weupnp.Main;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 微信扫描后web网页界面
 * Created by Thinkpad on 2015/10/28.
 */
public class WebActivity extends BaseActivity{
    WebView webView;
    ProgressBar progressBar;
    String url;
    Button btn;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        url=getIntent().getStringExtra("result");
        tv= (TextView) findViewById(R.id.tv_web_content);
        Log.i("weitie:", "url2:" + url);
        if(Utils.isNO(url)&&url.startsWith("1")&&url.length()==5){
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("type",6);
            intent.putExtra("result",url);
            startActivity(intent);
            finish();
        }
        if(!url.contains("http://")){
            url="http://"+url;
        }
        if (Patterns.WEB_URL.matcher(url).matches()) {

            //符合标准
        } else{
            //不符合标准
            tv.setText(url);
        }


        System.out.print(url);
        webView= (WebView) findViewById(R.id.webView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        btn= (Button) findViewById(R.id.btn_web_ok);
        //支持javascipt
        webView.getSettings().setJavaScriptEnabled(true);
        //设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        //显示缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.loadUrl(url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
            }
        });


        webView.addJavascriptInterface(
                new Object() {
                    //js向app传参数
                    @JavascriptInterface
                    public void ReplayFromJs(final String mainid) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(WebActivity.this, mainid, Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                }, "doapp"); //doapp是对象名
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //app调用js中的方法传递参数
                webView.loadUrl("javascript:showa()");
            }
        });

    }
    String mainId=null;
    Handler mHandler=new Handler();
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
    public static boolean checkURL(String url){
        boolean value=false;
        try {
            HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
            int code=conn.getResponseCode();
            System.out.println(">>>>>>>>>>>>>>>> "+code+" <<<<<<<<<<<<<<<<<<");
            if(code!=200){
                value=false;
            }else{
                value=true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
