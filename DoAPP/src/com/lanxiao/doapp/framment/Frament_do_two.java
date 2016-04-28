package com.lanxiao.doapp.framment;
/**
 * doing界面第二层frament
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easemob.util.NetUtils;
import com.example.doapp.R;
import com.lanxiao.doapp.activity.AddContactActivity;
import com.lanxiao.doapp.activity.SetActivity_set;
import com.lanxiao.doapp.myView.ActionItem;
import com.lanxiao.doapp.myView.DoPopup;
import com.lanxiao.doapp.myView.ScllorTabView;
import com.lanxiao.doapp.myView.TitlePopup;
import com.lanxiao.doapp.myView.Util;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;


public class Frament_do_two extends MainBaseFragment {
    private ViewPager viewPager;
    private ArrayList<Frament_attention_reficive> fragmentList;
   // private TextView tv1, tv2, tv3;
    ScllorTabView scllorTabView;
    //Drawable deafault, select;
    Boolean isFirist=false;
    MyFragmentPagerAdapter adapter;
    private TitlePopup titlePopup;
    private DoPopup doPopup;
    private DoPopup doPopupForattent;
    ScrollView scrollview_doing;
    private RelativeLayout Rl_main;
    private FrameLayout fl_error_item;
    private TextView errorText,tv_do_ismy,tv_do_attent;
    private ImageView tv_do_img_ismy,tv_do_img_attent;
    private RelativeLayout friendgroup,pref;
    private String [] doItems={"待办","交办","已办","微博"};
    private String [] attentItems={"全部","关注"};
    private String [] allAttentItems={"全部","关注","待办","交办","已办","微博"};
    int currentFrament=0;
    public static Frament_do_two newInstance() {
        Frament_do_two frament_do_two = new Frament_do_two();
        return frament_do_two;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentList = new ArrayList<Frament_attention_reficive>();
        for (int i=0;i<allAttentItems.length;i++) {
            Frament_attention_reficive frament_attention = Frament_attention_reficive.newInstance(i+"");
            fragmentList.add(frament_attention);
        }
       /* frament_ismY = Frament_ismY.newInstance();
        frament_Friends = Frament_Friends.newInstance();
        frament_replir=Frament_replir.newInstance();
        frament_send=Frament_send.newInstance();
        frament_blob=Frament_Blob.newInstance();*/
       /* fragmentList.add(frament_attention);
        fragmentList.add(frament_Friends);
        fragmentList.add(frament_ismY);
        fragmentList.add(frament_replir);
        fragmentList.add(frament_send);
        fragmentList.add(frament_blob);*/
        //select = getResources().getDrawable(R.drawable.buttonstyle_red);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.framenttwo_doing, container, false);

        return v;
    }

    private void setBackgroundColor(int index) {
       tv_do_ismy.setSelected(false);
        tv_do_attent.setSelected(false);
        tv_do_img_ismy.setVisibility(View.INVISIBLE);
        tv_do_img_attent.setVisibility(View.INVISIBLE);
        LogUtils.i("index:"+index);
        switch (index) {
            case 0:
                tv_do_attent.setSelected(true);
                tv_do_img_attent.setVisibility(View.VISIBLE);
                tv_do_attent.setText(allAttentItems[index]);
                break;
            case 1:
                tv_do_attent.setSelected(true);
                tv_do_img_attent.setVisibility(View.VISIBLE);
                tv_do_attent.setText(allAttentItems[index]);
                break;
            case 2:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                tv_do_ismy.setText(allAttentItems[index]);

                break;
            case 3:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                tv_do_ismy.setText(allAttentItems[index]);
                break;
            case 4:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                tv_do_ismy.setText(allAttentItems[index]);
                break;
            case 5:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                tv_do_ismy.setText(allAttentItems[index]);
                break;
        }
    }
    private void setActionColor(int index) {
        tv_do_ismy.setSelected(false);
        tv_do_attent.setSelected(false);
        tv_do_img_ismy.setVisibility(View.INVISIBLE);
        tv_do_img_attent.setVisibility(View.INVISIBLE);

        switch (index) {
            case 0:
                tv_do_attent.setSelected(true);
                tv_do_img_attent.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_do_attent.setSelected(true);
                tv_do_img_attent.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);

                break;
            case 3:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                break;
            case 4:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                break;
            case 5:
                tv_do_ismy.setSelected(true);
                tv_do_img_ismy.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void initView() {
        Rl_main= (RelativeLayout) getView().findViewById(R.id.Rl_main);
        viewPager = (ViewPager) getView().findViewById(R.id.viewPager);
        friendgroup= (RelativeLayout) getView().findViewById(R.id.friendgroup);
        pref= (RelativeLayout) getView().findViewById(R.id.pref);
        tv_do_ismy= (TextView) getView().findViewById(R.id.tv_do_ismy);
        tv_do_attent= (TextView) getView().findViewById(R.id.tv_do_attent);
        tv_do_img_attent= (ImageView) getView().findViewById(R.id.tv_do_img_attent);
        tv_do_img_ismy= (ImageView) getView().findViewById(R.id.tv_do_img_ismy);
        tv_do_ismy.setOnClickListener(new textViewListener(0));
        tv_do_attent.setOnClickListener(new textViewListener(1));
        adapter=new MyFragmentPagerAdapter(getFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        setBackgroundColor(0);
        LogUtils.i("Frament_do_two");
        fl_error_item= (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }

    @Override
    protected void setUpView() {
        //关闭预加载，默认一次只加载一个Fragment
        viewPager.setOffscreenPageLimit(1);
        friendgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SetActivity_set.class);
                getActivity().startActivity(intent);
            }
        });
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        fl_error_item.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        final View v=getView().findViewById(R.id.Rl_main);
        pref.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				titlePopup.show(v);
			}
		});
        //实例化标题栏弹窗
        titlePopup = new TitlePopup(getActivity(),Util.getScreenWidth(getContext())/2
        , ViewGroup.LayoutParams.WRAP_CONTENT);
        doPopup = new DoPopup(getActivity(),Util.getScreenWidth(getContext())/4
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<doItems.length;i++){
            doPopup.addAction(doItems[i]);
        }
        doPopupForattent = new DoPopup(getActivity(),Util.getScreenWidth(getContext())/4
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<attentItems.length;i++){
            doPopupForattent.addAction(attentItems[i]);
        }
        titlePopup.addAction(new ActionItem(getActivity(), "扫一扫",  R.drawable.qrcode_normal));
        titlePopup.addAction(new ActionItem(getActivity(), "加好友",  R.drawable.login_icon_account));
        DoPopup.OnItemOnClickListener onItemOnClickListenernew=new DoPopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(String item, int position) {
                if(position==0){
                    if(tv_do_ismy.isSelected()) {
                        currentFrament = 2;
                    }else {
                        currentFrament=0;
                    }
                }
                if(position==1){
                    if(tv_do_ismy.isSelected()) {
                        currentFrament = 3;
                    }else {
                        currentFrament=1;
                    }
                }
                if(position==2){
                        currentFrament=4;
                }
                if(position==3){
                    currentFrament=5;
                }
                LogUtils.i("currentFrament:"+currentFrament);
                    viewPager.setCurrentItem(currentFrament);
            }
        };
        TitlePopup.OnItemOnClickListener itemOnClickListener=new TitlePopup.OnItemOnClickListener(){
            @Override
            public void onItemClick(ActionItem item, int position) {
                if(position==0){
                    Intent intent=new Intent("android.intent.captureActivity");
                    startActivity(intent);

                }
                if(position==1){
                   /* Intent intent=new Intent(getActivity(), LocationContantActivity.class);
                    startActivity(intent);*/
                    Intent intent=new Intent(getActivity(), AddContactActivity.class);
                    startActivity(intent);

                }


            }
        };

        titlePopup.setItemOnClickListener(itemOnClickListener);
        doPopup.setItemOnClickListener(onItemOnClickListenernew);
        doPopupForattent.setItemOnClickListener(onItemOnClickListenernew);
    }

    @Override
    protected void onConnectionConnected() {
        LogUtils.i("on");
        fl_error_item.setVisibility(View.GONE);
    }

    @Override
    protected void onConnectionDisconnected() {
        LogUtils.i("dis");
        fl_error_item.setVisibility(View.VISIBLE);
        if (NetUtils.hasNetwork(getActivity())){
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    private class textViewListener implements View.OnClickListener {
        private int index = 0;

        public textViewListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            doPopup.dismiss();
            doPopupForattent.dismiss();
            switch (v.getId()){
                case R.id.tv_do_ismy:
                    doPopup.show(v);
                    setActionColor(2);
                    break;
                case R.id.tv_do_attent:
                    doPopupForattent.show(v);
                    setActionColor(0);
                    break;

            }

        }

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
           setBackgroundColor(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<Frament_attention_reficive> list;
        FragmentManager fm;

        public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Frament_attention_reficive> list) {
            super(fm);
            this.list = list;
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }


    }
    public void resh(){
            viewPager.setCurrentItem(0);
            fragmentList.get(0).resh();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
