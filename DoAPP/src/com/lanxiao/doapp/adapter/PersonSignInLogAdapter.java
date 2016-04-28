package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.easeui.Conversion;
import com.example.doapp.R;
import com.lanxiao.doapp.entity.PersonSignInLogItem;
import com.lanxiao.doapp.myView.CircularImage;
import com.lanxiao.doapp.untils.util.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PersonSignInLogAdapter extends BaseAdapter {

    private List<PersonSignInLogItem> list;
    private Context context;

    public PersonSignInLogAdapter(Context context, List<PersonSignInLogItem> list
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
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.singin_log_person_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PersonSignInLogItem si = list.get(position);
        Utils.setAver(Conversion.getInstance().getHeadUrl(),holder.signinLogPersonUserpic);
        holder.signinLogPersonUsername.setText(si.getUserName());
        holder.signinLogPersonItemTime.setText(si.getSingintime());
        holder.signinLogPersonBeiju.setText("备注："+si.getSignbeiju());
        holder.signinLogPersonAddress.setText(si.getSignaddress());
        holder.signinLogPersonImg.setOnClickListener(new MyOnClickListion(position));
        return convertView;
    }
    private class MyOnClickListion implements View.OnClickListener{
        int p;
        public MyOnClickListion(int p){
            this.p=p;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
              case R.id.signin_log_person_img:

                break;
            }
        }
    }

    static class ViewHolder {
        @InjectView(R.id.signin_log_person_userpic)
        CircularImage signinLogPersonUserpic;
        @InjectView(R.id.signin_log_person_username)
        TextView signinLogPersonUsername;
        @InjectView(R.id.signin_log_person_item_time)
        TextView signinLogPersonItemTime;
        @InjectView(R.id.signin_log_person_beiju)
        TextView signinLogPersonBeiju;
        @InjectView(R.id.signin_log_person_img)
        ImageView signinLogPersonImg;
        @InjectView(R.id.signin_log_person_address)
        TextView signinLogPersonAddress;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}

