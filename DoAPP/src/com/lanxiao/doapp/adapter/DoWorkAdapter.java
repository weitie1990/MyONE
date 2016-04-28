package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.entity.ApplyItem;
import com.lanxiao.doapp.entity.DoWorkItem;
import com.lanxiao.doapp.entity.Work;
import com.lanxiao.doapp.untils.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class DoWorkAdapter extends MBaseAdapter {
	Context context;
	List<Work> list;

	public DoWorkAdapter(Context context, List<Work> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()+2;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position==list.size()){
			return null;
		}
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		ImageView iv;
		TextView tv_name;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		Log.i("tag","list.size():"+list.size());
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.gridview_item,parent,false);
			holder=new Holder();
			holder.iv=(ImageView) convertView.findViewById(R.id.ItemImage);
			holder.tv_name=(TextView) convertView.findViewById(R.id.ItemText);
			convertView.setTag(holder);
	}else{
			holder=(Holder) convertView.getTag();
	}
		//如果是倒数第二项，显示添加按钮
		if(position==list.size()){
			holder.tv_name.setVisibility(View.GONE);
			holder.iv.setVisibility(View.GONE);
			//如果是最后一项，显示添加按钮
		}else
		if(position==list.size()+1){
			holder.tv_name.setVisibility(View.GONE);
			holder.iv.setImageResource(R.drawable.ib_tab_add);
		}else {
			holder.iv.setImageResource(Utils.forImage(list.get(position).getLogo()));
			holder.tv_name.setText(list.get(position).getName());
		}

		return convertView;
	}

}
