package com.lanxiao.doapp.framment;
/**
 * "应用"的第一层frament
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.util.NetUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.CurrentLocationActivity;
import com.lanxiao.doapp.activity.MeetingDoActivity;
import com.lanxiao.doapp.activity.MeetingWebActivity;
import com.lanxiao.doapp.adapter.ApplyAdapter1;
import com.lanxiao.doapp.adapter.ApplyAdapter2;
import com.lanxiao.doapp.adapter.ApplyAdapter3;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.chatui.applib.controller.DemoHelper;
import com.lanxiao.doapp.entity.Apply1;
import com.lanxiao.doapp.entity.Apply2;
import com.lanxiao.doapp.entity.Apply3;
import com.lanxiao.doapp.myView.ListViewForScrollView;
import com.lanxiao.doapp.untils.util.Utils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

import java.util.List;

public class Frament_applay extends MainBaseFragment implements View.OnClickListener{
    private View rootView;
    private FragmentTransaction fs;
    TextView iv_applay_home,iv_applay_work,iv_applay_commpany;
    ListViewForScrollView lv_work,lv_life,lv_yuanqu;
    List<Apply1> list_life;
    List<Apply2> list_work;
    List<Apply3> list_yuanqu;
    ApplyAdapter2 applyAdapter_1_work;
    ApplyAdapter1 applyAdapter_1_life;
    ApplyAdapter3 applyAdapter_1_yuanqu;
    ScrollView scroll_view;
    private FrameLayout fl_error_item;
    private TextView errorText;
    public static Frament_applay newInstance() {
        Frament_applay frament_applay = new Frament_applay();
        return frament_applay;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void initView() {
        fl_error_item= (FrameLayout) getView().findViewById(R.id.fl_error_item);
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        fl_error_item.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        iv_applay_home = (TextView) rootView.findViewById(R.id.iv_applay_home);
        iv_applay_work = (TextView) rootView.findViewById(R.id.iv_applay_work);
        iv_applay_commpany= (TextView) rootView.findViewById(R.id.iv_applay_commpany);
        lv_work= (ListViewForScrollView) rootView.findViewById(R.id.lv_work);
        lv_life= (ListViewForScrollView) rootView.findViewById(R.id.lv_life);
        lv_yuanqu= (ListViewForScrollView) rootView.findViewById(R.id.lv_yuanqu);
        scroll_view= (ScrollView) rootView.findViewById(R.id.scroll_view);
        iv_applay_home.setOnClickListener(this);
        iv_applay_work.setOnClickListener(this);
        iv_applay_commpany.setOnClickListener(this);
        iv_applay_home.setSelected(true);
        init();
    }
    @Override
    protected void setUpView() {



    }

    @Override
    protected void onConnectionConnected() {
        fl_error_item.setVisibility(View.GONE);
    }

    @Override
    protected void onConnectionDisconnected() {
        fl_error_item.setVisibility(View.VISIBLE);
        if (NetUtils.hasNetwork(getActivity())){
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.framenttwo_applay, container, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        lv_life.setVisibility(View.GONE);
        lv_work.setVisibility(View.GONE);
        lv_yuanqu.setVisibility(View.GONE);
        iv_applay_home.setSelected(false);
        iv_applay_work.setSelected(false);
        iv_applay_commpany.setSelected(false);
        iv_applay_work.setTextColor(getResources().getColor(R.color.main_text));
        iv_applay_home.setTextColor(getResources().getColor(R.color.main_text));
        iv_applay_commpany.setTextColor(getResources().getColor(R.color.main_text));
        switch (v.getId()){
            case R.id.iv_applay_home:
                iv_applay_home.setSelected(true);
                iv_applay_home.setTextColor(getResources().getColor(R.color.main_kuang));
                lv_life.setVisibility(View.VISIBLE);
                scroll_view.smoothScrollTo(0, 0);
                break;
            case R.id.iv_applay_work:
                iv_applay_work.setSelected(true);
                iv_applay_work.setTextColor(getResources().getColor(R.color.main_kuang));
                lv_work.setVisibility(View.VISIBLE);
                scroll_view.smoothScrollTo(0, 0);
                break;
            case R.id.iv_applay_commpany:
                iv_applay_commpany.setSelected(true);
                iv_applay_commpany.setTextColor(getResources().getColor(R.color.main_kuang));
                lv_yuanqu.setVisibility(View.VISIBLE);
                scroll_view.smoothScrollTo(0, 0);
                break;
        }
    }
    private void init(){
        try {
            list_life = DemoApplication.getInstance().getDb().findAll(Apply1.class);
            list_work = DemoApplication.getInstance().getDb().findAll(Apply2.class);
           list_yuanqu=DemoApplication.getInstance().getDb().findAll(Apply3.class);
            iv_applay_home.setText(list_life.get(0).getTitle());
            iv_applay_work.setText(list_work.get(0).getTitle());
            iv_applay_commpany.setText(list_yuanqu.get(0).getTitle());
        } catch (DbException e) {
            e.printStackTrace();
        }
        lv_life.setVisibility(View.VISIBLE);
        applyAdapter_1_work =new ApplyAdapter2(getActivity(),list_work);
        lv_work.setAdapter(applyAdapter_1_work);
        applyAdapter_1_life =new ApplyAdapter1(getActivity(),list_life);
        lv_life.setAdapter(applyAdapter_1_life);
        applyAdapter_1_yuanqu=new ApplyAdapter3(getActivity(),list_yuanqu);
        lv_yuanqu.setAdapter(applyAdapter_1_yuanqu);
        scroll_view.smoothScrollTo(0, 0);
        lv_work.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Apply2 apply2 = list_work.get(position);
                LogUtils.i("work:" + apply2.getType());
                if (apply2.getType().equals("app")) {
                    String tage = apply2.getTarget();
                    Utils.intoApp(tage,getActivity());
                } else if (apply2.getType().equals("web")) {
                    String tage = apply2.getTarget();
                    intoWeb(tage + "?userid=" + DemoHelper.getInstance().getCurrentUsernName(), apply2.getName());
                }
            }
        });
        lv_life.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Apply1 apply1=list_life.get(position);
                LogUtils.i("work:" + apply1.getType());
                if(apply1.getType().equals("app")){
                    String tage=apply1.getTarget();
                    Utils.intoApp(tage,getActivity());
                }else if(apply1.getType().equals("web")){
                    String tage=apply1.getTarget();
                    intoWeb(tage +"?userid="+DemoHelper.getInstance().getCurrentUsernName(), apply1.getName());
                }
            }
        });
        lv_yuanqu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Apply3 apply3=list_yuanqu.get(position);
                LogUtils.i("work:" + apply3.getType());
                if(apply3.getType().equals("app")){
                    String tage=apply3.getTarget();
                    Utils.intoApp(tage,getActivity());
                }else if(apply3.getType().equals("web")){
                    String tage=apply3.getTarget();
                    intoWeb(tage +"?userid="+DemoHelper.getInstance().getCurrentUsernName(), apply3.getName());
                }
            }
        });
    }

    private void intoWeb(String url,String type){
        Intent intent1=new Intent(getContext(),MeetingWebActivity.class);
        intent1.putExtra("result",url);
        intent1.putExtra("type",type);
        startActivity(intent1);
    }
}
