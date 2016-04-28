package com.lanxiao.doapp.framment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.example.doapp.R;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;
import com.lanxiao.doapp.myView.DoappTitleBar;

public abstract class MainBaseFragment extends Fragment{
    protected DoappTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        titleBar = (DoappTitleBar) getView().findViewById(R.id.doapp_title_bar);
        EMChatManager.getInstance().addConnectionListener(connectionListener);
        initView();
        setUpView();
    }
    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                default:
                    break;
            }
        }
    };
    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.CONNECTION_CONFLICT) {
               // isConflict = true;
            } else {
               handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    /**
     * 显示标题栏
     */
    public void ShowTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.VISIBLE);
        }
    }
    
    /**
     * 隐藏标题栏
     */
    public void hideTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.GONE);
        }
    }
    
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 初始化控件
     */
    protected abstract void initView();
    
    /**
     * 设置属性，监听等
     */
    protected abstract void setUpView();
    /**
     * 连接到服务器
     */
    protected abstract void onConnectionConnected();


    /**
     * 连接断开
     */
    protected abstract void onConnectionDisconnected();

    @Override
    public void onDestroy() {
        super.onDestroy();
        DemoApplication.getRefWatcher().watch(this);

    }
}
