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
package com.lanxiao.doapp.framment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.easemob.chat.EMContactManager;

import com.easemob.easeui.domain.EaseUser;
import com.easemob.util.EMLog;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.AddContactActivity;
import com.lanxiao.doapp.activity.ChatActivity;
import com.lanxiao.doapp.activity.GroupsActivity;
import com.lanxiao.doapp.activity.NewFriendsMsgActivity;
import com.lanxiao.doapp.activity.PublicChatRoomsActivity;
import com.lanxiao.doapp.activity.RobotsActivity;
import com.lanxiao.doapp.activity.Tongshi_Contnet;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.InviteMessgeDao;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.UserDao;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.ContactItemView;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * 联系人列表页
 * 
 */
public class ContactListFragment extends EaseContactListFragment {
	
    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void initView() {
        super.initView();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        //添加headerview
        listView.addHeaderView(headerView);
        //添加正在加载数据提示的loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);
        //注册上下文菜单
        registerForContextMenu(listView);
    }
    
    @Override
    public void refresh() {
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }
    
    
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.iv_more);
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        titleBar.setLeftImageResource(R.drawable.iv_main_back);
        titleBar.setLeftLayoutClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        //设置联系人数据
        setContactsMap(DemoHelper.getInstance().getContactList());
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser)listView.getItemAtPosition(position)).getUsername();
                LogUtils.i((username==null)+"");
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
            }
        });

        
        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        
        
        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);
        
        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
        
        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);
        
        if (!DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }
        
        if(blackListSyncListener != null){
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }
        
        if(contactInfoSyncListener != null){
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }
    
	
	protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.application_item:
                // 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                break;
            case R.id.group_item:
                // 进入群聊列表页面
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            case R.id.chat_room_item:
                //进入聊天室列表页面
                startActivity(new Intent(getActivity(), Tongshi_Contnet.class));
                break;
           /* case R.id.robot_item:
                //进入Robot列表页面
                startActivity(new Intent(getActivity(), RobotsActivity.class));
                break;*/

            default:
                break;
            }
        }
	    
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        LogUtils.i(((AdapterContextMenuInfo) menuInfo).position+"");
	    toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
	    toBeProcessUsername = toBeProcessUser.getUsername();
        LogUtils.i(toBeProcessUsername+"processusername");
        getActivity().getMenuInflater().inflate(R.menu.context_contact_list, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			try {
                // 删除此联系人
                deleteContact(toBeProcessUser);
                // 删除相关的邀请消息
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			moveToBlacklist(toBeProcessUsername);
			return true;
		}
		return super.onContextItemSelected(item);
	}


	/**
	 * 删除联系人
	 * 
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
        try {
            LogUtils.i("删除");
            DemoApplication.getInstance().getDb().delete(tobeDeleteUser);
        } catch (DbException e) {
            e.printStackTrace();
        }
        String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
                    LogUtils.i(tobeDeleteUser.getUsername());
					EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							contactList.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2 + e.getMessage(), 1).show();
						}
					});

				}

			}
		}).start();

	}
	
	class ContactSyncListener implements DemoHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, 1).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }
                        
                    });
                }
            });
        }
    }
    
    class BlackListSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    blackList = EMContactManager.getInstance().getBlackListUsernames();
                    refresh();
                }
                
            });
        }
        
    };
    
    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }
        
    }
	
}
