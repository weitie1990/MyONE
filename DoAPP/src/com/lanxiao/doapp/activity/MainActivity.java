package com.lanxiao.doapp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.Conversion;
import com.easemob.easeui.Permissions.PermissionsActivity;
import com.easemob.easeui.Permissions.PermissionsChecker;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.FristLevelContantTongShi;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.easemob.util.EMLog;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.DemoDBManager;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.InviteMessgeDao;
import com.lanxiao.doapp.chatui.applib.chatuimain.domain.InviteMessage;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.Constant;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.Apply1;
import com.lanxiao.doapp.entity.Apply2;
import com.lanxiao.doapp.entity.Apply3;
import com.easemob.easeui.domain.ContantTongShi;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.entity.Work;
import com.lanxiao.doapp.framment.ConversationListFragment;
import com.lanxiao.doapp.framment.Frament_TabFriend;
import com.lanxiao.doapp.framment.Frament_UserInfo;
import com.lanxiao.doapp.framment.Frament_applay;
import com.lanxiao.doapp.framment.Frament_do_two;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.MoreWindow;
import com.lanxiao.doapp.untils.CinemaUtil;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 主界面
 * @author toshiba
 *
 */
public class MainActivity extends BaseActivity implements OnClickListener,EMEventListener {
	private String MainTAG="MainActivity";
	private static Boolean isClick = false;
	private Frament_do_two frament_d0;
	private Frament_applay frament_applay;
	private ConversationListFragment chatHistoryFragment;
	private android.support.v4.app.FragmentTransaction beginTransaction;
	private Frament_TabFriend frament_TabFriend;
	private RadioButton rb_applay,rb_doing;
	private RadioButton rb_message;
	private RadioButton rb_friend;
	private ImageView rb_home;
	private SharedPreferences.Editor editor;
	private RadioGroup tabs_rg;
	private FragmentTransaction ft;
	private MoreWindow mMoreWindow;
	private Uri uri;
	private int num=1;//1.默认为Doing 2.朋友 3.应用，4，聊天
	private int currentRadio= R.id.tab_rb_doing;//用来确定当前Radio选中状态
	View tool,top_line;
	Map<String,Object> WXUserInfo=new HashMap<>();


	private TextView unreadLabel,unreadAddressLable;
	// 账号在别处登录
	public boolean isConflict = false;
	// 账号被移除
	private boolean isCurrentAccountRemoved = false;
	private boolean IsWXLogin=false;
	private ArrayList<String> mSelectPath;
	int count=0;
	private Fragment[] fragments;
	int type=0;
	/*private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					backStatus();
					break;

			}
		}
	};*/

	private static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO,
			Manifest.permission.WRITE_CALENDAR,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,
	Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS};

	private PermissionsChecker checker;

	private static final int REQUEST_CODE = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
			// 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			DemoHelper.getInstance().logout(true,null);
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
			// 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
			// 三个fragment里加的判断同理
			finish();
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		setContentView(R.layout.activity_main);
		getWindow().setBackgroundDrawable(null);
		init(savedInstanceState);
		if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
		registerBroadcastReceiver();

	}
	private View[] mTabs;
	/**
	 * Fragment的TAG 用于解决app内存被回收之后导致的fragment重叠问题
	 */
	private static final  String[] FRAGMENT_TAG = {"do","meeage","friend","apply"};
	private void init(Bundle savedInstanceState){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadContant();//如果软件被卸载了，重新请求联系人
			}
		},5000);
		RequstWorkAndHomeItem();//第一次请求工作和应用界面item信息
		checker = new PermissionsChecker(this);
		String userid= DemoHelper.getInstance().getCurrentUsernName();
		try {
			PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", userid));
			if(Parent==null){
				saveToDb();
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		searchContentServer();
		DemoApplication.getInstance().addDestoryActivity(this,MainTAG);
		IsWXLogin=sp.getBoolean("wxlogin",false);
		inviteMessgeDao=new InviteMessgeDao(this);
		unreadLabel= (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable= (TextView) findViewById(R.id.unread_address_number);
		Bundle bundle=getIntent().getExtras();
		top_line=findViewById(R.id.top_line);
		rb_message=(RadioButton) findViewById(R.id.tab_rb_message);
		rb_home= (ImageView) findViewById(R.id.tab_rb_add);
		rb_applay=(RadioButton) findViewById(R.id.tab_rb_appliation);
		rb_doing=(RadioButton) findViewById(R.id.tab_rb_doing);
		rb_friend=(RadioButton) findViewById(R.id.tab_rb_friend);
		tabs_rg= (RadioGroup) findViewById(R.id.tabs_rg);
		mTabs=new View[4];
		mTabs[0]=rb_doing;
		mTabs[1]=findViewById(R.id.btn_container_conversation);
		mTabs[2]=findViewById(R.id.btn_container_address_list);
		mTabs[3]=rb_applay;
		// 把第一个tab设为选中状态
		mTabs[0].setSelected(true);
		rb_message.setOnClickListener(this);
		rb_home.setOnClickListener(this);
		rb_applay.setOnClickListener(this);
		rb_doing.setOnClickListener(this);
		rb_friend.setOnClickListener(this);
		beginTransaction=getSupportFragmentManager().beginTransaction();
		if (savedInstanceState != null ) {
			// 用于程序在后台被杀掉后恢复当前的状态
			index=savedInstanceState.getInt("index");
			frament_d0 = (Frament_do_two) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[0]);
			chatHistoryFragment = (ConversationListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[1]);
			frament_TabFriend = (Frament_TabFriend) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[2]);
			frament_applay = (Frament_applay) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG[3]);
			if(index==0){
				beginTransaction.show(frament_d0).hide(chatHistoryFragment).hide(frament_TabFriend).hide(frament_applay).commit();
			}else if(index==1){
				beginTransaction.show(chatHistoryFragment).hide(frament_d0).hide(frament_TabFriend).hide(frament_applay).commit();
			}else if(index==2){
				beginTransaction.show(frament_TabFriend).hide(frament_d0).hide(chatHistoryFragment).hide(frament_applay).commit();
			}else if(index==3){
				beginTransaction.show(frament_applay).hide(frament_d0).hide(chatHistoryFragment).hide(frament_TabFriend).commit();
			}
		}else {
			frament_d0= Frament_do_two.newInstance();
			frament_applay= Frament_applay.newInstance();
			chatHistoryFragment=new ConversationListFragment();
			frament_TabFriend= Frament_TabFriend.newInstanc();
			fragments=new Fragment[]{frament_d0,chatHistoryFragment,frament_TabFriend,frament_applay};
			beginTransaction.add(R.id.tab_content1, frament_d0, FRAGMENT_TAG[0])
					.add(R.id.tab_content1, chatHistoryFragment, FRAGMENT_TAG[1]).add(R.id.tab_content1,frament_TabFriend,FRAGMENT_TAG[2]).hide(chatHistoryFragment).hide(frament_TabFriend).show(frament_d0).commit();
		}

	}



	boolean isFragmentStatOk =true;
	int index;
	private int currentTabIndex=0;
	private int currentFrament=0;
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			//消息界面
			case R.id.tab_rb_message:
				index=1;
				break;
			//加号界面
			case R.id.tab_rb_add:

				showMoreWindow(v);
				return;
			//应用界面
			case R.id.tab_rb_appliation:
				index=3;
				break;
			//doing界面
			case R.id.tab_rb_doing:
				index=0;
				break;
			//朋友界面
			case R.id.tab_rb_friend:
				index=2;

				break;
		default:
			break;
		}
		if(mMoreWindow!=null&&mMoreWindow.isShowing()){
			mMoreWindow.dismiss();
		}
		showFrament();
	}

	/**
	 * 保存frament切换前的状态
	 */
	private void showFrament(){
		//isFragmentStatOk =false;
		if (currentTabIndex != index) {
			FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
			trx.hide(fragments[currentTabIndex]);
			/*if(index==1){
				trx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
						setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
			}else if(index==0){
				trx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				trx.setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right);
			}else if(index==2){
				trx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				trx.setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right);
			}else{
				trx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				trx.setCustomAnimations(R.anim.in_from_left, R.anim.out_to_right);
			}
			*/
			if (!fragments[index].isAdded()) {
				LogUtils.i("NO-ADD");
				trx.add(R.id.tab_content1, fragments[index],FRAGMENT_TAG[index]);
			}

			trx.show(fragments[index]).commit();

			mTabs[currentTabIndex].setSelected(false);
			// 把当前tab设为选中状态
			mTabs[index].setSelected(true);
			currentTabIndex = index;
		}

		//mHandler.sendMessageDelayed(mHandler.obtainMessage(1), postdely);
	}
	/**
	 * 显示弹出界面
	 * @param view
	 */
	private void showMoreWindow(View view) {
			if (null == mMoreWindow) {
				mMoreWindow = new MoreWindow(this);
				mMoreWindow.init(tabs_rg.getHeight());
				mMoreWindow.showMoreWindow(top_line);

			}else {
				if (!mMoreWindow.isShowing()) {

					mMoreWindow.showMoreWindow( top_line);
					LogUtils.i("show");

				} else {

					mMoreWindow.close();
					LogUtils.i("dismiss");
				}
			}
	}
	String imagePath=null;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED){
			finish();
		}
		//如果是userinfo中的相机和图片的请求事件，就传递下去
		if(requestCode==131106||requestCode==0x23){
			Fragment fragment=getSupportFragmentManager().findFragmentByTag("Frament_do");
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		//系统相机返回信息
		if(requestCode==0x00&&resultCode==RESULT_OK){
			mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			Intent intent=new Intent(MainActivity.this,InputActivity.class);
			intent.putStringArrayListExtra("imagePath", mSelectPath);
			startActivity(intent);
		}

		//裁剪图片返回的信息
		if(requestCode==0x01){
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=2;
			LogUtils.i(imagePath);
			Bitmap bitmap=BitmapFactory.decodeFile(imagePath,options);
			bitmap= CinemaUtil.compressImage(bitmap, 500);
			try {
				FileOutputStream fis=new FileOutputStream(new File(imagePath));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fis);
				fis.flush();
				fis.close();
				Frament_UserInfo frament_userInfo= Frament_UserInfo.NewInstance();
				Bundle bundle=new Bundle();
				bundle.putParcelable("bitmap",bitmap);
				frament_userInfo.setArguments(bundle);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LogUtils.i(requestCode + "," + resultCode);
		if(requestCode==0x18){
			ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.tab_content1, frament_d0).commit();
			currentRadio= R.id.tab_rb_doing;
			LogUtils.i("frament_d0");
		}

	}


	public static long postdely=600;


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(com.easemob.R.menu.main, menu);
		return true;
	}
	void backStatus(){
		isFragmentStatOk = true;
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if(!isFragmentStatOk){
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(!isFragmentStatOk){
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterBroadcastReceiver();

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		LogUtils.i(count + "");
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count2 = getUnreadAddressCountTotal();
				count=count2;
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
					frament_TabFriend.updateUnreadAddressLable(count2);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}
	/**
	 * 获取未读申请与通知消息
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
		return unreadAddressCountTotal;
	}
	/**
	 * 获取未读消息数
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		int chatroomUnreadMsgCount = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		for (EMConversation conversation : EMChatManager.getInstance().getAllConversations().values()) {
			if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
				chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
		}
		return unreadMsgCountTotal - chatroomUnreadMsgCount;
	}
	/**
	 * 检查当前用户是否被删除
	 */
	public boolean getCurrentAccountRemoved() {
		return isCurrentAccountRemoved;
	}
	/**
	 * 保存提示新消息
	 *
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		// 提示有新消息
		DemoHelper.getInstance().getNotifier().viberateAndPlayTone(null);
		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
	}
	private InviteMessgeDao inviteMessgeDao;
	/**
	 * 监听事件
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		LogUtils.i("EventNewMessage");
		switch (event.getEvent()) {
			case EventNewMessage: // 普通消息
			{
				EMMessage message = (EMMessage) event.getData();

				// 提示新消息
				DemoHelper.getInstance().getNotifier().onNewMsg(message);

				refreshUI();
				break;
			}

			case EventOfflineMessage: {
				refreshUI();
				break;
			}

			case EventConversationListChanged: {
				refreshUI();
				break;
			}

			default:
				break;
		}
	}

	private void refreshUI() {
		runOnUiThread(new Runnable() {
			public void run() {
				// 刷新bottom bar消息未读数
				LogUtils.i("refreshUI");
				updateUnreadLabel();

				if (currentRadio == R.id.tab_rb_message) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		});
	}
	private BroadcastReceiver broadcastReceiver;
	private LocalBroadcastManager broadcastManager;
	private void registerBroadcastReceiver() {
		broadcastManager = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
		intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
		broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				updateUnreadLabel();
				updateUnreadAddressLable();
				if (currentRadio == R.id.tab_rb_message) {
					// 当前页面如果为聊天历史页面，刷新此页面
					if (chatHistoryFragment != null) {
						chatHistoryFragment.refresh();
					}
				}
			}
		};
		broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}
	private void unregisterBroadcastReceiver(){
		broadcastManager.unregisterReceiver(broadcastReceiver);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
		}
		// unregister this event listener when this activity enters the
		// background
		DemoHelper sdkHelper = DemoHelper.getInstance();
		sdkHelper.pushActivity(this);

		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(this,
				new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
		MobclickAgent.onResume(this);
		if (checker.lacksPermissions(PERMISSIONS)) {
			startPermissionsActivity();
		}
	}
	Boolean isBack=false;
	@Override
	public void onBackPressed() {
		if(isBack){
			super.onBackPressed();
		}else {
			Toast.makeText(MainActivity.this,"再点一次退出程序",Toast.LENGTH_LONG).show();
			isBack=true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						isBack=false;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}
	/**
	 * 微信用户第一次进来保存个人详细信息.获取好友列表 本地DB
	 */
	private void saveToDb(){
		RequestParams params = new RequestParams("UTF-8");
		LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
		params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
		HttpUtils httpUtils=new HttpUtils(5000);
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.USER_PERSON_INFO, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				try {
					JSONObject jb = new JSONObject(responseInfo.result);
					PersonInfo pi = new PersonInfo();
					pi.setUserid(DemoHelper.getInstance().getCurrentUsernName());
					pi.setNickname(jb.optString("nickname"));
					pi.setLastName(jb.optString("LastName"));
					pi.setBornDate(jb.optString("BornDate"));
					pi.setSex(jb.optString("Sex"));
					pi.setCertificateID(jb.optString("CertificateID"));
					pi.setTag1(jb.optString("tag1"));
					pi.setJobTitle(jb.optString("JobTitle"));
					pi.setCellPhoneNumber(jb.optString("CellPhoneNumber"));
					pi.setMailAddress(jb.optString("MailAddress"));
					pi.setPhoneNumber(jb.optString("PhoneNumber"));
					pi.setCountry(jb.optString("Country"));
					pi.setCity(jb.optString("City"));
					pi.setCompanyName(jb.optString("CompanyName"));
					pi.setCompanyID(jb.optString("CompanyID"));
					pi.setDepartment(jb.optString("Department"));
					pi.setDepartmentID(jb.optString("DepartmentID"));
					pi.setState(jb.optString("State"));
					try {
						DemoApplication.getInstance().getDb().save(pi);
					} catch (DbException e) {
						e.printStackTrace();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				Utils.showToast("数据获取失败", getApplicationContext());
			}
		});


	}


	/**
	 * 一进入主界面就更新自己的用户头像和昵称
	 */
	public void searchContentServer(){
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("keyword", DemoHelper.getInstance().getCurrentUsernName());
		params.addBodyParameter("type", "1");
		HttpUtils httpUtils = new HttpUtils(5000);
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_FRIEND, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i(arg0.getMessage() + ":" + arg1);
				Utils.showToast("请求网络异常", getApplicationContext());
				try {
					PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
					if(Parent!=null){
						Conversion.getInstance().setNickName(Parent.getNickname());
						Conversion.getInstance().setHeadUrl(Parent.getHeadUrl());
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
				return;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				try {
					PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
					JSONObject js = new JSONObject(responseInfo.result);
					if (js.optString("message").equals("成功")) {
						List<DoUser> list = new ArrayList<DoUser>();
						JSONArray ja=js.getJSONArray("userlist");
						for (int i=0;i<ja.length();i++){
							JSONObject js2=ja.getJSONObject(i);
							String userId = js2.optString("userid");
							String heanUrl = js2.optString("userheadlogo");
							String nicKName = js2.optString("nickname");
							Conversion.getInstance().setNickName(nicKName);
							Conversion.getInstance().setHeadUrl(heanUrl);
							LogUtils.i("heanurl:" + heanUrl + "nickname:" + nicKName);
							if(Parent!=null){
								LogUtils.i("update");
								Parent.setHeadUrl(heanUrl);
								Parent.setNickname(nicKName);
								DemoApplication.getInstance().getDb().update(Parent);
							}
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		});


	}

	private boolean isConflictDialogShow;
	private boolean isAccountRemovedDialogShow;
	private AlertDialog.Builder accountRemovedBuilder;
	private AlertDialog.Builder conflictBuilder;
	/**
	 * 帐号被移除的dialog
	 */
	private void showAccountRemovedDialog() {
		isAccountRemovedDialogShow = true;
		DemoHelper.getInstance().logout(true, null);
		String st5 = getResources().getString(R.string.Remove_the_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (accountRemovedBuilder == null)
					accountRemovedBuilder = new AlertDialog.Builder(MainActivity.this);
				accountRemovedBuilder.setTitle(st5);
				accountRemovedBuilder.setMessage("此用户已被移除");
				accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						accountRemovedBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				accountRemovedBuilder.setCancelable(false);
				accountRemovedBuilder.create().show();
				isCurrentAccountRemoved = true;
			} catch (Exception e) {
				EMLog.e(MainTAG, "---------color userRemovedBuilder error" + e.getMessage());
			}

		}
	}
	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		DemoHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new AlertDialog.Builder(MainActivity.this);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						conflictBuilder = null;
						finish();
						startActivity(new Intent(MainActivity.this, LoginActivity.class));
					}
				});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(MainTAG, "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		type=intent.getIntExtra("type",0);
		LogUtils.i("type:" + type);
		//添加类型跳转过来的
		if(type==1){
			index=3;
			showFrament();
			if(mMoreWindow!=null&&mMoreWindow.isShowing()){
				mMoreWindow.dismiss();
			}
			//发表DO跳转过来了
		}else if(type==2){
			index=0;
			showFrament();
			frament_d0.resh();
			if(mMoreWindow!=null&&mMoreWindow.isShowing()){
				mMoreWindow.dismiss();
			}
		}
		//jpsh通知点击中转过来的
		else if(type==3){
			index=0;
			showFrament();
			frament_d0.resh();
			//会议提交返回过来的
		}else if(type==4){
			index=0;
			showFrament();
			if(mMoreWindow!=null&&mMoreWindow.isShowing()){
				mMoreWindow.dismiss();
			}
			frament_d0.resh();
			//签到提交返回过来的
		}else if(type==5){
			index=0;
			showFrament();
			frament_d0.resh();
			if(mMoreWindow!=null&&mMoreWindow.isShowing()){
				mMoreWindow.dismiss();
			}
		}
		//二维码扫描添加好友传递过来的
	else if(type==6){
		index=0;
		showFrament();
		if(mMoreWindow!=null&&mMoreWindow.isShowing()) {
			mMoreWindow.dismiss();
		}
			String result=intent.getStringExtra("result");
			addContact(result);
			//安装apk
	}else if(type==10){
			String apkurl=intent.getStringExtra("apk");
			LogUtils.i("apkurl:"+apkurl);
			installApk(new File(apkurl));
		}
		if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
			showConflictDialog();
		} else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
			showAccountRemovedDialog();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		outState.putInt("index", index);
		super.onSaveInstanceState(outState, outPersistentState);

	}
	private ProgressDialog progressDialog;
	/**
	 *  添加contact
	 */
	public void addContact(final String userId){
		if(EMChatManager.getInstance().getCurrentUser().equals(userId)){
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}

		if(DemoHelper.getInstance().getContactList().containsKey(userId)){
			//提示已在好友列表中(在黑名单列表里)，无需添加
			if(EMContactManager.getInstance().getBlackListUsernames().contains(userId)){
				new EaseAlertDialog(this, "已在好友列表中").show();
				return;
			}
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}

		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		new Thread(new Runnable() {
			public void run() {

				try {
					//demo写死了个reason，实际应该让用户手动填入
					String s = getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(userId, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);

	}
	private void startPermissionsActivity() {
		PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
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
	private void RequstWorkAndHomeItem(){

		try {
			List<Work> all = DemoApplication.getInstance().getDb().findAll(Work.class);
			if(all==null||all.size()==0){
				RequestParams params = new RequestParams("UTF-8");
				params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
				HttpUtils httpUtils = new HttpUtils(5000);
				//保存工作中的每项
				httpUtils.send(HttpRequest.HttpMethod.POST, Api.WORKITEM, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.i(arg0.getMessage() + ":" + arg1);

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.i(responseInfo.result);
						try {
							JSONObject js = new JSONObject(responseInfo.result);
							JSONArray ja = js.optJSONArray("worklist");
							for (int i = 0; i < ja.length(); i++) {
								JSONObject jb = ja.getJSONObject(i);
								Work work = new Work();
								work.setName(jb.optString("name"));
								work.setType(jb.optString("type"));
								work.setTarget(jb.optString("target"));
								work.setLogo(jb.optString("logo"));
								DemoApplication.getInstance().getDb().save(work);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				});
				//保存工作中的每项
				httpUtils.send(HttpRequest.HttpMethod.POST, Api.APPLYITEM, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.i(arg0.getMessage() + ":" + arg1);

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						LogUtils.i(responseInfo.result);
						try {
							JSONObject js = new JSONObject(responseInfo.result);
							JSONArray ja=js.optJSONArray("applist");
							for (int i=0;i<ja.length();i++){
								JSONObject jb=ja.getJSONObject(i);
								JSONArray jasum = jb.getJSONArray("list");
								if(i==0) {
									Apply1 apply1 = new Apply1();
									for (int j = 0; j < jasum.length(); j++) {
										apply1.setTitle(jb.optString("title"));
										JSONObject jbsum = jasum.getJSONObject(j);
										apply1.setName(jbsum.optString("name"));
										apply1.setType(jbsum.optString("type"));
										apply1.setTarget(jbsum.optString("target"));
										apply1.setLogo(jbsum.optString("logo"));
										DemoApplication.getInstance().getDb().save(apply1);
									}
								}
								if(i==1) {
									Apply2 apply2 = new Apply2();
									for (int j = 0; j < jasum.length(); j++) {
										apply2.setTitle(jb.optString("title"));
										JSONObject jbsum = jasum.getJSONObject(j);
										apply2.setName(jbsum.optString("name"));
										apply2.setType(jbsum.optString("type"));
										apply2.setTarget(jbsum.optString("target"));
										apply2.setLogo(jbsum.optString("logo"));
										DemoApplication.getInstance().getDb().save(apply2);
									}
								}
								if(i==2) {
									Apply3 apply3 = new Apply3();
									for (int j = 0; j < jasum.length(); j++) {
										apply3.setTitle(jb.optString("title"));
										JSONObject jbsum = jasum.getJSONObject(j);
										apply3.setName(jbsum.optString("name"));
										apply3.setType(jbsum.optString("type"));
										apply3.setTarget(jbsum.optString("target"));
										apply3.setLogo(jbsum.optString("logo"));
										DemoApplication.getInstance().getDb().save(apply3);
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				});
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 加载联系人
	 */
	private void loadContant(){
		try {
			List<EaseUser> whosfriend = DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("whosfriend", "=", DemoHelper.getInstance().getCurrentUsernName()));
			if(whosfriend!=null){
				DemoApplication.getInstance().getDb().delete(EaseUser.class, WhereBuilder.b("whosfriend","=",DemoHelper.getInstance().getCurrentUsernName()));
			}
			 //每次进入获取联第人列表
				Map<String, EaseUser> contactsMap = DemoHelper.getInstance().getContactList();
				List<String> blackList = EMContactManager.getInstance().getBlackListUsernames();
				if(contactsMap == null){
					return;
				}
				StringBuffer sb=new StringBuffer();
				Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, EaseUser> entry = iterator.next();
					if(!blackList.contains(entry.getKey())){
						//不显示黑名单中的用户
						EaseUser user = entry.getValue();
						EaseCommonUtils.setUserInitialLetter(user);
						sb.append(user.getUsername()+",");
					}

				}
				if(sb.length()!=0){
					sb.deleteCharAt(sb.length()-1);
					com.lidroid.xutils.util.LogUtils.i(sb.toString());
					saveContentServer(sb.toString());
				}

		} catch (DbException e) {
			e.printStackTrace();
		}

		try {
				PersonInfo Parent = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
				if (Parent == null|| TextUtils.isEmpty(Parent.getCompanyID())) {
					return;
				}
			ContantTongShi whosfriend = DemoApplication.getInstance().getDb().findFirst(Selector.from(ContantTongShi.class));
			if(whosfriend==null){
				RequestParams params = new RequestParams("UTF-8");
				params.addBodyParameter("id", Parent.getCompanyID());
				HttpUtils httpUtils = new HttpUtils();
				httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_ALL_TONGSHI, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						com.lidroid.xutils.util.LogUtils.i(arg0.getMessage() + ":" + arg1);
						return;
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
						try {
							JSONObject js = new JSONObject(responseInfo.result);
							JSONArray ja = js.getJSONArray("companyuserlist");
							if(ja==null){
								return;
							}
							LogUtils.i("ja.length:"+ja.length());
							for(int i = 0; i < ja.length(); i++){
								JSONObject js1 = ja.getJSONObject(i);
								FristLevelContantTongShi tc=new FristLevelContantTongShi();
								tc.setFullname(js1.optString("fullname"));
								tc.setId(js1.optInt("id"));
								tc.setName(js1.optString("name"));
								LogUtils.i("savetc");
								JSONArray jauser111=js1.getJSONArray("userlist");
								if(jauser111!=null){
									//List<EaseUser> fieldList = new ArrayList<EaseUser>();
									for(int j=0;j<jauser111.length();j++){
										JSONObject jb111 = jauser111.getJSONObject(j);
										EaseUser eu=new EaseUser();
										String ToNickName = jb111.optString("username");
										eu.setUserId(jb111.optString("userid"));
										eu.setUsername(jb111.optString("userid"));
										eu.setNickName(ToNickName);
										eu.setAvatar(jb111.optString("userhead"));
										eu.setCellphone(jb111.optString("cellphone"));
										eu.setInitialLetter(Utils.getPingYin(ToNickName));
										eu.setCompanyId(js1.optInt("id"));
										//fieldList.add(eu);
										LogUtils.i("eu");
										DemoApplication.getInstance().getDb().save(eu);
									}
								}
								DemoApplication.getInstance().getDb().save(tc);
								if(TextUtils.isEmpty(js1.optString("subdept"))){
									continue;
								}



								JSONArray ja1=js1.getJSONArray("subdept");
								if(ja1!=null){
									for(int k=0;k<ja1.length();k++){
										JSONObject jssum = ja1.getJSONObject(k);
										ContantTongShi tcsum=new ContantTongShi();
										tcsum.setFullname(jssum.optString("fullname"));
										tcsum.setId(jssum.optInt("id"));
										tcsum.setName(jssum.optString("name"));
										tcsum.setTongshiId(js1.optInt("id"));
										JSONArray jauser=jssum.getJSONArray("userlist");
										if(jauser!=null){
											//List<EaseUser> fieldList = new ArrayList<EaseUser>();
											for(int j=0;j<jauser.length();j++){
												JSONObject js2 = jauser.getJSONObject(j);
												EaseUser eu=new EaseUser();
												String ToNickName = js2.optString("username");
												eu.setUserId(js2.optString("userid"));
												eu.setUsername(js2.optString("userid"));
												eu.setNickName(ToNickName);
												eu.setAvatar(js2.optString("userhead"));
												eu.setCellphone(js2.optString("cellphone"));
												eu.setInitialLetter(Utils.getPingYin(ToNickName));
												eu.setCompanyId(jssum.optInt("id"));
												//eu.setParent(tcsum);
												//fieldList.add(eu);
												LogUtils.i("eu");
												DemoApplication.getInstance().getDb().save(eu);
											}
										}
										DemoApplication.getInstance().getDb().save(tcsum);
									}
								}

							}

						} catch (JSONException e) {
							e.printStackTrace();
						} catch (DbException e) {
							e.printStackTrace();
						}

					}
				});
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 保存好友到本地数据库和获取到同事保存到本地
	 */
	public void saveContentServer(final String from) {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("useridlist", from);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_ALL_FRIEND, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				com.lidroid.xutils.util.LogUtils.i(arg0.getMessage() + ":" + arg1);
				return;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
				String ToNickName = null;
				try {
					JSONObject js = new JSONObject(responseInfo.result);
					JSONArray ja = js.getJSONArray("userlist");
					if (ja == null) {
						return;
					}
					for (int i = 0; i < ja.length(); i++) {
						JSONObject js2 = ja.getJSONObject(i);
						EaseUser easeUser = new EaseUser();
						ToNickName = js2.optString("nickname");
						easeUser.setUserId(js2.optString("userid"));
						easeUser.setUsername(js2.optString("userid"));
						easeUser.setNickName(ToNickName);
						easeUser.setAvatar(js2.optString("touxiang"));
						easeUser.setInitialLetter(Utils.getPingYin(ToNickName));
						easeUser.setWhosfriend(DemoHelper.getInstance().getCurrentUsernName());
						DemoDBManager.getInstance().getDoappContactList(easeUser);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}
}
