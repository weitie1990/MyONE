package com.lanxiao.doapp.framment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.doapp.R;
import com.lanxiao.doapp.activity.BaseActivity;
import com.lanxiao.doapp.activity.CurrentLocationActivity;
import com.lanxiao.doapp.activity.MainActivity;
import com.lanxiao.doapp.activity.MeetingDoActivity;
import com.lanxiao.doapp.activity.MeetingWebActivity;
import com.lanxiao.doapp.activity.WorkWebActivity;
import com.lanxiao.doapp.adapter.DoWorkAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoWorkItem;
import com.lanxiao.doapp.entity.Work;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.DoappTitleBar;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Frament_workappion extends BaseActivity {
    DoWorkAdapter doWorkAdapter;
    GridView gridView;
    DoappTitleBar doapp_title_bar;
    String [] text={"签到","会议","投票","任务","审批","报销","出差","物品领用","请假"};
    int [] icon={R.drawable.iv_work_meeting,R.drawable.iv_work_clent,R.drawable.iv_work_log,R.drawable.iv_work_task,
            R.drawable.iv_work_approve,R.drawable.iv_work_account,R.drawable.iv_work_notice,R.drawable.iv_work_project,
            R.drawable.iv_work_leave};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frament_workappion);
        gridView= (GridView) findViewById(R.id.gridview);
        doapp_title_bar= (DoappTitleBar) findViewById(R.id.doapp_title_bar);
        doapp_title_bar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==list.size()+1){
                    Intent intent = new Intent(Frament_workappion.this, MainActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    return;
                }
                if(i==list.size()){
                    return;
                }
                Work work=list.get(i);
                LogUtils.i("work:"+work.getType());
                if(work.getType().equals("app")){
                   String tage=work.getTarget();
                    Utils.intoApp(tage,Frament_workappion.this);
                }else if(work.getType().equals("web")){
                    String tage=work.getTarget();
                    intoWeb(tage +"?userid="+DemoHelper.getInstance().getCurrentUsernName(), work.getName());
                }
            }
        });
        init();
    }

    private void intoWeb(String url,String type){
        Intent intent1=new Intent(Frament_workappion.this,MeetingWebActivity.class);
        intent1.putExtra("result",url);
        intent1.putExtra("type",type);
        startActivity(intent1);
    }
    private List<Work> list;
    private void init(){
        initWork();
        doWorkAdapter=new DoWorkAdapter(this,list);
        //添加并且显示
        gridView.setAdapter(doWorkAdapter);

    }

    public void initWork(){
        try {
            list = DemoApplication.getInstance().getDb().findAll(Work.class);
        } catch (DbException e) {
            e.printStackTrace();
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
    private void intoInputActivity(){
        Intent intent=new Intent("android.intent.inputActivity");
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_blew, R.anim.out_to_up);
    }
}
