package com.lanxiao.doapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.ApplyItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TeamSumAdapter extends MBaseAdapter {
	Context context;
	List<ApplyItem> list;

	public TeamSumAdapter(Context context, List<ApplyItem> list) {
		this.context = context;
		this.list = list;
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
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.listitemforcompany,parent,false);
			holder=new Holder();
			holder.tv_name=(TextView) convertView.findViewById(R.id.tv_team_teamname);
			holder.bt_add=(Button) convertView.findViewById(R.id.btn_team_teamjoin);
			convertView.setTag(holder);
	}else{
			holder=(Holder) convertView.getTag();
	}
		holder.tv_name.setText(list.get(position).getName());
		boolean isAdd=list.get(position).isAdd();
		if(isAdd){
			holder.bt_add.setText("退出");
		}else {
			holder.bt_add.setText("加入");
		}
		holder.bt_add.setOnClickListener(new MyClick(position));
		return convertView;
	}
	public class MyClick implements View.OnClickListener{
		int p;
		public MyClick(int p){
			this.p=p;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.ll_team_info:
					//joinTeam(p);
					break;
			}
		}
	}
	public void DateChange(	List<ApplyItem> list){
		if(list!=null){
			this.list=list;
		}
		notifyDataSetChanged();
	}
	/**
	 * 加入部门       todo 暂时还没有加入下级部门的接口
	 */
	public void joinTeam(final int p){
		final ProgressDialog pd=new ProgressDialog(context);
		pd.setTitle("正在添加");
		pd.show();
		final RequestParams params = new RequestParams("UTF-8");
		LogUtils.i(DemoHelper.getInstance().getCurrentUsernName());
		params.addBodyParameter("userid", DemoHelper.getInstance().getCurrentUsernName());
		params.addBodyParameter("fieldname","DepartmentID,Department");
		params.addBodyParameter("fieldvalue",list.get(p).getInfo()+","+list.get(p).getName());
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpRequest.HttpMethod.POST, Api.UPDATAUSER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				pd.dismiss();
				LogUtils.i(responseInfo.result);
				try {
					JSONObject jb = new JSONObject(responseInfo.result);
					if (jb.optString("result").equals("1")) {
						Utils.showToast("更新成功", context);
						for (int i = 0; i < list.size(); i++) {
							if (i == p) {
								list.get(i).setAdd(true);
							} else {
								list.get(i).setAdd(false);
							}
						}
						//btn.setText("退出");
						//同时更新本地数据
						String[] pam = {"Department", "DepartmentID"};
						PersonInfo pf = DemoApplication.getInstance().getDb().findFirst(Selector.from(PersonInfo.class).where("userid", "=", DemoHelper.getInstance().getCurrentUsernName()));
						pf.setDepartmentID(list.get(p).getInfo());
						pf.setDepartment(list.get(p).getName());
						DemoApplication.getInstance().getDb().update(pf);
						refsh();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				Utils.showToast("无网络，请检查网络设置", context);
				pd.dismiss();
			}
		});
	}
	public void refsh(){
		notifyDataSetChanged();
	}
}
