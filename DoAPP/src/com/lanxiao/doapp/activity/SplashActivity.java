package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.animation.AlphaAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.FileUtils;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	/**
	 * 升级的描述信息
	 */
	private String description;
	/**
	 * 最新的APK的升级的地址
	 */
	private String apkurl;
	private RelativeLayout rootLayout;

	private static final int sleepTime = 2000;
	private NotificationManager notificationManager=null;
	NotificationCompat.Builder mBuilder;
	private Notification mNotification;
	private WebView runWebView=null;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		getVersion();
		if(!Utils.isNetworkAvailable(this)){
			Toast.makeText(getApplicationContext(), "当前网络不可用！", Toast.LENGTH_LONG).show();
			intoMainActivity(0);
		}else {
			checkVersion();
		}
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(1500);
		rootLayout.startAnimation(animation);
	}

	@Override
	protected void onStart() {
		super.onStart();



	}
	long start=0;

	private void intoMainActivity(final long costTime){
		new Thread(new Runnable() {
			public void run() {
				if (DemoHelper.getInstance().isLoggedIn()) {
					// ** 免登陆情况 加载所有本地群和会话
					//不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
					//加上的话保证进了主页面会话和群组都已经load完毕
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();

					//等待sleeptime时长
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//进入主页面
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}
		}).start();
	}
	String version=null;
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
		String st = getResources().getString(R.string.Version_number_is_wrong);
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
			String versiontext = "All rights reserved.Ver."+packinfo.versionName;
			version=packinfo.versionName;
			return versiontext;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return st;
		}
	}
	/**
	 * 校验是否有新版本，如果有就升级
	 */
	private void checkVersion() {
		//Api.DOWNLOADAPK
		//"http://192.168.1.102:8080/updateinfo.html"
		start = System.currentTimeMillis();
		HttpUtils httpUtils=new HttpUtils(2000);
		RequestParams rp=new RequestParams();
		rp.addBodyParameter("version",version);
		httpUtils.send(HttpRequest.HttpMethod.POST,Api.DOWNLOADAPK,new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				long costTime = System.currentTimeMillis() - start;
				try {
					LogUtils.i(responseInfo.result);
					LogUtils.i(version);
					JSONObject jb = new JSONObject(responseInfo.result);
					if(!jb.optString("result").equals("-1")) {
						if (!version.equals(jb.optString("version"))) {
							myAlertDiog(jb.optString("desc"), jb.optString("download"));
						} else {
							intoMainActivity(costTime);
						}
					}else {
						intoMainActivity(costTime);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				Utils.showToast("网络获取数据失败", getApplicationContext());
				intoMainActivity(System.currentTimeMillis() - start);
			}
		});
	}
	/**
	 * 安装APK
	 * @param t
	 */
	private void installApk(File t) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
		startActivity(intent);

	}
	private void myAlertDiog(String message, final String appurl){
		AlertDialog.Builder builder=new AlertDialog.Builder(SplashActivity.this);
		builder.setTitle("软件版本升级");
		builder.setMessage(message);
		builder.setNegativeButton("立刻升级", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				LogUtils.i(FileUtils.hasSDCard()+"");
				if (FileUtils.hasSDCard()) {
					notificationInit(appurl);
					HttpUtils httpUtils = new HttpUtils("UTF-8");
					httpUtils.download(appurl, Environment.getExternalStorageDirectory() + "/" + new File(appurl).getName(), new RequestCallBack<File>() {
						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							Utils.showToast("下载完成", getApplicationContext());
							installApk(responseInfo.result);
						}

						@Override
						public void onLoading(long total, long current, boolean isUploading) {
							super.onLoading(total, current, isUploading);
							LogUtils.i("loading");
							int result = (int) (current * 100 / total);
							mNotification.contentView.setTextViewText(R.id.content_view_text1, result + "%");
							mNotification.contentView.setProgressBar(R.id.content_view_progress, (int) total, (int) current, false);
							notificationManager.notify(0, mNotification);
						}

						@Override
						public void onFailure(HttpException e, String s) {

						}
					});
				} else {
					Utils.showToast("未安装SD卡,此操作需要SD卡支持", getApplicationContext());
				}
				intoMainActivity(System.currentTimeMillis() - start);
			}
		});
		builder.setNeutralButton("下次再说", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				intoMainActivity(System.currentTimeMillis() - start);
			}
		});
		AlertDialog ad=builder.create();
		ad.setCanceledOnTouchOutside(false);
		ad.show();
	}
	private void notificationInit(String appurl){
		//通知栏内显示下载进度条
		Intent intent=new Intent(this,MainActivity.class);//点击进度条，进入程序
		intent.putExtra("type",10);
		intent.putExtra("apk",Environment.getExternalStorageDirectory() + "/" + new File(appurl).getName());
		PendingIntent pIntent= PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notificationManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(
				getApplicationContext()).setSmallIcon(R.drawable.mashen_noction)
				.setContentTitle("new message");
		mBuilder.setTicker("开始下载");//第一次提示消息的时候显示在通知栏上
		mBuilder.setNumber(12);
		mBuilder.setAutoCancel(true);//自己维护通知的消失
		mBuilder.setContentIntent(pIntent);
		mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
		mBuilder.setContent(new RemoteViews(getPackageName(),R.layout.updata_apk_view));
		mNotification=mBuilder.build();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
