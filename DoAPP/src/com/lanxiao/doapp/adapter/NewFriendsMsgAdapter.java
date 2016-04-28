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
package com.lanxiao.doapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.Conversion;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.widget.MyUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.InviteMessgeDao;
import com.lanxiao.doapp.chatui.applib.chatuimain.domain.InviteMessage;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.DoUser;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView= LayoutInflater.from(context).inflate(R.layout.row_invite_msg, parent,false);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
			holder.reason = (TextView) convertView.findViewById(R.id.message);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.status = (Button) convertView.findViewById(R.id.user_state);
			holder.groupContainer = (LinearLayout) convertView.findViewById(R.id.ll_group);
			holder.groupname = (TextView) convertView.findViewById(R.id.tv_groupName);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
			AutoUtils.autoSize(convertView);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String str1 = context.getResources().getString(R.string.Has_agreed_to_your_friend_request);
		String str2 = context.getResources().getString(R.string.agree);
		
		String str3 = context.getResources().getString(R.string.Request_to_add_you_as_a_friend);
		String str4 = context.getResources().getString(R.string.Apply_to_the_group_of);
		String str5 = context.getResources().getString(R.string.Has_agreed_to);
		String str6 = context.getResources().getString(R.string.Has_refused_to);
		final InviteMessage msg = getItem(position);
		if (msg != null) {
			if(msg.getGroupId() != null){ // 显示群聊提示
				holder.groupContainer.setVisibility(View.VISIBLE);
				holder.groupname.setText(msg.getGroupName());
			} else{
				holder.groupContainer.setVisibility(View.GONE);
			}
			
			holder.reason.setText(msg.getReason());
			//holder.name.setText(msg.getFrom());
			//设置用户头像和用户名
			MyUtils.setNickNameandAverFromSever(msg.getFrom(),holder.avator,holder.name,getContext());
			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			//别人同意我的好友请求
			if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
				holder.status.setVisibility(View.INVISIBLE);
				holder.reason.setText(str1);

			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) {
				holder.status.setVisibility(View.VISIBLE);
				holder.status.setEnabled(true);
				holder.status.setText(str2);
				if(msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED){
					if (msg.getReason() == null) {
						// 如果没写理由
						holder.reason.setText(str3);
					}
				}else{ //入群申请
					if (TextUtils.isEmpty(msg.getReason())) {
						holder.reason.setText(str4 + msg.getGroupName());
					}
				}
				// 设置点击事件
				holder.status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 同意别人发的好友请求
						acceptInvitation(holder.status, msg);
					}
				});
			} else if (msg.getStatus() == InviteMessage.InviteMesageStatus.AGREED) {
				holder.status.setText(str5);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			} else if(msg.getStatus() == InviteMessage.InviteMesageStatus.REFUSED){
				holder.status.setText(str6);
				holder.status.setBackgroundDrawable(null);
				holder.status.setEnabled(false);
			}

			// 设置用户头像
		}

		return convertView;
	}
	//查找朋友的URL

	/**
	 * 同意好友请求或者群申请
	 * 
	 * @param button
	 */
	private void acceptInvitation(final Button button, final InviteMessage msg) {
		final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				// 调用sdk的同意方法
				try {
					if(msg.getGroupId() == null) //同意好友请求
					{
						EMChatManager.getInstance().acceptInvitation(msg.getFrom());
						searchContentServer(msg);
					}
					else //同意加群申请
					    EMGroupManager.getInstance().acceptApplication(msg.getFrom(), msg.getGroupId());
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							button.setText(str2);
							msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
							// 更新db
							ContentValues values = new ContentValues();
							values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
							messgeDao.updateMessage(msg.getId(), values);
							button.setBackgroundDrawable(null);
							button.setEnabled(false);

						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), 1).show();
						}
					});

				}
			}
		}).start();
	}

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView reason;
		Button status;
		LinearLayout groupContainer;
		TextView groupname;
		// TextView time;
	}

	/**
	 * 添加好友关系到我们服务器
	 * @param userId
	 * @param ToNickName
	 */
	public void addContentForServer(String userId,String ToNickName) {
		String userid = EMChatManager.getInstance().getCurrentUser();
		DoUser doUser=DemoHelper.getInstance().getDoUser();
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("userid", userid);
		params.addBodyParameter("username", Conversion.getInstance().getNickName());
		params.addBodyParameter("friendid", userId);
		params.addBodyParameter("friendname", ToNickName);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.ADD_CONTENT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i(arg0.getMessage() + ":" + arg1);
				Utils.showToast("请求网络异常", context);
				return;
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				try {
					JSONObject js = new JSONObject(responseInfo.result);
					if (js.optString("message").equals("成功")) {
						Utils.showToast("添加成功", context);
					} else {
						Utils.showToast(js.optString("bodyvalue"), context);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	EaseUser easeUser;
	/**
	 * 从服务器查找用户信息
	 */
	public void searchContentServer(final InviteMessage msg){
		RequestParams params = new RequestParams("UTF-8");
		params.addBodyParameter("useridlist",msg.getFrom());
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_ALL_FRIEND, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.i(arg0.getMessage() + ":" + arg1);
				Utils.showToast("请求网络异常", context);
				return;
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				String ToNickName=null;
				try {
					JSONObject js = new JSONObject(responseInfo.result);
					JSONArray ja=js.getJSONArray("userlist");
					if(ja.length()==0){
						LogUtils.i("未查找到");
						return;
					}
					for (int i=0;i<ja.length();i++) {
						JSONObject js2 = ja.getJSONObject(i);
						easeUser=new EaseUser();
						ToNickName=js2.optString("nickname");
						easeUser.setUsername(msg.getFrom());
						easeUser.setNickName(ToNickName);
						easeUser.setAvatar(js2.optString("touxiang"));
						easeUser.setInitialLetter(Utils.getPingYin(ToNickName));
						//DemoApplication.getInstance().getDb().save(easeUser);
						addContentForServer(msg.getFrom(), ToNickName);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		});
	}

}
