package com.lanxiao.doapp.framment;
/**
 * 朋友的第一层frament
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.easeui.widget.EaseAlertDialog;
import com.easemob.util.NetUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.AddContactActivity;
import com.lanxiao.doapp.activity.EaseContactAcitvity;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.RecCommentFriend;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.myView.DefinedScrollView;
import com.lanxiao.doapp.myView.MainSetItemView;
import com.lanxiao.doapp.myView.RoundedCornersImage;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Frament_TabFriend extends MainBaseFragment {




    private FrameLayout fl_error_item;
    private TextView errorText;
    private TextView unreadAddressLable;
    private DefinedScrollView definedScrollView;
    static int cout = 0;
    private ProgressDialog pd;
    List<RecCommentFriend> list;
    private LinearLayout.LayoutParams param;
    private LinearLayout mLinearLayout;
    int getCout=0;
    public static Frament_TabFriend newInstanc() {
        Frament_TabFriend frament_TabFriend = new Frament_TabFriend();
        return frament_TabFriend;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frament_friend, container, false);

        return v;
    }




    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable(final int cout) {
        getCout=cout;
    }

    @Override
    protected void initView() {

        list = new ArrayList<>();
        pd = new ProgressDialog(getContext());
        definedScrollView = (DefinedScrollView) getView().findViewById(R.id.definedview);
        fl_error_item = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        unreadAddressLable = (TextView) getView().findViewById(R.id.unread_address_number_sum);
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        fl_error_item.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        if(getCout!=0){
            unreadAddressLable.setVisibility(View.VISIBLE);
            unreadAddressLable.setText(getCout+"");
        }

    }

    @Override
    protected void setUpView() {
        getView().findViewById(R.id.btn_container_address_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unreadAddressLable.getVisibility()==View.VISIBLE){
                    unreadAddressLable.setVisibility(View.GONE);
                }
                Intent intent = new Intent(getActivity(), EaseContactAcitvity.class);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().startActivity(intent);
            }
        });
        getView().findViewById(R.id.re).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        SerchRecommentFromServer();
    }

    @Override
    protected void onConnectionConnected() {
        fl_error_item.setVisibility(View.GONE);
    }

    @Override
    protected void onConnectionDisconnected() {
        fl_error_item.setVisibility(View.VISIBLE);
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    private void SerchRecommentFromServer() {
        pd.setMessage("正在获取朋友信息...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.SEARCH_RECOMMENT, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd.dismiss();
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("0")) {
                        JSONArray jsonArray = jb.optJSONArray("userlist");
                        if (jsonArray != null || jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectSum = jsonArray.getJSONObject(i);
                                RecCommentFriend recCommentFriend = new RecCommentFriend();
                                recCommentFriend.setNickname(jsonObjectSum.optString("nickname"));
                                recCommentFriend.setCompany(jsonObjectSum.optString("company"));
                                recCommentFriend.setUserheadlogo(jsonObjectSum.optString("userheadlogo"));
                                recCommentFriend.setUserid(jsonObjectSum.optString("userid"));
                                recCommentFriend.setSign(jsonObjectSum.optString("sign"));
                                recCommentFriend.setRecmdStr(jsonObjectSum.optString("recmdStr"));
                                recCommentFriend.setTaglist(jsonObjectSum.optString("taglist"));
                                list.add(recCommentFriend);
                            }
                        }
                        for (int i = 0; i < list.size(); i++) {
                            new AnsyTry().execute(i);
                        }
                        definedScrollView.setPageListener(new DefinedScrollView.PageListener() {
                            @Override
                            public void page(int page) {
                                //setCurPage(page);
                            }
                        });
                    }else {
                        Utils.showToast("暂无推荐好友！", getContext());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd.dismiss();
                e.printStackTrace();
                Utils.showToast("获取数据失败", getContext());
                LogUtils.i(s);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    class MyOnClikLision implements View.OnClickListener{
        int p;
        public MyOnClikLision(int p){
            this.p=p;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.roundeImage_userpic:
                    Utils.intoInfo(getActivity(),list.get(p).getUserid());
                    break;
                case R.id.iv_friends_up:

                    break;
                case R.id.iv_friend_add:
                    MyDiogAlret(p);
                    break;
                case R.id.iv_friends_down:
                    break;
                case R.id.iv_friend_attent:
                    follow(p);
                    break;
            }
        }
    }
    public void follow(int p){
        HttpUtils httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("targetuserid", list.get(p).getUserid());
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.ATTENTION, params, new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb=new JSONObject(responseInfo.result);
                    if(jb.optString("result").equals("0")){
                        Utils.showToast("关注成功",getContext());
                    }else {
                        Utils.showToast(jb.optString("bodyvalue"),getContext());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
    public void MyDiogAlret(final int p){
        final EditText editText = new EditText(getContext());
        new AlertDialog.Builder(getContext()).setTitle("说点什么呗！").setIcon(android.R.drawable.ic_dialog_info).setView(editText)
                .setPositiveButton(R.string.dl_ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickString = editText.getText().toString();
                        addContact(list.get(p).getUserheadlogo(),nickString);
                    }
                }).setNegativeButton(R.string.dl_cancel, null).show();
    }
    /**
     *  添加contact
     */
    public void addContact(final String userId, final String nickString){
        if(EMChatManager.getInstance().getCurrentUser().equals(userId)){
            new EaseAlertDialog(getContext(), R.string.not_add_myself).show();
            return;
        }

        if(DemoHelper.getInstance().getContactList().containsKey(userId)){
            //提示已在好友列表中(在黑名单列表里)，无需添加
            if(EMContactManager.getInstance().getBlackListUsernames().contains(userId)){
                new EaseAlertDialog(getContext(), "已在好友列表中").show();
                return;
            }
            new EaseAlertDialog(getContext(), R.string.This_user_is_already_your_friend).show();
            return;
        }

        String stri = getResources().getString(R.string.Is_sending_a_request);
        pd.setMessage(stri);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    EMContactManager.getInstance().addContact(userId, nickString);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getActivity(), s1, 1).show();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getActivity(), s2 + e.getMessage(), 1).show();
                        }
                    });
                }
            }
        }).start();
    }
    TextView ivRecommentTitile;
    RoundedCornersImage roundeImageUserpic;
    TextView mivFriendName;
    TextView mivFriendCommpanyName;
    TextView mivFriendUserinfo;
    TextView mivFriendTag;
    ImageView iv_friend_attent;
    ImageView iv_friend_add;

    class AnsyTry extends AsyncTask{
        int i;
        @Override
        protected Object doInBackground(Object[] params) {
            i= (int) params[0];
            View view= LayoutInflater.from(getActivity()).inflate(R.layout.frament_recomment, null);
            ivRecommentTitile = ButterKnife.findById(view, R.id.iv_recomment_titile);
            roundeImageUserpic = ButterKnife.findById(view, R.id.roundeImage_userpic);
            mivFriendName = ButterKnife.findById(view, R.id.miv_friend_name);
            mivFriendCommpanyName = ButterKnife.findById(view, R.id.miv_friend_commpanyName);
            mivFriendUserinfo = ButterKnife.findById(view, R.id.miv_friend_userinfo);
            mivFriendTag = ButterKnife.findById(view, R.id.miv_friend_tag);
            iv_friend_attent = ButterKnife.findById(view, R.id.iv_friend_attent);
            iv_friend_add = ButterKnife.findById(view, R.id.iv_friend_add);

            param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mLinearLayout = new LinearLayout(getContext());
            iv_friend_attent.setOnClickListener(new MyOnClikLision(i));
            iv_friend_add.setOnClickListener(new MyOnClikLision(i));
            roundeImageUserpic.setOnClickListener(new MyOnClikLision(i));
            ivRecommentTitile.setText(list.get(i).getRecmdStr());
            Utils.setAver(list.get(i).getUserheadlogo(), roundeImageUserpic);
            mivFriendName.setText(list.get(i).getNickname());
            mivFriendCommpanyName.setText(list.get(i).getCompany());
            mivFriendUserinfo.setText(list.get(i).getSign());
            mivFriendTag.setText(list.get(i).getTaglist());
            mLinearLayout.addView((View) o, param);
            definedScrollView.addView(mLinearLayout);
        }
    }
}
