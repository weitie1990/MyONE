package com.lanxiao.doapp.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.doapp.R;
import com.lanxiao.doapp.entity.Apply1;
import com.lanxiao.doapp.entity.ApplyItem;
import com.lanxiao.doapp.untils.util.Utils;
import com.zhy.autolayout.utils.AutoUtils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ApplyAdapter1 extends MBaseAdapter {
	Context context;
	List<Apply1> list;

	public ApplyAdapter1(Context context, List<Apply1> list) {
		this.context = context;
		this.list = list;
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

	class Holder {
		ImageView iv;
		TextView tv_name, tv_info;
		ImageView bt_add;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		Log.i("tag","list.size():"+list.size());
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.listitemforapply,parent,false);
			holder=new Holder();
			holder.iv=(ImageView) convertView.findViewById(R.id.iv_iconforapply);
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_appforname);
			holder.tv_info=(TextView) convertView.findViewById(R.id.tv_appforinfo);
			holder.bt_add=(ImageView) convertView.findViewById(R.id.ib_addapply);
			convertView.setTag(holder);
			AutoUtils.autoSize(convertView);
	}else{
			holder=(Holder) convertView.getTag();
	}
		holder.iv.setImageResource(Utils.forImage(list.get(position).getLogo()));
		holder.tv_name.setText(list.get(position).getName());
		return convertView;
	}

}
