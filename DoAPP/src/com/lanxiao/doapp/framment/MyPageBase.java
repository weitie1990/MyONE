package com.lanxiao.doapp.framment;

import android.support.v4.app.Fragment;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by Thinkpad on 2016/2/23.
 */
public abstract class MyPageBase extends Fragment {
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;

    //setUserVisibleHint  adapter中的每个fragment切换的时候都会被调用，如果是切换到当前页，那么isVisibleToUser==true，否则为false
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.i("isVisible:"+isVisible+",isvisibletouser:"+isVisibleToUser);
        if(isVisibleToUser) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}
