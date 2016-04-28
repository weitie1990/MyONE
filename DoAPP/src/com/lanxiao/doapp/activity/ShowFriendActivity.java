package com.lanxiao.doapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.example.doapp.R;
import com.lanxiao.doapp.adapter.MBaseAdapter;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.untils.util.Utils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

public class ShowFriendActivity extends BaseActivity {
    List<DoUser> list;
    ListView lv;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);
        list= (ArrayList<DoUser>) getIntent().getExtras().getSerializable("user");
        lv= (ListView) findViewById(R.id.lv_showFriend_addContact);
        lv.setAdapter(new MyAdapter());
    }
    private class MyAdapter extends MBaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.listitemforaddcontact,viewGroup,false);
            ImageView iv= (ImageView) view.findViewById(R.id.avatar);
            TextView tv= (TextView) view.findViewById(R.id.name);
            Button indicator= (Button) view.findViewById(R.id.indicator);
            String aver=list.get(i).getAver();

            Utils.setAver(aver,iv);
            tv.setText(list.get(i).getNickName());
            indicator.setOnClickListener(new MyOnOnClickListener(i));
            return view;
        }
    }
    private class MyOnOnClickListener implements View.OnClickListener{
        public int p;
        public MyOnOnClickListener(int p){
            this.p=p;
        }
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.indicator){
                MyDiogAlret(p);
            }
        }
    }
    public void MyDiogAlret(final int p){
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this).setTitle("说点什么呗！").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        addContact(list.get(p).getId(),nickString);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }
    /**
     *  添加contact
     */
    public void addContact(final String userId, final String nickString){
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
                    EMContactManager.getInstance().addContact(userId, nickString);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, 1).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
                        }
                    });
                }
            }
        }).start();
    }
    public void back(View v) {
        finish();
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
