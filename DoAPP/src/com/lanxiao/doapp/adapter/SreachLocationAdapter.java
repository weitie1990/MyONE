package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.doapp.R;

import java.util.List;


/**
 * 当前位置附近pio
 */
public class SreachLocationAdapter extends BaseAdapter {

	private Context mContext;
	private List<PoiInfo> pois;
	public SreachLocationAdapter(Context context, List<PoiInfo> pois) {
		mContext = context;
		this.pois = pois;
	}

	public int getCount() {
		return pois.size();
	}

	public Object getItem(int position) {
		return pois.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.currentlocation_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView
					.findViewById(R.id.currentlocation_item_icon);
			holder.name = (TextView) convertView
					.findViewById(R.id.currentlocation_item_name);
			holder.count = (TextView) convertView
					.findViewById(R.id.currentlocation_item_count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			holder.icon
					.setImageResource(R.drawable.v5_0_1_poi_list_icon_selected);
		} else {
			holder.icon.setImageResource(R.drawable.v5_0_1_poi_list_icon);
		}
		PoiInfo poiInfo = pois.get(position);
		holder.name.setText(poiInfo.name);
		Log.i("name",poiInfo.name);
		holder.count.setText(poiInfo.address);
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView name;
		TextView count;
	}
}
