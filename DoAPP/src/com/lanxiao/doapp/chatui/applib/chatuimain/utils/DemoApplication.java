/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanxiao.doapp.chatui.applib.chatuimain.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.baidu.mapapi.SDKInitializer;
import com.lanxiao.doapp.activity.MainActivity;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lidroid.xutils.DbUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class DemoApplication extends Application {

    public static Context applicationContext;
    private static DemoApplication instance;
    // login user name
    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    private IWXAPI wxApi;
    // APP_ID 应用从微信授权申请到的合法appId
    public static final String APP_ID = "wxfb89c1c93dcc556a";
    private static Map<String, FragmentActivity> destoryMap = new HashMap<>();
    private String unionid;
    private String aver;
    private String bodyvalue;
    DbUtils db;
    public void setBodyvalue(String bodyvalue) {
        this.bodyvalue = bodyvalue;
    }
    public String getBodyvalue(){
        return bodyvalue;
    }
    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        initDb();
        wxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        wxApi.registerApp(APP_ID);
        //极光推送初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(applicationContext);
        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
        SDKInitializer.initialize(applicationContext);
        //监听异常
        //Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        mRefWatcher=LeakCanary.install(this);

    }
    public static RefWatcher getRefWatcher() {
        return getInstance().mRefWatcher;
    }
    public DbUtils getDb(){
        return db;
    }

    public static DemoApplication getInstance() {
        return instance;
    }
    public IWXAPI getApi() {
        return wxApi;
    }
    /**
     * 缓存的配置
     */


    /**
     * 初始化db
     */
    private void initDb(){
        DbUtils.DaoConfig config = new DbUtils.DaoConfig(this);
        config.setDbName("doapp"); //db名
        config.setDbVersion(1);  //db版本
        db = DbUtils.create(config);//db还有其他的一些构造方法，比如含有更新表版本的监听器的
    }
    // 共享变量
    private Handler handler = null;

    // set方法
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    // get方法
    public Handler getHandler() {
        return handler;
    }


    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */

    public void addDestoryActivity(FragmentActivity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }


    public void setAver(String aver) {
        this.aver = aver;
    }
    private String nickName;
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }
    private class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            System.out.println("哥捕获一个异常......");

           /* try {
                File file = new File(Environment.getExternalStorageDirectory(), "carch.log");
                PrintWriter err = new PrintWriter(file);
                ex.printStackTrace(err );
                err.flush();
                err.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            //先在友盟上面保存错误信息   todo debug时关闭些方法
            MobclickAgent.onKillProcess(applicationContext);
            Intent intent = new Intent(applicationContext, MainActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    applicationContext, 0, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT );
            //退出程序
            AlarmManager mgr = (AlarmManager)applicationContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent); // 1秒钟后重启应用
            //早死早超生
            android.os.Process.killProcess(android.os.Process.myPid());


        }

    }
}
