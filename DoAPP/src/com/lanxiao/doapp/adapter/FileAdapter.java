package com.lanxiao.doapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.entity.MyFile;

import java.util.List;

/**
 * Created by Thinkpad on 2015/12/3.
 */
public class FileAdapter extends MBaseAdapter {
    List<MyFile> fileItem;
    Context context;
    public FileAdapter(List<MyFile> fileItem,Context context){
        this.fileItem=fileItem;
        this.context=context;
    }
    @Override
    public int getCount() {
        return fileItem.size();
    }

    @Override
    public Object getItem(int i) {
        return fileItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;

        if(view==null){
            view= LayoutInflater.from(context).inflate(R.layout.fileitem, viewGroup,false);
            holder=new ViewHolder();
            holder.ivFileIcon=(ImageView) view.findViewById(R.id.iv_fileicon);
            holder.tvFileName=(TextView) view.findViewById(R.id.tv_filename);
            holder.tvFileNum=(TextView) view.findViewById(R.id.tv_filenumber);
            holder.tvFlieTime=(TextView) view.findViewById(R.id.tv_filecreatetime);
            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }
        Log.i("tag", fileItem.size() + "zzz");
        holder.ivFileIcon.setImageResource(fileItem.get(i).getIcon());
        holder.tvFileName.setText(fileItem.get(i).getFile_Name());
        holder.tvFileNum.setText(fileItem.get(i).getFile_lenth());
        holder.tvFlieTime.setText(fileItem.get(i).getFile_CreateTime());
        return view;
    }
    class ViewHolder{
        ImageView ivFileIcon;
        TextView tvFileName;
        TextView tvFileNum;
        TextView tvFlieTime;
    }
}
