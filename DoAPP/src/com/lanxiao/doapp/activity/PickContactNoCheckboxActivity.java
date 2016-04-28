/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanxiao.doapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.widget.EaseSidebar;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.Constant;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PickContactNoCheckboxActivity extends BaseActivity {

	private ListView listView;
	private EaseSidebar sidebar;
	protected EaseContactAdapter contactAdapter;
	private List<EaseUser> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_contact_no_checkbox);
		listView = (ListView) findViewById(R.id.list);
		sidebar = (EaseSidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		contactList = new ArrayList<EaseUser>();
		// 获取设置contactlist
		getContactList();
		// 设置adapter
		contactAdapter = new EaseContactAdapter(this, R.layout.ease_row_contact, contactList);
		listView.setAdapter(contactAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick(position);
			}
		});

	}

	protected void onListItemClick(int position) {
//		if (position != 0) {
			setResult(RESULT_OK, new Intent().putExtra("username", contactAdapter.getItem(position)
					.getUserId()));
			finish();
//		}
	}

	public void back(View view) {
		finish();
	}

	private void getContactList() {
		contactList.clear();
		/*Map<String, EaseUser> users = DemoHelper.getInstance().getContactList();
		Iterator<Entry<String, EaseUser>> iterator = users.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, EaseUser> entry = iterator.next();
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME) && !entry.getKey().equals(Constant.CHAT_ROOM) && !entry.getKey().equals(Constant.CHAT_ROBOT))
				contactList.add(entry.getValue());
		}*/
		try {
			List<EaseUser> list= DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("whosfriend","=",DemoHelper.getInstance().getCurrentUsernName()));
			if(list!=null&&list.size()!=0){
				com.lidroid.xutils.util.LogUtils.i(list.size()+"");
				contactList.clear();
				contactList.addAll(list);
				// 排序
				Collections.sort(contactList, new Comparator<EaseUser>() {

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
			}else{
				com.lidroid.xutils.util.LogUtils.i("server");
			}
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
}
