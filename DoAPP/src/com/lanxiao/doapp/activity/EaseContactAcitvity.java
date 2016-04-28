package com.lanxiao.doapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.FrameLayout;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.Constant;
import com.lanxiao.doapp.framment.ContactListFragment;
import com.umeng.analytics.MobclickAgent;

/**
 * 联系人界面
 */
public class EaseContactAcitvity extends BaseActivity {
    FrameLayout fl;
    ContactListFragment easeContactListFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ease_contact_acitvity);
        //fl= (FrameLayout) findViewById(R.id.frameLayout_contact);
        easeContactListFragment=new ContactListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout_contact,easeContactListFragment,"EaseContactListFragment").commit();
        registerBroadcastReceiver();
    }

    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;
    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if(easeContactListFragment!=null){
                    easeContactListFragment.refresh();
                }
                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(EaseContactAcitvity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcastReceiver();
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
