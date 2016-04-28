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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.R;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.ui.EaseBaseFragment;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.easemob.easeui.widget.EaseContactList;
import com.easemob.exceptions.EaseMobException;
import com.lanxiao.doapp.chatui.applib.chatuimain.db.DemoDBManager;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.bitlet.weupnp.LogUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 联系人列表页
 * 
 */
public class EaseContactListFragment extends EaseBaseFragment {
    private static final String TAG = "EaseContactListFragment";
    protected List<EaseUser> contactList;
    protected ListView listView;
    protected boolean hidden;
    protected List<String> blackList;
    protected ImageButton clearSearch;
    protected EditText query;
    protected Handler handler = new Handler();
    protected EaseUser toBeProcessUser;
    protected String toBeProcessUsername;
    protected EaseContactList contactListLayout;
    protected boolean isConflict;
    protected FrameLayout contentContainer;
    private SwipeRefreshLayout swipeRefeshLayout;
    private Map<String, EaseUser> contactsMap;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {

        contentContainer = (FrameLayout) getView().findViewById(R.id.content_container);
        
        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);        
        listView = contactListLayout.getListView();
        swipeRefeshLayout= contactListLayout.getSwipeRefeshLayout();
        swipeRefeshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        //搜索框
        query = (EditText) getView().findViewById(R.id.query);
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
    }

    @Override
    protected void setUpView() {
        swipeRefeshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContentFromServer();
            }
        });
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        
        //黑名单列表
        blackList = EMContactManager.getInstance().getBlackListUsernames();
        contactList = new ArrayList<EaseUser>();
        // 获取设置contactlist
        //getContactList();
        //init list
        contactListLayout.init(contactList);
        com.lidroid.xutils.util.LogUtils.i(contactList.size()+"");
        if(listItemClickListener != null){
            listView.setOnItemClickListener(new OnItemClickListener() {
    
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                    listItemClickListener.onListItemClicked(user);
//                    itemClickLaunchIntent.putExtra(EaseConstant.USER_ID, username);
                }
            });
        }
        
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                    
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
        
        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }
        });
        
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }


    /**
     * 把user移入到黑名单
     */
    protected void moveToBlacklist(final String username){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //加入到黑名单
                    EMContactManager.getInstance().addUserToBlackList(username,false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, 0).show();
                            refresh();
                        }
                    });
                } catch (EaseMobException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, 0).show();
                        }
                    });
                }
            }
        }).start();
        
    }
    
    // 刷新ui
    public void refresh() {
        getContactList();
        contactListLayout.refresh();
    }


    @Override
    public void onDestroy() {
        
        EMChatManager.getInstance().removeConnectionListener(connectionListener);
        
        super.onDestroy();
    }


    /**
     * 获取联系人列表，并过滤掉黑名单和排序
     */
    protected void getContactList() {
        contactList.clear();
        synchronized (contactList) {
            //获取联系人列表
            if(contactsMap == null){
                return;
            }
            Iterator<Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, EaseUser> entry = iterator.next();
                //兼容以前的通讯录里的已有的数据显示，加上此判断，如果是新集成的可以去掉此判断
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")){
                    if(!blackList.contains(entry.getKey())){
                        //不显示黑名单中的用户
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        //contactList.add(user);
                    }
                }
            }
            try {
                List<EaseUser> list=DemoApplication.getInstance().getDb().findAll(Selector.from(EaseUser.class).where("whosfriend","=",DemoHelper.getInstance().getCurrentUsernName()));
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

    }
    
    
    
    protected EMConnectionListener connectionListener = new EMConnectionListener() {
        
        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }
        
        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListItemClickListener listItemClickListener;
    
    
    protected void onConnectionDisconnected() {
        
    }
    
    protected void onConnectionConnected() {
        
    }
    
    /**
     * 设置需要显示的数据map，key为环信用户id
     * @param contactsMap
     */
    public void setContactsMap(Map<String, EaseUser> contactsMap){
        this.contactsMap = contactsMap;
    }
    
    public interface EaseContactListItemClickListener {
        /**
         * 联系人listview item点击事件
         * @param user 被点击item所对应的user对象
         */
        void onListItemClicked(EaseUser user);
    }
    
    /**
     * 设置listview item点击事件
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }
    public void getContentFromServer(){
        //每次进入获取联第人列表
        Map<String, EaseUser> contactsMap=DemoHelper.getInstance().getContactList();
        com.lidroid.xutils.util.LogUtils.i(contactsMap.size() + "");
        List<String> blackList = EMContactManager.getInstance().getBlackListUsernames();
        if(contactsMap == null){
            return;
        }
        StringBuffer sb=new StringBuffer();
        Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, EaseUser> entry = iterator.next();
            if(!blackList.contains(entry.getKey())){
                //不显示黑名单中的用户
                EaseUser user = entry.getValue();
                EaseCommonUtils.setUserInitialLetter(user);
                sb.append(user.getUsername()+",");
            }

        }
        if(sb.length()!=0){
            sb.deleteCharAt(sb.length()-1);
            com.lidroid.xutils.util.LogUtils.i(sb.toString());
            saveContentServer(sb.toString());
        }
    }
    /**
     * 保存好友到本地数据库
     */
    public void saveContentServer(final String from) {
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("useridlist", from);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_ALL_FRIEND, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                com.lidroid.xutils.util.LogUtils.i(arg0.getMessage() + ":" + arg1);
                swipeRefeshLayout.setRefreshing(false);
                return;
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                com.lidroid.xutils.util.LogUtils.i(responseInfo.result);
                swipeRefeshLayout.setRefreshing(false);
                String ToNickName = null;
                try {
                    DemoApplication.getInstance().getDb().delete(EaseUser.class, WhereBuilder.b("whosfriend","=",DemoHelper.getInstance().getCurrentUsernName()));
                    JSONObject js = new JSONObject(responseInfo.result);
                    JSONArray ja = js.getJSONArray("userlist");
                    if (ja == null) {
                        com.lidroid.xutils.util.LogUtils.i("未查找到");
                        Utils.showToast("暂无好友！",getActivity());
                        return;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject js2 = ja.getJSONObject(i);
                        EaseUser easeUser = new EaseUser();
                        ToNickName = js2.optString("nickname");
                        easeUser.setUserId(js2.optString("userid"));
                        easeUser.setUsername(js2.optString("userid"));
                        easeUser.setNickName(ToNickName);
                        easeUser.setAvatar(js2.optString("touxiang"));
                        easeUser.setInitialLetter(Utils.getPingYin(ToNickName));
                        easeUser.setWhosfriend(DemoHelper.getInstance().getCurrentUsernName());
                        DemoDBManager.getInstance().getDoappContactList(easeUser);
                    }
                    refresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (DbException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
