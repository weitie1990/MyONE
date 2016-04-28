package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easemob.easeui.domain.ContantTongShi;
import com.easemob.easeui.domain.EaseUser;
import com.example.doapp.R;
import com.lanxiao.doapp.untils.util.DisplayUtil;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thinkpad on 2016/4/22.
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    // 用来控制CheckBox的选中状况
    // 用来控制CheckBox的选中状况
    private static HashMap<String, Boolean> isSelected;
    private int checkGroup=-1;
    List<String> group_list=new ArrayList<>();
    List<List<EaseUser>> sumToshiforeaseuser=new ArrayList<>();
    List<List<ContantTongShi>> sumContantTongShi=new ArrayList<>();
    public MyExpandableListViewAdapter(Context context,List<String>  group_list,List<List<EaseUser>> item_list,List<List<ContantTongShi>> sumContantTongShi)
    {
        this.context = context;
        this.group_list=group_list;
        this.sumContantTongShi=sumContantTongShi;
        this.sumToshiforeaseuser=item_list;
        map=new HashMap<>();
        for(List<EaseUser> list:item_list){
            for(EaseUser eu:list) {
                map.put(eu.getUserId(), false);
            }
        }
    }
    @Override
    public int getGroupCount() {
        return group_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return sumContantTongShi.get(groupPosition).size()+sumToshiforeaseuser.get(groupPosition).size();
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
                if(childPosition<sumContantTongShi.get(groupPosition).size()){
                    return TYPE_1;
                }else  {
                    return TYPE_2;
                }
    }

    @Override
    public int getChildTypeCount() {
        return 3;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(getChildType(groupPosition,childPosition)==TYPE_1){
            return sumContantTongShi.get(groupPosition).get(childPosition);
        }else {
            return sumToshiforeaseuser.get(groupPosition).get(childPosition);
        }
    }
    private static int TYPE_1=1;
    private static int TYPE_2=2;

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupHolder groupHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.expendlist_group, parent,false);
            groupHolder = new GroupHolder();
            groupHolder.txt = (TextView)convertView.findViewById(R.id.group_text);
            groupHolder.img = (ImageView)convertView.findViewById(R.id.group_image);
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (GroupHolder)convertView.getTag();
        }

        if (!isExpanded) {
            groupHolder.img.setBackgroundResource(android.R.drawable.ic_media_play);
        }
        else
        {
            groupHolder.img.setBackgroundResource(android.R.drawable.arrow_down_float);
        }

        groupHolder.txt.setText(group_list.get(groupPosition));
        return convertView;
    }
    private Map<String,Boolean> map;
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        GroupHolder itemHolder = null;
        ItemHolder holder=null;
        int type=getChildType(groupPosition,childPosition);
        LogUtils.i("groupPosition:"+groupPosition+","+"childPosition:"+childPosition+","+"type:"+type);
        if(type==TYPE_1){
            ContantTongShi eu=sumContantTongShi.get(groupPosition).get(childPosition);
            if (convertView == null)
            {
                TextView tv=new TextView(context);
                LinearLayout.LayoutParams lp=new AutoLinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtil.dip2px(context, 60));
                tv.setLayoutParams(lp);
                tv.setGravity(Gravity.CENTER_VERTICAL);
                tv.setTextSize(16);
                tv.setPadding(DisplayUtil.dip2px(context, 30),0,0,0);
                tv.setBackgroundResource(R.color.backgroud);

                itemHolder = new GroupHolder();
                // 选择框checkbox
                itemHolder.txt =tv;
                convertView=tv;
                tv.setTag(itemHolder);
            }
            else
            {
                itemHolder = (GroupHolder)convertView.getTag();
            }
            itemHolder.txt.setText(eu.getName());
        }
        else if(type==TYPE_2){
            int position=childPosition-sumContantTongShi.get(groupPosition).size();
            final EaseUser eu = sumToshiforeaseuser.get(groupPosition).get(position);
            if(convertView==null) {
                convertView=LayoutInflater.from(context).inflate(R.layout.row_contact_with_checkbox,parent,false);
                holder=new ItemHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                holder.avatarView = (ImageView) convertView.findViewById(R.id.avatar);
                holder.nameView = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            }else {
                holder= (ItemHolder) convertView.getTag();
            }
            Utils.setAver(eu.getAvatar(), holder.avatarView);
            holder.nameView.setText(eu.getNickName());
            holder.checkBox.setButtonDrawable(R.drawable.checkbox_bg_selector);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //isCheckedArray[position]=isChecked;
                    //map.put(list.get(position).getUserId(),isChecked);
                    map.put(eu.getUserId(),isChecked);
                }
            });
            holder.checkBox.setChecked(map.get(eu.getUserId()));
            holder.checkBox.setClickable(false);
            holder.checkBox.setEnabled(false);
        }
        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public class GroupHolder
    {
        public TextView txt;

        public ImageView img;
    }

    public class ItemHolder
    {

        public CheckBox checkBox;
        public ImageView avatarView;
        public TextView nameView;
    }
    public  Map<String, Boolean> getIsSelected() {
        return map;
    }


}
