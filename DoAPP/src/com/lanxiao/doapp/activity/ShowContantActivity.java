package com.lanxiao.doapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.widget.EaseSidebar;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShowContantActivity extends BaseActivity {
    private List<EaseUser> list;
    private PickContactAdapter contactAdapter;
    private ListView contactsListView;
    private TextView contant_title;
    private EaseSidebar es;
    private Button bt_contant_ok;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contant);
        init();
        getContact();
    }
    private void init(){
        bt_contant_ok= (Button) findViewById(R.id.bt_contant_ok);
        contactsListView= (ListView) findViewById(R.id.content_list);
        contant_title= (TextView) findViewById(R.id.contant_title);
        contant_title.setText(getIntent().getStringExtra("title"));
        id=getIntent().getIntExtra("id",0);
        es= (EaseSidebar) findViewById(R.id.sidebar);
        es.setListView(contactsListView);
        bt_contant_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<EaseUser> list = getToBeAddMembers();
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putParcelableArrayList("list", list);
                intent.putExtras(bundle);
                setResult(LOCATION_RESULT_SEND, intent);
                finish();
            }
        });
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.toggle();

            }
        });
    }
    /**
     * 获取本地联系人的数据
     */
    private void getContact() {
        try {
            list= DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("company_id", "=", id));
            if(list==null){
                LogUtils.i("list");
                list=new ArrayList<>();
            }else{
                if(list.size()==0){
                    Utils.showToast("暂无联系人",ShowContantActivity.this);
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
    List<EaseUser>  list_select= new ArrayList<EaseUser>();
    /**
     * 获取要需要发送的成员
     *
     * @return
     */
    private ArrayList<EaseUser> getToBeAddMembers() {
        list_select.clear();
        int length = contactAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++) {
            if (contactAdapter.isCheckedArray[i]) {
                list_select.add(contactAdapter.getItem(i));
            }
        }

        return (ArrayList<EaseUser>) list_select;
    }
    /**
     * adapter
     */
    private class PickContactAdapter extends EaseContactAdapter {

        private boolean[] isCheckedArray;

        public PickContactAdapter(Context context, int resource, List<EaseUser> users) {
            super(context, resource, users);
            isCheckedArray = new boolean[users.size()];
        }
        public void isClear(){
            if(isCheckedArray!=null){
                for(int i=0;i<isCheckedArray.length;i++){
                    isCheckedArray[i]=false;
                    notifyDataSetChanged();
                }
            }
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
            if (!TextUtils.isEmpty(list.get(position).getAvatar())) {
                BitmapUtils bp = new BitmapUtils(getContext());
                bp.display(avatarView, list.get(position).getAvatar());
            }
            nameView.setText(list.get(position).getNickName());
            if (checkBox != null) {
                checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);

                // checkBox.setOnCheckedChangeListener(null);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isCheckedArray[position] = isChecked;
                        //如果是单选模式
                       /* if ( isChecked) {
                            for (int i = 0; i < isCheckedArray.length; i++) {
                                if (i != position) {
                                    isCheckedArray[i] = false;
                                }
                            }
                            contactAdapter.notifyDataSetChanged();
                        }*/
                        if(getToBeAddMembers().size()==0){
                            bt_contant_ok.setVisibility(View.GONE);
                        }else {
                            bt_contant_ok.setVisibility(View.VISIBLE);
                        }

                    }
                });

                checkBox.setChecked(isCheckedArray[position]);

            }
            return view;
        }
    }
}
