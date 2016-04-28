package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.entity.SignInLogItem;
import com.lidroid.xutils.HttpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignInLogAdapter extends BaseAdapter {

    private List<SignInLogItem> list;
    private Context context;

    public SignInLogAdapter(Context context, List<SignInLogItem> list
    ) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.signin_log_item, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        SignInLogItem si=list.get(position);
        holder.tvSigninLogStats.setVisibility(View.GONE);
        holder.rlSigninLogTwo.setVisibility(View.GONE);
        holder.tvSigninLogUserid.setText(si.getUserId());
        holder.tvSigninLogFristtime.setText(si.getFirstTime());
        holder.tvSigninLogFristaddress.setText(si.getFirstAddress());
        if(si.getTwoSatuas().equals("1")){
            holder.rlSigninLogTwo.setVisibility(View.VISIBLE);
            holder.tvSigninLogTwotime.setText(si.getTwoTime());
            holder.tvSigninLogTwoaddress.setText(si.getTwoAddress());
        }else {
            holder.tvSigninLogStats.setVisibility(View.VISIBLE);

        }
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_signinLog_userid)
        TextView tvSigninLogUserid;
        @InjectView(R.id.tv_signinLog_fristtime)
        TextView tvSigninLogFristtime;
        @InjectView(R.id.tv_signinLog_fristaddress)
        TextView tvSigninLogFristaddress;
        @InjectView(R.id.tv_signinLog_twotime)
        TextView tvSigninLogTwotime;
        @InjectView(R.id.tv_signinLog_twoaddress)
        TextView tvSigninLogTwoaddress;
        @InjectView(R.id.rl_signinLog_two)
        RelativeLayout rlSigninLogTwo;
        @InjectView(R.id.tv_signinLog_stats)
        TextView tvSigninLogStats;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}

