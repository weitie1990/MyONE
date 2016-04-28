package com.easemob.easeui.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.easeui.R;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.utils.EaseUserUtils;
import com.easemob.util.EMLog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

public class EaseContactAdapter extends ArrayAdapter<EaseUser> implements SectionIndexer{
    private static final String TAG = "ContactAdapter";
    List<String> list;
    List<EaseUser> userList;
    List<EaseUser> copyUserList;
    private LayoutInflater layoutInflater;
    private SparseIntArray positionOfSection;
    private SparseIntArray sectionOfPosition;
    private int res;
    private MyFilter myFilter;
    private boolean notiyfyByFilter;

    public EaseContactAdapter(Context context, int resource, List<EaseUser> objects) {
        super(context, resource, objects);
        LogUtils.i("EaseContactAdapter");
        this.res = resource;
        this.userList = objects;
        copyUserList = new ArrayList<EaseUser>();
        copyUserList.addAll(objects);
        layoutInflater = LayoutInflater.from(context);
    }
    
    private static class ViewHolder {
        ImageView avatar;
        TextView nameView;
        TextView headerView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            if(res == 0)
                convertView = layoutInflater.inflate(R.layout.ease_row_contact, null);
            else
                convertView = layoutInflater.inflate(res, null);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.headerView = (TextView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        
        final EaseUser user = getItem(position);
        if(user == null)
            Log.d("ContactAdapter", position + "");
        String username = user.getNickName();
        String header = user.getInitialLetter().substring(0, 1).toUpperCase();
        
        if (position == 0 || header != null && !header.equals(getItem(position - 1).getInitialLetter().substring(0, 1).toUpperCase())) {
            if (TextUtils.isEmpty(header)) {
                holder.headerView.setVisibility(View.GONE);
            } else {
                holder.headerView.setVisibility(View.VISIBLE);
                holder.headerView.setText(header);
            }
        } else {
            holder.headerView.setVisibility(View.GONE);
        }
        //设置昵称
        EaseUserUtils.setUserNick(username, holder.nameView);
        //设置头像 头像点击事件
        EaseUserUtils.setAver(user.getAvatar(),holder.avatar);

        if(primaryColor != 0)
            holder.nameView.setTextColor(primaryColor);
        if(primarySize != 0)
            holder.nameView.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if(initialLetterBg != null)
            holder.headerView.setBackgroundDrawable(initialLetterBg);
        if(initialLetterColor != 0)
            holder.headerView.setTextColor(initialLetterColor);
        
        return convertView;
    }
    
    @Override
    public EaseUser getItem(int position) {
        return super.getItem(position);
    }
    
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPositionForSection(int section) {
        return positionOfSection.get(section);
    }

    @Override
    public int getSectionForPosition(int position) {
        return sectionOfPosition.get(position);
    }
    //
    @Override
    public Object[] getSections() {
        positionOfSection = new SparseIntArray();
        sectionOfPosition = new SparseIntArray();
        int count = getCount();
        list = new ArrayList<String>();
        list.add(getContext().getString(R.string.search_header));
        positionOfSection.put(0, 0);
        sectionOfPosition.put(0, 0);
        for (int i = 1; i < count; i++) {

            String letter = getItem(i).getInitialLetter();
            EMLog.d(TAG, "contactadapter getsection getHeader:" + letter + " name:" + getItem(i).getUsername());
            int section = list.size() - 1;
            if (list.get(section) != null && !list.get(section).equals(letter)) {
                list.add(letter);
                section++;
                positionOfSection.put(section, i);
            }
            sectionOfPosition.put(i, section);
        }
        return list.toArray(new String[list.size()]);
    }
    
    @Override
    public Filter getFilter() {
        if(myFilter==null){
            myFilter = new MyFilter(userList);
        }
        return myFilter;
    }
    
    protected class  MyFilter extends Filter{
        List<EaseUser> mOriginalList = null;
        
        public MyFilter(List<EaseUser> myList) {
            this.mOriginalList = myList;
        }

        @Override
        protected synchronized FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(mOriginalList==null){
                mOriginalList = new ArrayList<EaseUser>();
            }
            EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
            EMLog.d(TAG, "contacts copy size: " + copyUserList.size());
            
            if(prefix==null || prefix.length()==0){
                results.values = copyUserList;
                results.count = copyUserList.size();
            }else{
                String prefixString = prefix.toString().toLowerCase();
                final int count = mOriginalList.size();
                final ArrayList<EaseUser> newValues = new ArrayList<EaseUser>();
                //用用户的拼音来解析出快速查找功能
                for(int i=0;i<count;i++){
                    final EaseUser user = mOriginalList.get(i);
                    String username = user.getNickName().toLowerCase();
                    String infilt=user.getInitialLetter().toLowerCase();
                    if(username.startsWith(prefixString)||prefixString.equals(infilt)){
                        newValues.add(user);
                    }
                    else{
                         final String[] words = username.split(" ");
                         final int wordCount = words.length;
    
                         // Start at index 0, in case valueText starts with space(s)
                         for (int k = 0; k < wordCount; k++) {
                             if (words[k].startsWith(prefixString)) {
                                 newValues.add(user);
                                 break;
                             }
                         }
                    }
                }
                results.values=newValues;
                results.count=newValues.size();
            }
            EMLog.d(TAG, "contacts filter results size: " + results.count);
            return results;
        }

        @Override
        protected synchronized void publishResults(CharSequence constraint,
                FilterResults results) {
            userList.clear();
            LogUtils.i("userList" + (userList == null));
            LogUtils.i("results"+(results==null));
            userList.addAll((List<EaseUser>)results.values);
            EMLog.d(TAG, "publish contacts filter results size: " + results.count);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
                notiyfyByFilter = false;
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
    
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(!notiyfyByFilter){
            copyUserList.clear();
            copyUserList.addAll(userList);
        }
    }
    
    protected int primaryColor;
    protected int primarySize;
    protected Drawable initialLetterBg;
    protected int initialLetterColor;

    public EaseContactAdapter setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
        return this;
    }


    public EaseContactAdapter setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
        return this;
    }

    public EaseContactAdapter setInitialLetterBg(Drawable initialLetterBg) {
        this.initialLetterBg = initialLetterBg;
        return this;
    }

    public EaseContactAdapter setInitialLetterColor(int initialLetterColor) {
        this.initialLetterColor = initialLetterColor;
        return this;
    }
    
}
