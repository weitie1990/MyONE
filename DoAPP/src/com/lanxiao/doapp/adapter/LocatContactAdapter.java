package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.untils.util.Utils;
import com.lanxiao.doapp.entity.Contact;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表适配器。
 * 
 * @author guolin
 */
public class LocatContactAdapter extends BaseAdapter implements SectionIndexer{
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	List<String> list_string;

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;
	List<Contact> list;
	Context context;
	/**
	 * 字母表分组工具
	 */
	private SectionIndexer mIndexer;

	public LocatContactAdapter(Context context, int textViewResourceId, List<Contact> objects) {
		resource = textViewResourceId;
		this.context=context;
		list=objects;
	}
	public void setData(List<Contact> list){
		if(list!=null){
			this.list=list;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact = list.get(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(context).inflate(resource, parent,false);
			AutoUtils.autoSize(layout);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView j_name = (TextView) layout.findViewById(R.id.tv_contant_name);
		TextView name= (TextView) layout.findViewById(R.id.tv_appforname);
		TextView number= (TextView) layout.findViewById(R.id.tv_appforinfo);
		LinearLayout sortKeyLayout = (LinearLayout) layout.findViewById(R.id.sort_key_layout);
		TextView sortKey = (TextView) layout.findViewById(R.id.sort_key);
		name.setText(contact.getName());
		number.setText(contact.getNumber());
		j_name.setText(Utils.getSubString(contact.getName()));
		int section = mIndexer.getSectionForPosition(position);
		if (position == mIndexer.getPositionForSection(section)) {
			sortKey.setText(contact.getSortKey());
			sortKeyLayout.setVisibility(View.VISIBLE);
		} else {
			sortKeyLayout.setVisibility(View.GONE);
		}
		return layout;
	}

	/**
	 * 给当前适配器传入一个分组工具。
	 * 
	 * @param indexer
	 */
	public void setIndexer(SectionIndexer indexer) {
		mIndexer = indexer;
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		list_string = new ArrayList<String>();
		list_string.add("Search");
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = list.get(i).getSortKey();
			int section = list_string.size() - 1;
			if (list_string.get(section) != null && !list.get(section).equals(letter)) {
				list_string.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list_string.toArray(new String[list_string.size()]);
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return positionOfSection.get(sectionIndex);
	}

	@Override
	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}
}
