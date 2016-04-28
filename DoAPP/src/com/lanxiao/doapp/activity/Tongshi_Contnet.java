package com.lanxiao.doapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.adapter.MyExpandableListViewAdapter;
import com.lanxiao.doapp.myView.CardModel;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

public class Tongshi_Contnet extends BaseActivity {
    private ExpandableListView expandableListView;

    private List<String> group_list;

    private List<String> item_lt;

    private List<List<String>> item_list;
    private LinearLayout ll_container_tongshi;

    private MyExpandableListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongshi__contnet);
        init();
    }
    private void init(){
        ll_container_tongshi= (LinearLayout) findViewById(R.id.ll_container_tongshi);
        // 随便一堆测试数据
        group_list = new ArrayList<String>();
        group_list.add("市场部");
        group_list.add("人事部");
        group_list.add("开发部");
        group_list.add("行动部");

        item_lt = new ArrayList<String>();
        item_lt.add("1");
        item_lt.add("2");
        item_lt.add("3");

        item_list = new ArrayList<List<String>>();
        item_list.add(item_lt);
        item_list.add(item_lt);
        item_list.add(item_lt);
        item_list.add(item_lt);








        expandableListView = (ExpandableListView)findViewById(R.id.expendlist);
        expandableListView.setGroupIndicator(null);

        // 监听组点击
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @SuppressLint("NewApi")
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                if (item_list.get(groupPosition).isEmpty())
                {
                    return true;
                }
                return false;
            }
        });

        // 监听每个分组里子控件的点击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                startActivityForResult(new Intent(Tongshi_Contnet.this, ShowContantActivity.class).putExtra("title", item_list.get(groupPosition).get(childPosition)), 0x22);
                return false;
            }
        });

      /*  adapter = new MyExpandableListViewAdapter(this,group_list,item_list);

        expandableListView.setAdapter(adapter);*/
    }
    public void back(View v){
        finish();
    }
    List<String> ids=new ArrayList<>();
    List<String> nickNames=new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0x22&&resultCode==LOCATION_RESULT_SEND){
            ArrayList<String> list_id=data.getStringArrayListExtra("handlerid");
            ArrayList<String> list_nicks=data.getStringArrayListExtra("targetusername");
            nickNames.addAll(list_nicks);
            LogUtils.i(list_nicks.get(0)+"");
            drawChild();
        }
    }
    private void drawChild(){
        if(ll_container_tongshi.getChildCount()!=0){
            ll_container_tongshi.removeAllViews();
        }
        for(int i=0;i<nickNames.size();i++) {
            View v=getLayoutInflater().inflate(R.layout.contant_item,null);
            TextView tv= (TextView) v.findViewById(R.id.contact_name);
            tv.setText(nickNames.get(i));
            v.findViewById(R.id.contact_delete).setOnClickListener(new MyClickListen(i));
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
        }
    }
}
