package com.lanxiao.doapp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.search.core.PoiInfo;
import com.easemob.EMError;
import com.easemob.chat.EMGroup;
import com.easemob.easeui.Conversion;
import com.easemob.easeui.domain.EaseEmojicon;
import com.easemob.easeui.domain.EaseEmojiconGroupEntity;
import com.easemob.easeui.model.EaseDefaultEmojiconDatas;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.easemob.easeui.widget.emojicon.EaseEmojiconMenu;
import com.easemob.easeui.widget.emojicon.EaseEmojiconMenuBase;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.adapter.CommentAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.CommonUtils;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.MyFile;
import com.lanxiao.doapp.entity.ReplyBean;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.CircleProgressBar;
import com.lanxiao.doapp.myView.MenuRightAnimations;
import com.lanxiao.doapp.myView.PopupList;
import com.lanxiao.doapp.untils.DateUntils;
import com.lanxiao.doapp.untils.SoundMeter;
import com.lanxiao.doapp.untils.util.MyVoiceRecorder;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PublicActivity extends BaseActivity implements
		OnClickListener {
	/** Called when the activity is first created. */

	private RelativeLayout rl_bar_bottom;
	private RelativeLayout overlayView;

	private ListView dataListView;
	private LinearLayout ll_btn_container;

	// clock

	// activity_overlay


	private String Tag = "PublicActivity.java";

	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private EditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	// private ViewPager expressionViewpager;
	// 给谁发送消息
	private MyVoiceRecorder voiceRecorder;
	private ImageView iv_emoticons_normal,btn_add_file,backforchat,btn_picture,btn_location,btn_chatroom;
	private ImageView iv_emoticons_checked;
	private RelativeLayout edittext_layout;
	private Button btnMore;
	private Button btn_send;
	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	public EMGroup group;
	private String comment = "";		//记录对话框中的内容
	private CommentAdapter McommentAdapter;
	private FriendStateItem fs;
	private String name=null;
	private double latitude=0;
	private double longitude=0;
	private String chatroomid=null;
	private SwipeRefreshLayout swipeRefreshLayout;
	private CircleProgressBar feed_circleProgressBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_activity2);
		init();
		/*
		 * 
		 * ################################变体菜单##################################
		 * ###
		 */
		Log.v(Tag, "onCreat 菜单初始化开始");
		MenuRightAnimations.initOffset(PublicActivity.this);

		rl_bar_bottom= (RelativeLayout) findViewById(R.id.rl_bar_bottom);
		rl_bar_bottom.setVisibility(View.VISIBLE);


		Log.v(Tag, "onCreat 菜单初始化结束");
		/*
		 * 
		 * ################################变体菜单##################################
		 * ###
		 */
		dataListView = (ListView) findViewById(R.id.list_view);

		dataListView.setCacheColorHint(Color.TRANSPARENT);
		McommentAdapter = new CommentAdapter(PublicActivity.this, list_reply, R.layout.feed_comments_item, fs);
		dataListView.setAdapter(McommentAdapter);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				requstLinken(docId);
			}
		});
	}
	List<EaseEmojiconGroupEntity> emojiconGroupList;
	EaseEmojiconMenu faceLayout;
	ViewPager fuctionViewPager;
	LinearLayout pagePointLayout;
	RelativeLayout bottomHideLayout;
	String docId=null;
	/**
	 * 初始化
	 */
	private void init(){
		Bundle bundle=getIntent().getExtras();
		docId=bundle.getString("docId");
		int type=bundle.getInt("type", 0);

		if(type==2){
			fs= (FriendStateItem) bundle.getSerializable("fs");
		}
		feed_circleProgressBar= (CircleProgressBar) findViewById(R.id.feed_circleProgressBar);
		swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.feed_swipeRefeshLayout);
		swipeRefreshLayout.setColorSchemeResources(com.easemob.easeui.R.color.holo_blue_bright, com.easemob.easeui.R.color.holo_green_light,
				com.easemob.easeui.R.color.holo_orange_light, com.easemob.easeui.R.color.holo_red_light);
		bottomHideLayout = (RelativeLayout) findViewById(R.id.bottomHideLayout1);
		faceLayout = (EaseEmojiconMenu) findViewById(R.id.faceLayout1);
		//faceCategroyViewPager = (ViewPager) findViewById(R.id.faceCategroyViewPager1);
		fuctionViewPager = (ViewPager) findViewById(R.id.fuctionViewPager1);
		pagePointLayout = (LinearLayout) findViewById(R.id.pagePointLayout1);
		LogUtils.i("weitie:"+"headurl"+Conversion.getInstance().getHeadUrl());
		mSensor = new SoundMeter();
		backforchat= (ImageView) findViewById(R.id.backforchat);
		btn_add_file= (ImageView) findViewById(R.id.btn_add_file);
		ll_btn_container= (LinearLayout) findViewById(R.id.ll_btn_container);
		btn_picture= (ImageView) findViewById(R.id.btn_picture);
		btn_location= (ImageView) findViewById(R.id.btn_location);
		btn_chatroom= (ImageView) findViewById(R.id.btn_chatroom);
		btn_send= (Button) findViewById(R.id.btn_send);
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		buttonSend = findViewById(R.id.btn_send);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		btnMore = (Button) findViewById(R.id.btn_more);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		btnMore.setOnClickListener(this);
		backforchat.setOnClickListener(this);
		btn_picture.setOnClickListener(this);
		btn_location.setOnClickListener(this);
		btn_chatroom.setOnClickListener(this);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

		edittext_layout.requestFocus();
		voiceRecorder = new MyVoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
				} else {
					edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ll_btn_container.setVisibility(View.GONE);
				edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				bottomHideLayout.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		initFaceView();
		requstLinken(docId);

		/*if(type==1){
			requstLinken(docId);
		}*/
		//首页item点击进来的
		/*if(type==2){
			fs= (FriendStateItem) bundle.getSerializable("fs");
			getReplyData();
			LogUtils.i("fs");
		}*/

	}
	private PowerManager.WakeLock wakeLock;
	private ImageView voiceCallBtn;
	private void showFace(){
		if (buttonPressToSpeak != null) {
			buttonPressToSpeak.setVisibility(View.GONE);
		}
		ll_btn_container.setVisibility(View.GONE);
		bottomHideLayout.setVisibility(View.VISIBLE);
		faceLayout.setVisibility(View.VISIBLE);
		hideKeyboard();
	}
	/**
	 * 初始化表情数据
	 */
	private void initFaceView() {

		//java.util.Map<Integer, ArrayList<String>> faceData = getFaceDate();
		emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
		emojiconGroupList.add(new EaseEmojiconGroupEntity(com.easemob.easeui.R.drawable.ee_1, Arrays.asList(EaseDefaultEmojiconDatas.getData())));
		faceLayout.init(emojiconGroupList);
		faceLayout.setEmojiconMenuListener(new EaseEmojiconMenuBase.EaseEmojiconMenuListener() {

			@Override
			public void onExpressionClicked(EaseEmojicon emojicon) {
				if (emojicon.getType() != EaseEmojicon.Type.BIG_EXPRESSION) {
					if (emojicon.getEmojiText() != null) {
						mEditTextContent.append(EaseSmileUtils.getSmiledText(PublicActivity.this, emojicon.getEmojiText()));
					}
				} else {
                   /* if (listener != null) {
                        listener.onBigExpressionClicked(emojicon);
                    }*/
				}
			}

			@Override
			public void onDeleteImageClicked() {
				if (!TextUtils.isEmpty(mEditTextContent.getText())) {
					KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
					mEditTextContent.dispatchKeyEvent(event);
				}
			}
		});

	}
	@Override
	public void onClick(View view) {
		switch (view.getId()){

			// 点击显示表情框
			case R.id.iv_emoticons_normal:
				showFace();
				iv_emoticons_normal.setVisibility(View.GONE);
				iv_emoticons_checked.setVisibility(View.VISIBLE);
				break;
			case R.id.iv_emoticons_checked:
				// 点击隐藏表情框
				bottomHideLayout.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.GONE);
				break;
			case R.id.btn_send:
				if(isEditEmply()){		//判断用户是否输入内容
					publishComment(this,1, comment);//发评价
					hideKeyboard();
				}
				break;
			//更多按钮
			case R.id.btn_more:
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.GONE);
				if(ll_btn_container.getVisibility()==View.GONE){
					ll_btn_container.setVisibility(View.VISIBLE);
				}else {
					ll_btn_container.setVisibility(View.GONE);
				}
				if(bottomHideLayout.getVisibility()==View.VISIBLE){
					bottomHideLayout.setVisibility(View.GONE);
				}
				hideKeyboard();
				break;
			//发送文件
			case  R.id.btn_add_file:
				Intent intent=new Intent(PublicActivity.this,FileBrowActivity.class);
				startActivityForResult(intent,REQUSET_FILE_BACK);
				break;
			case R.id.btn_picture:
				intoCimera();
				break;

			case R.id.btn_chatroom:
				startActivityForResult(new Intent(PublicActivity.this, NewGroupActivity.class), 11);
				break;
			case R.id.btn_location:
				startActivityForResult(new Intent(PublicActivity.this, CurrentLocationActivity.class), REQUSET_LOCATION_CURRENT);
				break;
			//返回上个界面
			case R.id.backforchat:
				finish();
				 break;


		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//发送附件
		if(requestCode==REQUSET_FILE_BACK&&resultCode==RESULT_FILE_BACK){
			MyFile myFile= (MyFile) data.getSerializableExtra("file");
			publishComment(this,5,myFile.getFile_Path());

		}
		//系统相机返回信息
		if(requestCode==0x00&&resultCode==RESULT_OK){
			ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
			StringBuilder sb = new StringBuilder();
			for(String p: mSelectPath){
				sb.append(p);
			}
			String imagePath=sb.toString();
			publishComment(this,2,imagePath);
		}
		if(data==null){
			return;
		}
		//定位返回位置名
		if(requestCode==REQUSET_LOCATION_CURRENT&&resultCode==RESULT_LOCATION_CURRENT){
			PoiInfo poiInfo=data.getParcelableExtra("poiInfo");
			if(poiInfo!=null) {
				name = poiInfo.name;
				latitude =  poiInfo.location.latitude;
				longitude = poiInfo.location.longitude;
				publishComment(this,4, name);
			}
		}
		//进入创建聊天室
		if(requestCode==11&&resultCode==RESULT_OK){
			chatroomid = data.getStringExtra("chatroomid");
			String chatroomname = data.getStringExtra("chatroomname");
			LogUtils.i(chatroomname+"--"+chatroomid);
			publishComment(this,6, chatroomname);
		}
	}

	/**
	 * 判断对话框中是否输入内容
	 */
	private boolean isEditEmply(){
		comment = mEditTextContent.getText().toString().trim();
		if(comment.equals("")){
			Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		mEditTextContent.setText("");
		return true;
	}
	/**
	 * 显示语音图标按钮
	 *
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		ll_btn_container.setVisibility(View.GONE);
	}
	private SoundMeter mSensor;
	private long endTime=0;
	/**
	 * 按住说话listener
	 *
	 */
	class PressToSpeakListen implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN:
					long firstTime= System.currentTimeMillis();
					if((firstTime-endTime)<500){
						endTime=firstTime;
						Toast.makeText(PublicActivity.this,"录音时间太短！", Toast.LENGTH_SHORT).show();
						return false;
					}
					endTime=firstTime;
					if (!CommonUtils.isExitsSdcard()) {
						String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
						Toast.makeText(PublicActivity.this, st4, Toast.LENGTH_SHORT).show();
						return false;
					}
					try {
						v.setPressed(true);
						wakeLock.acquire();
						if (EaseChatRowVoicePlayClickListener.isPlaying)
							EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
						recordingContainer.setVisibility(View.VISIBLE);
						recordingHint.setText(getString(R.string.move_up_to_cancel));
						recordingHint.setBackgroundColor(Color.TRANSPARENT);
						Log.i("weitie:","执行");
						voiceRecorder.startRecording(null, fs.getUserId(), getApplicationContext());

					} catch (Exception e) {
						e.printStackTrace();
						v.setPressed(false);
						if (wakeLock.isHeld())
							wakeLock.release();
						if (voiceRecorder != null)
							voiceRecorder.discardRecording();
						recordingContainer.setVisibility(View.INVISIBLE);
						Toast.makeText(PublicActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
						return false;
					}

					return true;
				case MotionEvent.ACTION_MOVE: {
					if (event.getY() < 0) {
						recordingHint.setText(getString(R.string.release_to_cancel));
						recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
					} else {
						recordingHint.setText(getString(R.string.move_up_to_cancel));
						recordingHint.setBackgroundColor(Color.TRANSPARENT);
					}
					return true;
				}
				case MotionEvent.ACTION_UP:
					v.setPressed(false);
					recordingContainer.setVisibility(View.INVISIBLE);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (event.getY() < 0) {
						// discard the recorded audio.
						voiceRecorder.discardRecording();
					} else {
						// stop recording and send voice file
						String st1 = getResources().getString(R.string.Recording_without_permission);
						String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
						String st3 = getResources().getString(R.string.send_failure_please);
						try {
							int length = voiceRecorder.stopRecoding();
							if (length > 0) {


								sendVoice(voiceRecorder.getMp3VoiceFilePath());
							} else if (length == EMError.INVALID_FILE) {
								Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(PublicActivity.this, st3, Toast.LENGTH_SHORT).show();
						}

					}
					return true;
				default:
					recordingContainer.setVisibility(View.INVISIBLE);
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					return false;
			}
		}
	}
	/**
	 * 显示键盘图标
	 *
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		edittext_layout.setVisibility(View.VISIBLE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		ll_btn_container.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 发送语音
	 *
	 * @param filePath
	 */
	private void sendVoice(final String filePath) {
		MyHandler myHandler=new MyHandler(this);
		myHandler.sendMessageDelayed(myHandler.obtainMessage(1, filePath), 1000);
	}

	static class MyHandler extends Handler {
		WeakReference<PublicActivity > mActivityReference;
		MyHandler(PublicActivity activity) {
			mActivityReference= new WeakReference<PublicActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final PublicActivity activity = mActivityReference.get();
			if (activity != null) {
				if(msg.what==1) {
					LogUtils.i(msg.obj.toString());
					publishComment(activity,3, msg.obj.toString());//发评价
				}
			}
		}
	}





	/**
	 * 发表评论
	 */
	private static void publishComment(final PublicActivity activity,int type,String param){
		activity.ll_btn_container.setVisibility(View.GONE);
		activity.pd.setMessage("正在提交...");
		activity.pd.setCanceledOnTouchOutside(false);
		activity.pd.show();
		RequestParams params = new RequestParams("UTF-8");
		LogUtils.i("点到" + activity.fs.getDocid() + "" + activity.fs.getUserId());
		params.addBodyParameter("mainid", activity.fs.getDocid());
		params.addBodyParameter("ispublic", "1");
		params.addBodyParameter("replieruserid",DemoHelper.getInstance().getCurrentUsernName());
		params.addBodyParameter("repliername",Conversion.getInstance().getNickName());
		LogUtils.i("点到" + DemoHelper.getInstance().getCurrentUsernName() + "" + Conversion.getInstance().getNickName());
		params.addBodyParameter("replytime", DateUntils.getEnglishDate(new Date()));
		LogUtils.i("type:" + type + "parmas:" + param);
		//点赞
		if(type==0){
			params.addBodyParameter("body", param);
			//文字
		}else if(type==1){
			params.addBodyParameter("body", param);
			//图片
		}else if(type==2){
			params.addBodyParameter("imageslist",new File(param));
			//语音
		}else if(type==3){
			File file=new File(param);
			if(!file.exists()){
				LogUtils.i("不存在");
			}
			params.addBodyParameter("soundlist", file);
			//地理位置
		}else if(type==4){
			params.addBodyParameter("geopos",activity.longitude+","+activity.latitude);
			params.addBodyParameter("geoaddress",activity.name);
			//附件
		}else if(type==5){
			params.addBodyParameter("doattachmentlist",new File(param));
			//聊天室
		}else if(type==6){
			params.addBodyParameter("chatroomid",activity.chatroomid);
			params.addBodyParameter("chatroomname",param);
		}

		HttpUtils httpUtils=new HttpUtils(5000);
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.PUISH_REPLY, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				activity.pd.dismiss();
				LogUtils.i(responseInfo.result);
				try {
					//解析评论的内容
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					JSONArray ja = jsonObject.optJSONArray("replylist");
					if (ja != null) {
						for (int j = 0; j < ja.length(); j++) {
							JSONObject jo_re = ja.optJSONObject(j);
							ReplyBean rb = new ReplyBean();
							rb.setReplyNickname(jo_re.optString("user"));
							rb.setReply_id(jo_re.optString("replydocid"));
							rb.setReplyAccount(jo_re.optString("userid"));
							rb.setTouxiang(Conversion.getInstance().getHeadUrl());
							Log.i("weitie:", "touxiang:" + Conversion.getInstance().getHeadUrl());
							rb.setTime(jo_re.optString("time"));
							rb.setReplyContent(jo_re.optString("maincontent"));
							rb.setSound(jo_re.optString("sound"));
							rb.setGeopara(jo_re.optString("geopara"));
							rb.setGeopos(jo_re.optString("geopos"));
							rb.setChatroomid(jo_re.optString("chatroomid"));
							rb.setChatroomname(jo_re.optString("chatroomname"));
							rb.setType(jo_re.optString("type"));
							JSONArray Images = jo_re.optJSONArray("images");
							if (Images != null) {
								String[] images = new String[Images.length()];
								for (int k = 0; k < Images.length(); k++) {
									images[k] = Images.optString(k);
								}
								rb.setImages(images);
							}
							JSONArray jsattachments2 = jo_re.optJSONArray("attachments");
							if (jsattachments2 != null) {
								String[] attachments = new String[jsattachments2.length()];
								for (int k = 0; k < jsattachments2.length(); k++) {
									attachments[k] = jsattachments2.optString(k);
								}
								rb.setAttachments(attachments);
							}
							activity.list_reply.add(rb);
						}
					}
					activity.McommentAdapter.DataChange(activity.list_reply,activity.fs);
					activity.dataListView.setSelection(activity.list_reply.size() - 1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				Utils.showToast("发表评论失败", DemoApplication.getInstance());
				activity.pd.dismiss();
			}
		});
	}


	List<ReplyBean> list_reply = new ArrayList<>();

	/**
	 * 获取回复列表数据
	 */
	private void getReplyData() {
		list_reply.clear();
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("mainid", fs.getDocid());
		params.addBodyParameter("userid", fs.getUserId());
		final HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.Post_REPLY, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				feed_circleProgressBar.setVisibility(View.GONE);
				swipeRefreshLayout.setRefreshing(false);
				LogUtils.i(responseInfo.result);
				try {
					//解析评论的内容
					JSONObject jsonObject = new JSONObject(responseInfo.result);
					JSONArray ja = jsonObject.optJSONArray("replylist");
					if (ja != null) {
						for (int j = 0; j < ja.length(); j++) {
							JSONObject jo_re = ja.optJSONObject(j);
							ReplyBean rb = new ReplyBean();
							rb.setReplyNickname(jo_re.optString("user"));
							rb.setReply_id(jo_re.optString("replydocid"));
							rb.setReplyAccount(jo_re.optString("userid"));
							rb.setTouxiang(jo_re.optString("touxiang"));
							rb.setTime(jo_re.optString("time"));
							rb.setReplyContent(jo_re.optString("maincontent"));
							rb.setSound(jo_re.optString("sound"));
							rb.setGeopara(jo_re.optString("geopara"));
							rb.setGeopos(jo_re.optString("geopos"));
							rb.setChatroomid(jo_re.optString("chatroomid"));
							rb.setChatroomname(jo_re.optString("chatroomname"));
							rb.setType(jo_re.optString("type"));
							JSONArray Images = jo_re.optJSONArray("images");
							if (Images != null) {
								String[] images = new String[Images.length()];
								for (int k = 0; k < Images.length(); k++) {
									images[k] = Images.optString(k);
								}
								rb.setImages(images);
							}
							JSONArray jsattachments2 = jo_re.optJSONArray("attachments");
							if (jsattachments2 != null) {
								String[] attachments = new String[jsattachments2.length()];
								for (int k = 0; k < jsattachments2.length(); k++) {
									attachments[k] = jsattachments2.optString(k);
								}
								rb.setAttachments(attachments);
							}
							list_reply.add(rb);
						}
						McommentAdapter.DataChange(list_reply,fs);
					}
					if (list_reply.size() == 0) {
						Utils.showToast("目前暂无评论！", PublicActivity.this);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				feed_circleProgressBar.setVisibility(View.GONE);
				swipeRefreshLayout.setRefreshing(false);
			}
		});
	}
	/**
	 * 调用系统拍照
	 */
	private void intoCimera(){
		Intent intent = new Intent(PublicActivity.this, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
		//Intent intent= CinemaUtil.IntoSystemPic(mContext);
		//mContext.startActivityForResult(intent, 0x00);
		startActivityForResult(intent, 0x00);

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

	ArrayList<FriendStateItem> listReplier;

	/**
	 * 获取事项的内容
	 * @param MainId
	 */
	public void requstLinken(String MainId){
		if(fs!=null){
			getReplyData();
			return;
		}
		feed_circleProgressBar.setVisibility(View.VISIBLE);
		LogUtils.i("MainID:"+MainId);
		listReplier=new ArrayList<>();
		HttpUtils httpUtils = new HttpUtils(5000);
		RequestParams rq=new RequestParams("UTF-8");
		rq.addBodyParameter("linkaddress", MainId);
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.BMOBREPLER, rq, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				listReplier = Json.parseRecommendData(responseInfo.result);
				if(listReplier.size()!=0) {
					fs=listReplier.get(0);
					getReplyData();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				swipeRefreshLayout.setRefreshing(false);
			}
		});

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
