package com.lanxiao.doapp.framment;
/**
 * doing界面"关注"的frament
 */

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.example.doapp.R;
import com.lanxiao.doapp.adapter.RecyClerAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.entity.FriendStateItem;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.http.Json;
import com.lanxiao.doapp.myView.AutoLoadRecyclerView;
import com.lanxiao.doapp.myView.CircleProgressBar;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Frament_attention_reficive extends MyPageBase {
    ArrayList<FriendStateItem> list1 = new ArrayList<>();
    ArrayList<FriendStateItem> totalDate = new ArrayList<>();
    AutoLoadRecyclerView listView;
    RecyClerAdapter myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String appUrl = Api.POST_DOLIST;
    CircleProgressBar progressBar;
    TextView tv_resh;
    String type=null;
    private View mFragmentView;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    @Override
    protected void lazyLoad() {
        LogUtils.i("att_isPrepared:"+isPrepared);
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        LogUtils.i("Frament_attention");
        Message message = Message.obtain();
        message.what = 2;
        mHandler.sendMessageDelayed(message, 0);
    }

    public enum RefreshType{
        REFRESH,LOAD_MORE
    }
    private RefreshType mRefreshType = RefreshType.REFRESH;


    public static Frament_attention_reficive newInstance(String type) {
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        Frament_attention_reficive frament_attention = new Frament_attention_reficive();
        frament_attention.setArguments(bundle);

        return frament_attention;
    }
    public void resh(){
        LogUtils.i("resh");
        if(listView!=null){
            listView.scrollToPosition(0);
            swipeRefreshLayout.setRefreshing(true);
            Message message = Message.obtain();
            message.what = 2;
            mHandler.sendMessageDelayed(message, 2000);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mFragmentView==null){
            mFragmentView = inflater.inflate(R.layout.listviewfordoingrecycleview, container, false);
            Bundle bundle=getArguments();
            if(bundle!=null){
                type=bundle.getString("type");
            }
            LogUtils.i("goit");
            listView = (AutoLoadRecyclerView) mFragmentView.findViewById(android.R.id.list);
            tv_resh= (TextView) mFragmentView.findViewById(R.id.tv_resh);
            swipeRefreshLayout= (SwipeRefreshLayout) mFragmentView.findViewById(R.id.mYswipeRefreshLayout);
            swipeRefreshLayout.setColorSchemeResources(com.easemob.easeui.R.color.holo_blue_bright, com.easemob.easeui.R.color.holo_green_light,
                    com.easemob.easeui.R.color.holo_orange_light, com.easemob.easeui.R.color.holo_red_light);
            // 拿到RecyclerView
            // 设置LinearLayoutManager
            listView.setLayoutManager(new LinearLayoutManager(getContext()));
            // 设置ItemAnimator
            listView.setItemAnimator(new DefaultItemAnimator());

            // 初始化自定义的适配器
            myAdapter = new RecyClerAdapter(getContext(), list1,tag);
            // 为mRecyclerView设置适配器
            listView.setAdapter(myAdapter);
            if (list1.size() == 0) {
                progressBar = (CircleProgressBar) mFragmentView.findViewById(R.id.McircleProgressBar);
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
            isPrepared = true;
            lazyLoad();
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup)mFragmentView.getParent();
        if(parent != null) {
            parent.removeView(mFragmentView);
        }

        return mFragmentView;
    }
    Object tag = new Object();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tv_resh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tv_resh.setVisibility(View.GONE);
                Message message = Message.obtain();
                message.what = 2;
                mHandler.sendMessageDelayed(message, 2000);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Message message = Message.obtain();
                message.what = 2;
                mHandler.sendMessageDelayed(message, 2000);
            }
        });
        myAdapter.setOnItemClickListener(new RecyClerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                myAdapter.intoDescatte(list1.get(data));
            }
        });
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(getContext()).resumeTag(tag);
                } else {
                    Picasso.with(getContext()).pauseTag(tag);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                getAttentionList();
            }


        }
    };

    /**
     * 根据url获取json数据
     */
    public void getAttentionList() {
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams rq=new RequestParams("UTF-8");
        rq.addBodyParameter("userid", EMChatManager.getInstance().getCurrentUser());
        rq.addBodyParameter("type",type);
        LogUtils.i(EMChatManager.getInstance().getCurrentUser());
        LogUtils.i("HttpUtils_attention");
        httpUtils.send(HttpRequest.HttpMethod.GET, appUrl+"?t="+ System.currentTimeMillis(),rq,new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                mHasLoadedOnce = true;
                ArrayList<FriendStateItem> list = Json.parseRecommendData(responseInfo.result);
                if (list.size() != 0 && list.get(list.size() - 1) != null) {
                    if (mRefreshType == RefreshType.REFRESH) {
                        list1.clear();
                    }
                    list1.addAll(list);
                    myAdapter.setData(list1);
                    swipeRefreshLayout.setRefreshing(false);
                    listView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    tv_resh.setVisibility(View.GONE);
                } else {
                    Utils.showToast("暂无数据！", getActivity().getApplicationContext());
                    swipeRefreshLayout.setRefreshing(false);
                    tv_resh.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if(list1.size()==0){
                    swipeRefreshLayout.setRefreshing(false);
                    tv_resh.setText("网络数据请求异常,请点击重新加载");
                    tv_resh.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
                LogUtils.i(s);
            }
        });
    }
}
