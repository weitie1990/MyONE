package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.activity.LoginActivity;
import com.lanxiao.doapp.activity.RegisterActivity;
import com.lanxiao.doapp.activity.RegisterPage2Activity;
import com.lanxiao.doapp.activity.SetActivity_set;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.PersonInfo;
import com.lanxiao.doapp.http.Api;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class CompanyAdapter extends MBaseAdapter {
    FragmentActivity context;
    List<Map<String, String>> list;
    String phone;
    String passWord;
    String type;
    public CompanyAdapter(FragmentActivity context, List<Map<String, String>> list, String phone, String passWord,String type) {
        this.context = context;
        this.list = list;
        this.passWord = passWord;
        this.phone = phone;
        this.type=type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    class Holder {
        ImageView iv;
        TextView tv_name, tv_info;
        Button bt_add;
        LinearLayout ll_team_info;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        Log.i("tag", "list.size():" + list.size());
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitemforcompany, parent, false);
            holder = new Holder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_team_teamname);
            holder.bt_add = (Button) convertView.findViewById(R.id.btn_team_teamjoin);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setText(list.get(position).get("companyname"));
        holder.bt_add.setOnClickListener(new MyClick(position));
        return convertView;
    }

    public class MyClick implements View.OnClickListener {
        int p;

        public MyClick(int p) {
            this.p = p;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_team_teamjoin:
                    joinUnit(p);
                    break;
            }
        }
    }

    public void joinUnit(final int p) {
        RequestParams params = new RequestParams("UTF-8");
        params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
        params.addBodyParameter("fieldname", "CompanyID,CompanyName");
        params.addBodyParameter("fieldvalue", list.get(p).get("companyid") + "," + list.get(p).get("companyname"));
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtils.i(responseInfo.result);
                try {
                    JSONObject jb = new JSONObject(responseInfo.result);
                    if (jb.optString("result").equals("1")) {
                        Utils.showToast("加入成功", context);
                        LogUtils.i("mlist");
                        PersonInfo pf = null;
                        try {
                            pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
                            pf.setCompanyID(list.get(p).get("companyid"));
                            pf.setCompanyName(list.get(p).get("companyname"));
                            LogUtils.i(list.get(p).get("companyid") + "," + list.get(p).get("companyname"));
                            DemoApplication.getInstance().getDb().update(pf);
                            Utils.showToast("加入成功", context);
                            if(type!=null&&type.equals("1")) {
                                Intent intent = new Intent(context, SetActivity_set.class);
                                context.startActivity(intent);
                            }else {
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.putExtra("cellphonenumber", phone);
                                intent.putExtra("password", passWord);
                                context.startActivity(intent);
                            }
                            context.finish();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast("加入失败", context);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Utils.showToast("无网络，请检查网络设置", context);
            }
        });
    }

    public void resh(List<Map<String, String>> list) {
        if(list!=null){
            this.list=list;
            notifyDataSetChanged();
        }
    }

}
