package com.lanxiao.doapp.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.core.PoiInfo;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.domain.EaseEmojicon;
import com.easemob.easeui.domain.EaseEmojiconGroupEntity;
import com.easemob.easeui.model.EaseDefaultEmojiconDatas;
import com.easemob.easeui.utils.EaseSmileUtils;
import com.easemob.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.easemob.easeui.widget.emojicon.EaseEmojiconMenu;
import com.easemob.easeui.widget.emojicon.EaseEmojiconMenuBase;
import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiImageSelectorActivity;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.CommonUtils;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.Result;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.untils.CinemaUtil;
import com.lanxiao.doapp.untils.DateTimePickDialogUtil;
import com.lanxiao.doapp.untils.DateUntils;
import com.lanxiao.doapp.untils.util.FileUtils;
import com.lanxiao.doapp.untils.util.MyVoiceRecorder;
import com.lanxiao.doapp.untils.util.Utils;
import com.lanxiao.doapp.entity.MyFile;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 工作的输入界面
 * Created by Thinkpad on 2015/11/3.
 */
public class InputActivity extends BaseActivity implements View.OnClickListener {
    FriendStateItem friendStateItem;
    LinearLayout ll,ll_inputcontent_backLoationContent;
    ImageView ib_taketawore, ib_dobyord,iv_location,iv_reming;
    ImageView iv_voice, iv_voice_pressvoice, iv_input_cinmea, iv_smile,iv_public
            ,iv_inputMessage_voiceanmion,iv_inputmessage_Pic,iv_inputmessage_deledepic,iv_sum,iv_pinadd;
    RelativeLayout rl_inputmessage_showvoice,rl_inputmessage_showpic,newsfeedpublish_poi_list,iv_inputMessage_voiceanmion_delete;
    View rl_voice_press;
    EditText et_input_text;
    TextView newsfeedpublish_poi_place,tv_input_tijiao;
    TextView tv_inputmessage_time;
    GridView gridView1;
    InputGridViewAdapter inputGridViewAdapter;
    private ImageView  iv_pic;
    EaseEmojiconMenu faceLayout;
    ViewPager fuctionViewPager;
    LinearLayout pagePointLayout;
    RelativeLayout bottomHideLayout;
    private List<String> reslist;
    File file;
    List<String> datas;//存放图片的地址
    String lastImaegePath;
    String dataTime;//设定的end time
    String time;//现在的时间
    int j=0;
    MyFile myFile;
    AlertDialog mAlertDialog;
    String url= Api.POST_DO_SUBMIT;//提交成功返回的json
    private SharedPreferences sp;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private String userid;
    private MyVoiceRecorder voiceRecorder;
    private int jumpType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTranslucentStatus();
        setContentView(R.layout.input_activity);
        init();

    }

    private void init() {
        datas=new ArrayList<>();
        tv_input_tijiao= (TextView) findViewById(R.id.tv_input_tijiao);
        userid= EMChatManager.getInstance().getCurrentUser();
        sp=getSharedPreferences("config",MODE_PRIVATE);
        inputGridViewAdapter=new InputGridViewAdapter();
        gridView1= (GridView) findViewById(R.id.gridView1);
        gridView1.setAdapter(inputGridViewAdapter);
        View view=getLayoutInflater().inflate(R.layout.submit_alertdiaog, null);
        mAlertDialog=new AlertDialog.Builder(this).setView(view).create();
        bottomHideLayout = (RelativeLayout) findViewById(R.id.bottomHideLayout1);
        faceLayout = (EaseEmojiconMenu) findViewById(R.id.faceLayout1);
        //faceCategroyViewPager = (ViewPager) findViewById(R.id.faceCategroyViewPager1);
        fuctionViewPager = (ViewPager) findViewById(R.id.fuctionViewPager1);
        pagePointLayout = (LinearLayout) findViewById(R.id.pagePointLayout1);
        //iv_sum= (ImageView) findViewById(R.id.iv_sum);
        friendStateItem=new FriendStateItem();
        //iv_pinadd= (ImageView) findViewById(R.id.iv_pinadd);
       // ll_btn_container= (LinearLayout) findViewById(R.id.ll_btn_container);
        tv_inputmessage_time= (TextView) findViewById(R.id.tv_inputmessage_time);
        iv_reming= (ImageView) findViewById(R.id.iv_reming);
        newsfeedpublish_poi_place= (TextView) findViewById(R.id.newsfeedpublish_poi_place);
        ll_inputcontent_backLoationContent= (LinearLayout) findViewById(R.id.ll_inputcontent_backLoationContent);
        iv_location= (ImageView) findViewById(R.id.iv_location);
        iv_public= (ImageView) findViewById(R.id.iv_public);
        newsfeedpublish_poi_list= (RelativeLayout) findViewById(R.id.newsfeedpublish_poi_list);
        newsfeedpublish_poi_list.setOnClickListener(this);
        rl_inputmessage_showpic= (RelativeLayout) findViewById(R.id.rl_inputmessage_showpic);
        rl_inputmessage_showvoice= (RelativeLayout) findViewById(R.id.rl_inputmessage_showvoice);
        iv_inputMessage_voiceanmion= (ImageView) findViewById(R.id.iv_inputMessage_voiceanmion);
        iv_voice_pressvoice = (ImageView) findViewById(R.id.iv_voice_pressvoice);
        ib_taketawore = (ImageView) findViewById(R.id.ib_taketawore);
        ib_dobyord = (ImageView) findViewById(R.id.ib_dobyord);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_pic = (ImageView) findViewById(R.id.iv_pic);
        iv_smile = (ImageView) findViewById(R.id.iv_smile);
        iv_input_cinmea = (ImageView) findViewById(R.id.iv_input_cinmea);
        et_input_text = (EditText) findViewById(R.id.et_input);
        rl_voice_press = findViewById(R.id.rl_voice_press);
        iv_inputMessage_voiceanmion_delete= (RelativeLayout) findViewById(R.id.iv_inputMessage_voiceanmion_delete);
        iv_inputMessage_voiceanmion_delete.setOnClickListener(this);
        rl_voice_press.setOnTouchListener(new PressToSpeakListen());
        ib_taketawore.setOnClickListener(this);
        iv_voice.setOnClickListener(this);
        iv_pic.setOnClickListener(this);
        ib_dobyord.setOnClickListener(this);
        iv_smile.setOnClickListener(this);
        iv_input_cinmea.setOnClickListener(this);
        tv_inputmessage_time.setOnClickListener(this);
        iv_public.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        iv_reming.setOnClickListener(this);
        ll_inputcontent_backLoationContent.setOnClickListener(this);
        tv_inputmessage_time.setOnClickListener(this);
   //     iv_sum.setOnClickListener(this);
        et_input_text.setOnClickListener(this);
     //   iv_pinadd.setOnClickListener(this);
        tv_input_tijiao.setOnClickListener(this);
        rl_inputmessage_showvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.playMusic(file.getPath(), iv_inputMessage_voiceanmion,getApplicationContext());//播放录音
            }
        });
        initFaceView();
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        voiceRecorder=new MyVoiceRecorder(micImageHandler);
        mSelectPath=getIntent().getStringArrayListExtra("imagePath");
        jumpType=getIntent().getIntExtra("type",0);
        //more选择相机跳转过来的
        if(mSelectPath!=null){
            ib_taketawore.setVisibility(View.GONE);
            ib_dobyord.setVisibility(View.GONE);
            tv_input_tijiao.setVisibility(View.VISIBLE);
            LogUtils.i("mSelectPath:"+mSelectPath.size());
            for(int i=0;i<mSelectPath.size();i++){
                datas.add(mSelectPath.get(i));
            }
            if(gridView1.getVisibility()==View.GONE) {
                gridView1.setVisibility(View.VISIBLE);
            }
            inputGridViewAdapter.notifyDataSetChanged();
        }
        //more选择文字跳转过来的
        if(jumpType==1){
            ib_taketawore.setVisibility(View.GONE);
            ib_dobyord.setVisibility(View.GONE);
            tv_input_tijiao.setVisibility(View.VISIBLE);
        }
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intoSpaceImageDetailActivity(datas,position);
            }
        });
    }
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //交他做
            case R.id.ib_taketawore:
               sendToTaDo();

                break;
            case R.id.iv_voice:
                //开始录音
                startVoice();
                break;
            case R.id.ib_dobyord:
                //自己做
                dobyself(userid,"1");
                break;
            case R.id.iv_pic:
                //获得系统拍照
                getSystemPic();
                break;
            case R.id.iv_input_cinmea:
                Intent intent1 = CinemaUtil.IntoSystemPic(this);//进入系统拍照
                startActivityForResult(intent1, REQUST_OPEN_SYSTEM_CINEMA);
                break;
            case R.id.iv_smile:
                //表情
                showFace();
                break;


            case R.id.iv_public:
                //公开，好友圈，仅自己可见按
                if(iv_public.isSelected()){
                    iv_public.setSelected(false);
                    j=1;
                }else {
                    iv_public.setSelected(true);
                    j=0;
                }
                    break;
            case R.id.iv_location:
                //进入附近位置
               startActivityForResult(new Intent(InputActivity.this, CurrentLocationActivity.class), REQUSET_LOCATION_CURRENT);
                break;
            case R.id.ll_inputcontent_backLoationContent:
                Intent intent=new Intent(InputActivity.this, CurrentLocationActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("poiInfo", poiInfo);
                intent.putExtras(bundle);
                //点击返回定位界面
                startActivityForResult(intent, REQUSET_LOCATION_CURRENT);
                break;
            case R.id.tv_inputmessage_time:
                //设置提醒时间
                if(TextUtils.isEmpty(tv_inputmessage_time.getText())){
                    time= DateUntils.getChineseDate(new Date());
                }else {
                    Date date=DateUntils.getEndDate(tv_inputmessage_time.getText().toString());
                    time=DateUntils.getChineseDate(date);
                }
                showtimeView(time);
                break;
            case R.id.iv_reming:
                //设置提醒时间
                if(TextUtils.isEmpty(tv_inputmessage_time.getText())){
                    time= DateUntils.getChineseDate(new Date());
                }else {
                    Date date=DateUntils.getEndDate(tv_inputmessage_time.getText().toString());
                    time=DateUntils.getChineseDate(date);
                }
                showtimeView(time);
                break;
            case R.id.et_input:
                rl_voice_press.setVisibility(View.GONE);
                bottomHideLayout.setVisibility(View.GONE);
               // ll_btn_container.setVisibility(View.GONE);
                break;
          /*  case R.id.iv_sum:
                //多功能键
                showSum();
                break;
            case R.id.iv_pinadd:
                //选择文件上传
                seletFile();
                break;*/

            case R.id.tv_input_tijiao:
                dobyself(userid,"1");
                break;
            //删除地址
            case R.id.newsfeedpublish_poi_list:
                name=null;
                ll_inputcontent_backLoationContent.setVisibility(View.GONE);
                iv_location.setVisibility(View.VISIBLE);
                break;
            //删除语音
            case R.id.iv_inputMessage_voiceanmion_delete:
                file=null;
                rl_inputmessage_showvoice.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 选择文件
     */
    private void seletFile() {
        Intent intent=new Intent(InputActivity.this,FileBrowActivity.class);
        startActivityForResult(intent, REQUSET_INUPUT_FILE_BACK);
    }

    /**
     * 显示多功能键
     */
    private void showSum() {
       /* if(ll_btn_container.getVisibility()==View.VISIBLE) {
            ll_btn_container.setVisibility(View.GONE);
        }else {
            ll_btn_container.setVisibility(View.VISIBLE);
        }*/
        faceLayout.setVisibility(View.GONE);
        rl_voice_press.setVisibility(View.GONE);
        bottomHideLayout.setVisibility(View.GONE);
        hideKeyboard();
    }
    List<EaseEmojiconGroupEntity> emojiconGroupList;
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
                        et_input_text.append(EaseSmileUtils.getSmiledText(InputActivity.this, emojicon.getEmojiText()));
                    }
                } else {
                   /* if (listener != null) {
                        listener.onBigExpressionClicked(emojicon);
                    }*/
                }
            }

            @Override
            public void onDeleteImageClicked() {
                if (!TextUtils.isEmpty(et_input_text.getText())) {
                    KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                    et_input_text.dispatchKeyEvent(event);
                }
            }
        });

    }
    private void showFace(){
        if(bottomHideLayout.getVisibility()==View.VISIBLE){
            bottomHideLayout.setVisibility(View.GONE);
        }else {
            bottomHideLayout.setVisibility(View.VISIBLE);
        }
        if (rl_voice_press != null) {
            rl_voice_press.setVisibility(View.GONE);
        }
        hideKeyboard();
    }

    /**
     * 获取系统相册资源
     */
    private void getSystemPic(){
       /* Intent intent=new Intent(Intent.ACTION_PICK);
       // intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image*//*");
        //intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUST_OPEN_SYSTEM_PIC);*/
        Intent intent = new Intent(InputActivity.this, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        //Intent intent= CinemaUtil.IntoSystemPic(mContext);
        //mContext.startActivityForResult(intent, 0x00);
        startActivityForResult(intent, REQUST_OPEN_SYSTEM_PIC);
    }


    private ArrayList<String> mSelectPath;
    String name="";
    private PoiInfo poiInfo;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //访问系统相册的返回信息
        if(requestCode==REQUST_OPEN_SYSTEM_PIC&&resultCode==RESULT_OK){
            mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            for(int i=0;i<mSelectPath.size();i++){
                datas.add(mSelectPath.get(i));
            }
            if(gridView1.getVisibility()==View.GONE) {
                gridView1.setVisibility(View.VISIBLE);
            }
            inputGridViewAdapter.notifyDataSetChanged();

        }
        //调用系统相机返回的信息
        if(requestCode==REQUST_OPEN_SYSTEM_CINEMA&&resultCode==RESULT_OK){
            File file=CinemaUtil.getFile();
            lastImaegePath=file.getPath();
            if(gridView1.getVisibility()==View.GONE) {
                gridView1.setVisibility(View.VISIBLE);
            }
            datas.add(lastImaegePath);
            inputGridViewAdapter.notifyDataSetChanged();
        }
        if(data==null){

            return;
        }
        //定位返回位置名
        if(requestCode==REQUSET_LOCATION_CURRENT&&resultCode==RESULT_LOCATION_CURRENT){
            poiInfo=data.getParcelableExtra("poiInfo");
            name= poiInfo.name;
            latitude = poiInfo.location.latitude;
            longitude = poiInfo.location.longitude;
            address= poiInfo.address;
            if(name!=null) {
                iv_location.setVisibility(View.GONE);
                ll_inputcontent_backLoationContent.setVisibility(View.VISIBLE);
                newsfeedpublish_poi_place.setText(name);
            }
        }
        if(data==null){
            return;
        }
        //有附件
      /*  if(requestCode==REQUSET_INUPUT_FILE_BACK&&resultCode==RESULT_FILE_BACK){
            myFile= (MyFile) data.getSerializableExtra("file");
            //todo
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.file_icon_default);
            datas.add(myFile.getFile_Path());
            if(gridView1.getVisibility()==View.GONE) {
                gridView1.setVisibility(View.VISIBLE);
            }
            inputGridViewAdapter.setData();
        }*/

    }
    double latitude;
    double longitude;
    String address;
    /**
     * 提交输入内容
     */
    private void sendToTaDo() {
        String input=et_input_text.getText().toString();
        if(TextUtils.isEmpty(input)&&TextUtils.isEmpty(name)&&file == null&&
                datas.size() == 0){
            Utils.showToast("提交内容不能为空", this);
            return;
        }
        if(!TextUtils.isEmpty(input)){
            friendStateItem.setContent(input);
        }
        if(!TextUtils.isEmpty(tv_inputmessage_time.getText().toString())){
            LogUtils.i(tv_inputmessage_time.getText().toString());
            friendStateItem.setEndTime(tv_inputmessage_time.getText().toString());
        }
        friendStateItem.setType(j+"");
        if(!TextUtils.isEmpty(name)){
            friendStateItem.setLocationName(name);
            friendStateItem.setLatitude(latitude + "");
            friendStateItem.setLongitude(longitude+"");
        }
        if (file != null) {
            friendStateItem.setVideoUri(file.getPath());

        }
        if (datas.size() != 0) {
           friendStateItem.setImages(datas.toArray(new String[datas.size()]));
        }
        Intent intent = new Intent(InputActivity.this, LocatContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("fs",friendStateItem);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    /**
     * 提交输入内容
     * @param handlerid
     * @param type
     */
    private void dobyself(String handlerid, final String type) {
        String input=et_input_text.getText().toString();
        if(TextUtils.isEmpty(input)&&TextUtils.isEmpty(name)&&file == null&&
                datas.size() == 0){
            Utils.showToast("提交内容不能为空",this);
            return;
        }
        if(mSelectPath!=null||jumpType==1){
            url=url+"?submittype=1";
            LogUtils.i(url);
        }
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("提交中....");
        pd.show();
        String content = "";
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid",userid);
        params.addBodyParameter("handlerid", handlerid);
        params.addBodyParameter("type", type);
        LogUtils.i("userid:" + userid);
        LogUtils.i("handlerid:"+handlerid);
        LogUtils.i("type:"+type);
        if(!TextUtils.isEmpty(tv_inputmessage_time.getText().toString())){
            LogUtils.i(tv_inputmessage_time.getText().toString());
            params.addBodyParameter("Alerttime", tv_inputmessage_time.getText().toString());
        }
        if(isPublic){
            LogUtils.i("isPublic");
            params.addBodyParameter("Ispublic","1");
        }
        if (!TextUtils.isEmpty(et_input_text.getText().toString())) {
            content = et_input_text.getText().toString();
            params.addBodyParameter(Json.LASTCONTENT, content);
        }
        String voiceUrl = null;
        if(!TextUtils.isEmpty(name)){
            params.addBodyParameter("geopos",name);
            params.addBodyParameter("geopara", longitude + "," + latitude);
            LogUtils.i("latitude:" + latitude + ",longitude:" + longitude + ",address:" + name);
        }
        if (file != null) {
            voiceUrl = file.getPath();
            LogUtils.i(voiceUrl);
            params.addBodyParameter("soundlist", file);
        }
        if (datas.size() != 0) {
            LogUtils.i(datas.size() + "");
            for (int i=0;i<datas.size();i++){
                params.addBodyParameter(Json.IMAGES+i, new File(datas.get(i)));
               /* if(myFile!=null&& !FileUtils.isImage(datas.get(i))){
                    LogUtils.i(myFile.getFile_Path());
                    params.addBodyParameter("doattachmentlist",new File(datas.get(i)));
                }*/
            }
        }

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params,new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.i(arg0.getMessage()+":"+arg1);
                pd.dismiss();
                Utils.showToast("提交失败",getApplicationContext());
                return;
            }
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                Result result= Json.getResult(responseInfo.result);
                pd.dismiss();
                if(result.getResult().equals("0")) {
                    if (type.equals("3")) {
                        StringBuffer sb = new StringBuffer("http://dosns.net/modules/doinglist/Mainform.aspx?opr=opendocument&id=");
                        sb.append(result.getBodyvalue());
                        DemoApplication.getInstance().setBodyvalue(result.getBodyvalue());
                        wechatShare(et_input_text.getText().toString(),0, sb.toString());
                        LogUtils.i("wechatShare");
                    }else{
                        Intent intent = new Intent(InputActivity.this, MainActivity.class);
                        intent.putExtra("type",2);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }
    /**
     * 录音按钮显示
     */
    private void startVoice(){
       // ll_btn_container.setVisibility(View.GONE);
        if(bottomHideLayout!=null){
            bottomHideLayout.setVisibility(View.GONE);
        }
        if(rl_voice_press.getVisibility()==View.VISIBLE){
            rl_voice_press.setVisibility(View.GONE);
        }else {
            rl_voice_press.setVisibility(View.VISIBLE);
        }

        hideKeyboard();
    }
    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private PowerManager.WakeLock wakeLock;
    private static final int POLL_INTERVAL = 300;
    private long endTime=0;
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v,MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    long firstTime= System.currentTimeMillis();
                    if((firstTime-endTime)<500){
                        endTime=firstTime;
                        Toast.makeText(InputActivity.this,"录音时间太短！", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    endTime=firstTime;
                    if (!CommonUtils.isExitsSdcard()) {
                        String st4 = getResources().getString(R.string.Send_voice_need_sdcard_support);
                        Toast.makeText(InputActivity.this, st4, Toast.LENGTH_SHORT).show();
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
                        Log.i("weitie:", "执行");
                        voiceRecorder.startRecording(null, DemoHelper.getInstance().getCurrentUsernName(), getApplicationContext());

                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(InputActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
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
                    if (event.getY() < 0) {
                        LogUtils.i("1");
                        // discard the recorded audio.
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder.isRecording()) {
                            voiceRecorder.discardRecording();
                            recordingContainer.setVisibility(View.INVISIBLE);
                            LogUtils.i("3");
                        }
                    } else {
                        LogUtils.i("2");
                        // stop recording and send voice file
                        String st1 = getResources().getString(R.string.Recording_without_permission);
                        String st2 = getResources().getString(R.string.The_recording_time_is_too_short);
                        String st3 = getResources().getString(R.string.send_failure_please);
                        try {
                            if (wakeLock.isHeld())
                                wakeLock.release();
                            LogUtils.i("4");
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                file=new File(voiceRecorder.getMp3VoiceFilePath());
                                rl_inputmessage_showvoice.setVisibility(View.VISIBLE);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(InputActivity.this, st1, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(InputActivity.this, st2, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(InputActivity.this, st3, Toast.LENGTH_SHORT).show();
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
    Boolean isPublic=false;


    /**
     * 显示选择日期对话框
     * @param time1
     */
    private void showtimeView(String time1){
        DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                InputActivity.this,time1);
        dateTimePicKDialog.dateTimePicKDialog(tv_inputmessage_time,iv_reming);
    }

    public void back(View v){
        finish();
    }

    public class InputGridViewAdapter extends BaseAdapter {
        public InputGridViewAdapter(){
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view= getLayoutInflater().inflate(R.layout.gridview_input_item,viewGroup,false);
            ImageView iv_inputmessage_Pic= (ImageView) view.findViewById(R.id.iv_inputmessage_Pic);
            RelativeLayout iv_inputmessage_deledepic= (RelativeLayout) view.findViewById(R.id.iv_inputmessage_deledepic);
            Utils.loadLocatalImage(datas.get(i),iv_inputmessage_Pic);
            iv_inputmessage_deledepic.setOnClickListener(new MyOnClickListenrer(i));

            return view;
        }
        private class MyOnClickListenrer implements View.OnClickListener{
            int i;
            public MyOnClickListenrer(int i){
                this.i=i;
            }
            @Override
            public void onClick(View view) {
                datas.remove(i);
                notifyDataSetChanged();
            }
        }
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
    /**
     * 图片查看器
     */
    private void intoSpaceImageDetailActivity(List<String> datas, int position) {
        Log.i("weitie", "点击查看大图");
        Intent intent = new Intent(InputActivity.this, ImagePagerActivity.class);
        intent.putExtra("image_urls", (ArrayList)datas);//非必须
        intent.putExtra("image_index", position);
        startActivity(intent);
    }
}
