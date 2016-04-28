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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.adapter.EaseContactAdapter;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.widget.EaseSidebar;
import com.easemob.easeui.widget.MyUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.Constant;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  群组添加联系人界面
 */
public class GroupPickContactsActivity extends BaseActivity {
	private ListView listView;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers;
	List<EaseUser> alluserList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_pick_contacts);

		// String groupName = getIntent().getStringExtra("groupName");
		String groupId = getIntent().getStringExtra("groupId");
		if (groupId == null) {// 创建群组
			isCreatingNewGroup = true;
		} else {
			// 获取此群组的成员列表
			EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
			exitingMembers = group.getMembers();
		}
		if(exitingMembers == null)
			exitingMembers = new ArrayList<String>();
		// 获取好友列表
		alluserList = new ArrayList<EaseUser>();
		for (EaseUser user : DemoHelper.getInstance().getContactList().values()) {
			if (!user.getUsername().equals(Constant.NEW_FRIENDS_USERNAME) & !user.getUsername().equals(Constant.GROUP_USERNAME) & !user.getUsername().equals(Constant.CHAT_ROOM) & !user.getUsername().equals(Constant.CHAT_ROBOT))
				alluserList.add(user);
		}
		LogUtils.i(alluserList.size()+"");


		/*// 对list进行排序
		Collections.sort(alluserList, new Comparator<EaseUser>() {

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
		});*/
		listView = (ListView) findViewById(R.id.list);
		if(alluserList.size()!=0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < alluserList.size(); i++) {
				sb.append(alluserList.get(i).getUsername() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			LogUtils.i(sb.toString());
			getMachFriend(sb.toString());
		}else {
			contactAdapter = new PickContactAdapter(getApplicationContext(), R.layout.row_contact_with_checkbox, alluserList);
			listView.setAdapter(contactAdapter);
		}

		es= (EaseSidebar) findViewById(R.id.sidebar);
		es.setListView(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				checkBox.toggle();

			}
		});
	}
	EaseSidebar es;
	List<EaseUser> contactList;
	/**
	 * 群组中添加朋友
	 * @param sb
	 */
	public  void getMachFriend(String sb) {
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("useridlist", sb);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, MyUtils.SEARCH_ALL_FRIEND, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				com.lidroid.xutils.util.LogUtils.i(arg0.getMessage() + ":" + arg1);
				return;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				contactList=new ArrayList<EaseUser>();
				com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
				JSONObject js = null;
				try {
					js = new JSONObject(responseInfo.result);
					JSONArray ja = js.getJSONArray("userlist");
					for (int i = 0; i < ja.length(); i++) {
						JSONObject js2 = ja.getJSONObject(i);
						EaseUser user = new EaseUser();
						alluserList.get(i).setAvatar(js2.optString("touxiang"));
						alluserList.get(i).setUserId(js2.optString("userid"));
						alluserList.get(i).setNickName(js2.optString("nickname"));
						alluserList.get(i).setInitialLetter(Utils.getPingYin(js2.optString("nickname")));

						/*user.setUserId(js2.optString("userid"));
						user.setAvatar(js2.optString("touxiang"));
						user.setNickName(js2.optString("nickname"));
						user.setInitialLetter(Utils.getPingYin(js2.optString("nickname")));
						contactList.add(user);*/
					}
					contactAdapter = new PickContactAdapter(getApplicationContext(), R.layout.row_contact_with_checkbox, alluserList);
					listView.setAdapter(contactAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}
	/**
	 * 确认选择的members
	 * 
	 * @param v
	 */
	public void save(View v) {
		/*if(getToBeAddMembers().size()==0){
			Utils.showToast("请先选择好友！",this);
			return;
		}*/
		setResult(RESULT_OK, new Intent().putExtra("newmembers", getToBeAddMembers().toArray(new String[0])));
		finish();
	}

	/**
	 * 获取要被添加的成员
	 * 
	 * @return
	 */
	private List<String> getToBeAddMembers() {
		List<String> members = new ArrayList<String>();
		LogUtils.i("contactAdapter:" + (contactAdapter == null));
		LogUtils.i("contactAdapter.isCheckedArray:"+(contactAdapter.isCheckedArray==null));
		int length = contactAdapter.isCheckedArray.length;
		for (int i = 0; i < length; i++) {
			String username = contactAdapter.getItem(i).getUsername();
			if (contactAdapter.isCheckedArray[i] && !exitingMembers.contains(username)) {
				members.add(username);
			}
		}

		return members;
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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
//			if (position > 0) {
				final String username = getItem(position).getUsername();
				// 选择框checkbox
				final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				ImageView avatarView = (ImageView) view.findViewById(R.id.avatar);
				TextView nameView = (TextView) view.findViewById(R.id.name);
				Utils.setAver(alluserList.get(position).getAvatar(),avatarView);
				nameView.setText(alluserList.get(position).getNickName());
				if (checkBox != null) {
				    if(exitingMembers != null && exitingMembers.contains(username)){
	                    checkBox.setButtonDrawable(R.drawable.checkbox_bg_gray_selector);
	                }else{
	                    checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
	                }
					// checkBox.setOnCheckedChangeListener(null);

					checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// 群组中原来的成员一直设为选中状态
							if (exitingMembers.contains(username)) {
									isChecked = true;
									checkBox.setChecked(true);
							}
							isCheckedArray[position] = isChecked;
							//如果是单选模式
							if (isSignleChecked && isChecked) {
								for (int i = 0; i < isCheckedArray.length; i++) {
									if (i != position) {
										isCheckedArray[i] = false;
									}
								}
								contactAdapter.notifyDataSetChanged();
							}
							
						}
					});
					// 群组中原来的成员一直设为选中状态
					if (exitingMembers.contains(username)) {
							checkBox.setChecked(true);
							isCheckedArray[position] = true;
					} else {
						checkBox.setChecked(isCheckedArray[position]);
					}
				}
//			}
			return view;
		}
	}

	public void back(View view){
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
