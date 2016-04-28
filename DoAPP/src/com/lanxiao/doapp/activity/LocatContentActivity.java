package com.lanxiao.doapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.ContantTongShi;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.domain.FristLevelContantTongShi;
import com.easemob.easeui.widget.EaseSidebar;
import com.example.doapp.R;
import com.lanxiao.doapp.adapter.MyExpandableListViewAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.entity.Result;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.SegmentView;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送中的朋友联系人列表
 */
public class LocatContentActivity extends BaseActivity {


    private ListView contactsListView;
    private ImageView iv_repate_back,img_weixin;
    private List<EaseUser> list = new ArrayList<EaseUser>();
    private List<List<EaseUser>> list_tongshi = new ArrayList<>();
    private List<FristLevelContantTongShi> toshilist = new ArrayList<>();
    private List<ContantTongShi> sumlist = new ArrayList<>();
    private TextView tv_repater_titile,tv_show_nocontant,tv_queding;
    /**
     * 是否为单选
     */
    private EditText et_reater_content;
    private PickContactAdapter contactAdapter;
    EaseSidebar es;
    private Button btn_search_btn_ok;
    private String MainId;
    private String type=null;
    private FriendStateItem fs;
    private ImageView tv_ok;
    private String bodyValue=null;
    private boolean firstSend=true;//是否没有发送过
    private HorizontalScrollView horizontalscrlooview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locatponeactivity);
        init();
        getContact();
        initView();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择本地朋友发送
                    if(totalList.size()==0){
                        Utils.showToast("请选择联系人",DemoApplication.getInstance());
                        return;
                    }
                    StringBuffer sb = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                for (int i=0;i<totalList.size();i++){
                        if(i==totalList.size()-1){
                            sb.append(totalList.get(i).getUserId());
                            sb2.append(totalList.get(i).getNickName());
                            break;
                        }
                        sb.append(totalList.get(i).getUserId()+",");
                         sb2.append(totalList.get(i).getNickName()+",");
                }

                    LogUtils.i(sb.toString());
                    LogUtils.i(sb2.toString());
                    //do里面的交他做
                    if(MainId==null) {
                       /* intent.putExtra("handlerid", sb.toString());
                        intent.putExtra("targetusername", sb2.toString());
                        intent.putExtra("type", "2");*/
                        dobyself(sb.toString(),"2");
                    }else {
                        //首项中的转发
                        Intent intent = new Intent();
                        intent.putExtra("handlerid", sb.toString());
                        intent.putExtra("targetusername", sb2.toString());
                        intent.putExtra("mainid",MainId);
                        intent.putExtra("body", et_reater_content.getText().toString().trim());
                        intent.putExtra("type", "1");
                        replier(sb.toString(),sb2.toString(),et_reater_content.getText().toString().trim(),"1",MainId);
                    }


            }
        });
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.toggle();
                if(checkBox.isChecked()){
                    totalList.add(list.get(i-1));
                }else {
                    totalList.remove(list.get(i-1));
                }
                drawChild();
            }
        });
        //会议选择参加人时返回的
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalList.size()==0){
                    Utils.showToast("请选择联系人",DemoApplication.getInstance());
                    return;
                }
                StringBuffer sb = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                for (int i=0;i<totalList.size();i++){
                    if(i==totalList.size()-1){
                        sb.append(totalList.get(i).getUserId());
                        sb2.append(totalList.get(i).getNickName());
                        break;
                    }
                    sb.append(totalList.get(i).getUserId()+",");
                    sb2.append(totalList.get(i).getNickName()+",");
                }
                LogUtils.i(sb.toString());
                LogUtils.i(sb2.toString());
                Intent intent=new Intent();
                  intent.putExtra("handlerid", sb.toString());
                intent.putExtra("targetusername", sb2.toString());
                setResult(LOCATION_RESULT_SEND, intent);
                finish();
            }
        });
        iv_repate_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstSend) {
                    Intent intent = new Intent(LocatContentActivity.this, MainActivity.class);
                    intent.putExtra("type", 2);
                    startActivity(intent);
                    finish();
                }else {
                    finish();
                }
            }
        });
        img_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择微信朋友发送转发
                replier("", "", et_reater_content.getText().toString().trim(), "2", MainId);
            }
        });
    }
    private SegmentView mSegmentView;
    private MyExpandableListViewAdapter adapter;
    private List<EaseUser> totalList=new ArrayList<>();
    /** 两个按钮切换 */
    private void initView() {
        horizontalscrlooview= (HorizontalScrollView) findViewById(R.id.horizontalscrlooview);
        ll_container_tongshi= (LinearLayout) findViewById(R.id.ll_container_tongshi);
        mSegmentView.setSegmentText("好友", 0);
        mSegmentView.setSegmentText("同事", 1);
        mSegmentView.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                switch (position) {
                    case 0:
                        contactsListView.setVisibility(View.VISIBLE);
                        expandableListView.setVisibility(View.GONE);
                        break;
                    case 1:
                        contactsListView.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }
        });
        expandableListView.setGroupIndicator(null);

        // 监听组点击
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
               /* if (toshi.get(groupPosition).isEmpty())
                {
                    return true;
                }*/
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(adapter.getChildType(groupPosition,childPosition)==1){
                    ContantTongShi ct=sumContantTongShi.get(groupPosition).get(childPosition);
                    startActivityForResult(new Intent(LocatContentActivity.this, ShowContantActivity.class)
                            .putExtra("title", ct.getName()).putExtra("id",ct.getId()), 0x22);
                }else {
                    int p=childPosition-sumContantTongShi.get(groupPosition).size();
                    CheckBox cb= (CheckBox) v.findViewById(R.id.checkbox);
                    cb.toggle();
                    if(cb.isChecked()){
                        if(!isSame(sumToshi.get(groupPosition).get(p).getUserId())){
                            totalList.add(sumToshi.get(groupPosition).get(p));
                        }else {
                            Utils.showToast("已选择了此联系人!",DemoApplication.getInstance());
                            cb.setChecked(false);
                        }
                    }else {
                        if(isSame(sumToshi.get(groupPosition).get(p).getUserId())){
                            totalList.remove(sumToshi.get(groupPosition).get(p));
                        }
                    }
                    drawChild();
                }


                return false;
            }
        });

        adapter = new MyExpandableListViewAdapter(this,first,sumToshi,sumContantTongShi);

        expandableListView.setAdapter(adapter);
    }
    private void init() {
        mSegmentView = (SegmentView) findViewById(R.id.segment_view);
        View v=LayoutInflater.from(this).inflate(R.layout.em_widget_contact_item,null);
        ImageView aver= (ImageView) v.findViewById(R.id.avatar);
        TextView name= (TextView) v.findViewById(R.id.name);
        aver.setImageResource(R.drawable.image_weixin);
        name.setText("微信朋友");
        //点击微信朋友
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择微信朋友发送
                if (MainId == null) {
                    doByWeixin(3 + "");
                } else {
                    //选择微信朋友发送转发
                    replier("", "", et_reater_content.getText().toString().trim(), "2", MainId);
                }
            }
        });
        fs= (FriendStateItem) getIntent().getSerializableExtra("fs");
        LogUtils.i((fs == null) + "");
        img_weixin= (ImageView) findViewById(R.id.img_weixin);
        tv_queding= (TextView) findViewById(R.id.tv_queding);
        tv_ok= (ImageView) findViewById(R.id.tv_ok);
        tv_show_nocontant= (TextView) findViewById(R.id.tv_show_nocontant);
        iv_repate_back= (ImageView) findViewById(R.id.iv_repate_back);
        tv_repater_titile= (TextView) findViewById(R.id.tv_repater_titile);
        btn_search_btn_ok= (Button) findViewById(R.id.btn_search_btn_ok);
        et_reater_content= (EditText) findViewById(R.id.et_reater_content);
        contactsListView = (ListView) findViewById(R.id.contacts_list_view);
        expandableListView = (ExpandableListView)findViewById(R.id.expendlist);
        expandableListView.addHeaderView(v);
        contactsListView.addHeaderView(v);
        es= (EaseSidebar) findViewById(R.id.sidebar);
        es.setListView(contactsListView);
        //点击跳转到添加联系人界面
        tv_show_nocontant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocatContentActivity.this, AddContactActivity.class));
                finish();
            }
        });
        type=getIntent().getStringExtra("type");
        MainId=getIntent().getStringExtra("mainid");
        LogUtils.i("Loctation:" + MainId);
        if(type!=null){
            //发布会议选择成员访问过来的
            if(type.equals("4")){
                tv_repater_titile.setText("选择成员");
                tv_ok.setVisibility(View.GONE);
                tv_queding.setVisibility(View.VISIBLE);
                return;
            }
            //tv_repater_titile.setVisibility(View.VISIBLE);
            //任务和会议类型转必过来的
            if(type.equals(FriendStateItem.TASK)||type.equals(FriendStateItem.MEETING)){
            et_reater_content.setVisibility(View.VISIBLE);
                //微博类型转必过来的
            }else if(type.equals(FriendStateItem.BLOG)){
                tv_repater_titile.setText("转发微博");
                et_reater_content.setHint("说点什么");
                et_reater_content.setVisibility(View.VISIBLE);
                btn_search_btn_ok.setVisibility(View.VISIBLE);
                mSegmentView.setVisibility(View.GONE);
                es.setVisibility(View.GONE);
                contactsListView.setVisibility(View.GONE);
                tv_ok.setVisibility(View.GONE);
                img_weixin.setVisibility(View.VISIBLE);
                //微博里面的转
                btn_search_btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replier("","",et_reater_content.getText().toString().trim(),"0",MainId);
                    }
                });
            }
        }

    }
    List<String> first=new ArrayList<>();
    List<List<EaseUser>> sumToshi=new ArrayList<>();
    List<List<ContantTongShi>> sumContantTongShi=new ArrayList<>();
    private ExpandableListView expandableListView;

    /**
     * 获取本地联系人的数据
     */
    private void getContact() {
        try {
            list=DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("whosfriend", "=", DemoHelper.getInstance().getCurrentUsernName()));
            toshilist=DemoApplication.getInstance().getDb().findAll(Selector.from(FristLevelContantTongShi.class));
            LogUtils.i("toshilist:"+totalList.size());
            if(toshilist==null){
                Utils.showToast("暂无同事,请查看是否加入公司",DemoApplication.getInstance());
            }else {
                for(FristLevelContantTongShi tc:toshilist){
                    first.add(tc.getName());
                    List<EaseUser> alluser11 = DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("company_id", "=", tc.getId()));
                    LogUtils.i("alluser:"+alluser11.size());
                    if(alluser11!=null){
                        sumToshi.add(alluser11);
                    }else {
                        alluser11=new ArrayList<>();
                        sumToshi.add(alluser11);
                    }
                    List<ContantTongShi> all = DemoApplication.getInstance().getDb().findAll(Selector.from(ContantTongShi.class).where("tongshi_id", "=", tc.getId()));
                    LogUtils.i("all:"+all.size());
                    if(all!=null){
                        sumContantTongShi.add(all);
                    }else {
                        all=new ArrayList<>();
                        sumContantTongShi.add(all);
                    }

                }
            }
            if(list==null){
                LogUtils.i("list");
                list=new ArrayList<>();
                tv_show_nocontant.setVisibility(View.VISIBLE);
                contactsListView.setVisibility(View.GONE);
            }else{
                if(list.size()==0){
                    LogUtils.i("list0");
                    tv_show_nocontant.setVisibility(View.VISIBLE);
                    contactsListView.setVisibility(View.GONE);
                }
            }
            // 排序
            Collections.sort(list, new Comparator<EaseUser>() {

                @Override
                public int compare(EaseUser lhs, EaseUser rhs) {
                    // 获取ascii值
                    int lhs_ascii = lhs.getInitialLetter().toUpperCase().charAt(0);
                    int rhs_ascii = rhs.getInitialLetter().toUpperCase().charAt(0);
                    // 判断若不是字母，则排在字母之后
                    if (lhs_ascii < 65 || lhs_ascii > 90)
                        return 1;
                    else if (rhs_ascii < 65 || rhs_ascii > 90)
                        return -1;
                    else
                        return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }
            });
            contactAdapter = new PickContactAdapter(getApplicationContext(), R.layout.row_contact_with_checkbox, list);
            contactsListView.setAdapter(contactAdapter);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    List<String> nickNames = new ArrayList<String>();
    /**
     * 获取要需要发送的成员
     *
     * @return
     */
   /* private List<String> getToBeAddMembers() {
        nickNames.clear();
        List<String> members = new ArrayList<String>();
        int length = contactAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++) {
            String username = contactAdapter.getItem(i).getUsername();
            String nickname=contactAdapter.getItem(i).getNickName();
            LogUtils.i(username);
            if (contactAdapter.isCheckedArray[i]) {
                members.add(username);
                nickNames.add(nickname);
            }
        }

        return members;
    }*/

    /**
     * adapter
     */
    private class PickContactAdapter extends EaseContactAdapter {

       // private boolean[] isCheckedArray;
        private Map<String,Boolean> map;
        public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
            super(context, resource, users);
            //isCheckedArray = new boolean[users.size()];
            map=new HashMap<>();
            for(EaseUser eu:users){
                map.put(eu.getUserId(),false);
            }
        }
        public Map<String,Boolean> getMap(){
            return map;
        }
        public void isClear(){
           /* if(isCheckedArray!=null){
                for(int i=0;i<isCheckedArray.length;i++){
                    isCheckedArray[i]=false;
                    notifyDataSetChanged();
                }
            }*/
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
//			if (position > 0) {
            final String username = getItem(position).getUsername();
            // 选择框checkbox
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
            TextView nameView = (TextView) view.findViewById(R.id.name);
            Utils.setAver(list.get(position).getAvatar(),avatarView);
            nameView.setText(list.get(position).getNickName());
            if (checkBox != null) {
                checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            //isCheckedArray[position]=isChecked;
                        map.put(list.get(position).getUserId(),isChecked);
                    }
                });

            }
            checkBox.setChecked(map.get(list.get(position).getUserId()));
            checkBox.setClickable(false);
            checkBox.setEnabled(false);
            return view;
        }
    }
    private void doByWeixin( final String type){
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid",DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("type", type);
        LogUtils.i("type:"+type);
        if(!TextUtils.isEmpty(bodyValue)){
            LogUtils.i(bodyValue);
            params.addBodyParameter("predocid",bodyValue);
        }
        if(!TextUtils.isEmpty(fs.getEndTime())){
            params.addBodyParameter("Alerttime", fs.getEndTime());
        }
        params.addBodyParameter("Ispublic",fs.getType());
        if (!TextUtils.isEmpty(fs.getContent())) {
            params.addBodyParameter(Json.LASTCONTENT, fs.getContent());
        }
        if(!TextUtils.isEmpty(fs.getLocationName())){
            params.addBodyParameter("geopos", fs.getLocationName());
            params.addBodyParameter("geopara", fs.getLongitude() + "," + fs.getLatitude());
        }
        if (!TextUtils.isEmpty(fs.getVideoUri())) {
            params.addBodyParameter("soundlist", new File(fs.getVideoUri()));
        }
        String[] images=fs.getImages();
        if (images!=null) {
            for (int i=0;i<images.length;i++){
                params.addBodyParameter(Json.IMAGES+i, new File(images[i]));
            }
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.POST_DO_SUBMIT, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.i(arg0.getMessage() + ":" + arg1);
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                Result result = Json.getResult(responseInfo.result);
                pd.dismiss();
                if (result.getResult().equals("0")) {
                    bodyValue = result.getBodyvalue();
                        StringBuffer sb = new StringBuffer("http://dosns.net/modules/doinglist/Mainform.aspx?opr=opendocument&id=");
                        sb.append(result.getBodyvalue());
                        DemoApplication.getInstance().setBodyvalue(result.getBodyvalue());
                        wechatShare(fs.getContent(), 0, sb.toString());
                }
            }
        });
    }
    /**
     * 提交输入内容
     * @param handlerid
     * @param type
     */
    private void dobyself(String handlerid, final String type) {
        pd.setCanceledOnTouchOutside(false);
        pd.setTitle("提交中....");
        pd.show();
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid",DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("handlerid", handlerid);
        params.addBodyParameter("type", type);
        LogUtils.i("handlerid:" + handlerid);
        LogUtils.i("type:"+type);
        if(!TextUtils.isEmpty(bodyValue)){
            LogUtils.i(bodyValue);
            params.addBodyParameter("predocid",bodyValue);
        }
        if(!TextUtils.isEmpty(fs.getEndTime())){
            params.addBodyParameter("Alerttime", fs.getEndTime());
        }
            params.addBodyParameter("Ispublic",fs.getType());
        if (!TextUtils.isEmpty(fs.getContent())) {
            params.addBodyParameter(Json.LASTCONTENT, fs.getContent());
        }
        if(!TextUtils.isEmpty(fs.getLocationName())){
            params.addBodyParameter("geopos", fs.getLocationName());
            params.addBodyParameter("geopara", fs.getLongitude() + "," + fs.getLatitude());
        }
        if (!TextUtils.isEmpty(fs.getVideoUri())) {
            params.addBodyParameter("soundlist", new File(fs.getVideoUri()));
        }
        String[] images=fs.getImages();
        if (images!=null) {
            for (int i=0;i<images.length;i++){
                params.addBodyParameter(Json.IMAGES+i, new File(images[i]));
            }
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.POST_DO_SUBMIT, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                LogUtils.i(arg0.getMessage() + ":" + arg1);
                pd.dismiss();
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                Result result = Json.getResult(responseInfo.result);
                pd.dismiss();
                if (result.getResult().equals("0")) {
                    bodyValue = result.getBodyvalue();
                    if (type.equals("3")) {
                        StringBuffer sb = new StringBuffer("http://dosns.net/modules/doinglist/Mainform.aspx?opr=opendocument&id=");
                        sb.append(result.getBodyvalue());
                        DemoApplication.getInstance().setBodyvalue(result.getBodyvalue());
                        wechatShare(fs.getContent(), 0, sb.toString());
                        LogUtils.i("wechatShare");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    showDiogforisGoin(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Utils.showToast("发送成功", DemoApplication.getInstance());
                        showDiogforisGoin(0);
                    }
                }
            }
        });
    }
    //转发请求
    private void replier(final String targetuserid, String targetusername, String body, final String type,String MainId) {
        String url=Api.POST_Replter;
        if (type.equals("2")) {
            StringBuffer sb = new StringBuffer("http://dosns.net/modules/doinglist/Mainform.aspx?opr=opendocument&id=");
            sb.append(MainId);
            LogUtils.i("wechatShare:" + MainId + ",url:" + sb.toString());
            wechatShare(body, 0, sb.toString());
            return;
        }

        pd.setTitle("正在转发....");
        pd.show();
        RequestParams params = new RequestParams("UTF-8");
        LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("mainid", MainId);
        LogUtils.i("mainid:"+MainId);
        params.addBodyParameter("replieruserid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("repliername", Conversion.getInstance().getNickName());
        params.addBodyParameter("targetusertype",type);
        params.addBodyParameter("targetuserid",targetuserid);
        params.addBodyParameter("targetusername",targetusername);
        params.addBodyParameter("body", body);
       /* if(type.equals("0")){
            url="http://www.dosns.net/modules/doinglist/targetdoing.aspx";
            params.addBodyParameter("linkaddress",MainId);
        }*/
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                        Utils.showToast("转发成功", DemoApplication.getInstance());
                            showDiogforisGoin(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                Utils.showToast("转发失败", DemoApplication.getInstance());
            }
        });
    }

    /**
     * 是否继续发送
     */
    private void showDiogforisGoin(int type){
        firstSend=false;
        AlertDialog.Builder builder;
        String message=null;
        if(type==0){
            message="发送成功,是否继续发送?";
        }else if(type==1){
            message="转发成功,是否继续转发?";
        }
        builder=new AlertDialog.Builder(this).setMessage(message).setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (contactAdapter != null) {
                    contactAdapter.isClear();
                }
            }
        }).setNegativeButton("回首页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(LocatContentActivity.this,MainActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog ad= builder.create();
        ad.setCanceledOnTouchOutside(false);
        ad.show();
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
    private LinearLayout ll_container_tongshi;

    private void drawChild(){
        LogUtils.i("toshilist.size():" + totalList.size());
        if(totalList.size()!=0){
            horizontalscrlooview.setVisibility(View.VISIBLE);
        }else {
            horizontalscrlooview.setVisibility(View.GONE);
        }
        if(ll_container_tongshi.getChildCount()!=0){
            ll_container_tongshi.removeAllViews();
        }
        for(int i=0;i<totalList.size();i++) {
            View v=getLayoutInflater().inflate(R.layout.contant_item,null);
            TextView tv= (TextView) v.findViewById(R.id.contact_name);
            tv.setText(totalList.get(i).getNickName());
            tv.setOnClickListener(new MyClickListen(i));
            ll_container_tongshi.addView(v);
        }
    }
    private class MyClickListen implements View.OnClickListener{
        int p;
        public MyClickListen (int p){
            this.p=p;
        }
        @Override
        public void onClick(View v) {
            cleanStata(p);
            totalList.remove(p);
            drawChild();
        }
    }

    /**
     * 删除头像刷新ui
     */
    private void cleanStata(int p){
        LogUtils.i("totalList:"+totalList.get(p).getUserId());
        for(EaseUser eu:list){
            if(eu.getUserId().equals(totalList.get(p).getUserId())){
                contactAdapter.map.put(eu.getUserId(), false);
                contactAdapter.notifyDataSetChanged();
                break;
            }
        }
        for (List<EaseUser> list:sumToshi){
            for(EaseUser eu:list){
                if(eu.getUserId().equals(totalList.get(p).getUserId())) {
                    LogUtils.i("userid:"+eu.getUserId());
                    adapter.getIsSelected().put(eu.getUserId(), false);
                    adapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x22&&resultCode==LOCATION_RESULT_SEND){
            ArrayList<EaseUser> list_id=  data.getParcelableArrayListExtra("list");
            deleteSameUser(list_id);
            drawChild();
        }
    }
    private void deleteSameUser(ArrayList<EaseUser> list_id){
        for(int i=0;i<list_id.size();i++){
            if(!isSame(list_id.get(i).getUserId())) {
                totalList.add(list_id.get(i));
            }
        }
    }
    private Boolean isSame(String userid){
        Boolean isSame=false;
        for(int j=0;j<totalList.size();j++){
            if(userid.equals(totalList.get(j).getUserId())){
                isSame=true;
            }
        }
        return isSame;
    }
}
