package com.lanxiao.doapp.myView;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.doapp.R;
import com.lanxiao.doapp.MultiImageSelector.MultiUtils.ScreenUtils;
import com.lanxiao.doapp.adapter.PopupListAdapter;
import com.lanxiao.doapp.chatui.applib.chatuimain.utils.DemoApplication;

import java.util.List;

/**
 * 列表项的长按弹出菜单
 */
public class PopupList implements View.OnTouchListener {

    private static final int itemViewWidth = 50;//dp
    private static final int itemViewHeight = 40;//dp
    private static final int dividerWidth = 1;//dp
    private static final int arrowWidth = 15;//dp
    private static final int arrowHeight = 7;//dp
    private static final int windowCornersRadius = 8;//dp

    private volatile static PopupList instance;

    private PopupWindow popupListWindow;
    PopupListAdapter.OnPopupListClickListener onPopupListClickListener;
    float rawX;
    float rawY;



    public PopupList() {

    }

    public PopupListAdapter.OnPopupListClickListener getOnPopupListClickListener() {
        return onPopupListClickListener;
    }

    public void setOnPopupListClickListener(PopupListAdapter.OnPopupListClickListener onPopupListClickListener) {
        this.onPopupListClickListener = onPopupListClickListener;
    }

    /**
     * 在手指按下的位置显示弹出菜单
     *
     * @param parent    上下文view，同时也是用来寻找窗口token的view，一般为ListView中的列表项ItemView
     * @param position  点击的列表项在列表数据集中的位置
     * @param popupList 要显示的列表
     * @param rawX      手指按下的屏幕绝对坐标X
     * @param rawY      手指按下的屏幕绝对坐标Y
     */
    public void showPopupWindow(final Context mContext, View parent, int position, List popupList, float rawX, float rawY,int type) {
        //预防性Bug修复，详见http://blog.csdn.net/shangmingchao/article/details/50947418
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        // popupWindow要显示的内容
        ViewGroup layoutView = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.popup_list, null);
        RecyclerView recyclerView = (RecyclerView) layoutView.findViewById(R.id.rv_popup);
        //如果item view内容的改变不会影响RecyclerView大小，设置成true以提升表现
        //contentView.setHasFixedSize(true);
        //设置布局管理器，LinearLayoutManager，水平线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        PopupListAdapter popupListAdapter = new PopupListAdapter(this, popupList);
        //设置item点击监听
        popupListAdapter.setContextView(parent);
        popupListAdapter.setTye(type);
        popupListAdapter.setContextPosition(position);
        popupListAdapter.setOnPopupListClickListener(this.onPopupListClickListener);
        recyclerView.setAdapter(popupListAdapter);
        //设置列表分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL));
        //计算出弹出窗口的宽高
        int PopupWindowWidth = recyclerView.getAdapter().getItemCount() * ScreenUtils.dp2px(itemViewWidth) +
                (recyclerView.getAdapter().getItemCount() - 1) * ScreenUtils.dp2px(dividerWidth);
        int PopupWindowHeight = ScreenUtils.dp2px(itemViewHeight + arrowHeight);
        //为水平列表添加指示箭头，默认在列表的左下角，根据手指按下位置绝对坐标进行位置调整
        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.drawable.popup_arrow);
        float leftEdgeOffset = rawX;
        float rightEdgeOffset = ScreenUtils.getScreenWidth(mContext) - rawX;
        if (leftEdgeOffset < PopupWindowWidth / 2) {
            if (leftEdgeOffset < ScreenUtils.dp2px(arrowWidth / 2.0f)) {
                iv.setTranslationX(ScreenUtils.dp2px(windowCornersRadius));
            } else {
                iv.setTranslationX(leftEdgeOffset - ScreenUtils.dp2px(arrowWidth / 2.0f));
            }
        } else if (rightEdgeOffset < PopupWindowWidth / 2) {
            if (rightEdgeOffset < ScreenUtils.dp2px(arrowWidth / 2.0f)) {
                iv.setTranslationX(PopupWindowWidth - rightEdgeOffset - ScreenUtils.dp2px(arrowWidth / 2.0f)-ScreenUtils.dp2px(windowCornersRadius));
            } else {
                iv.setTranslationX(PopupWindowWidth - rightEdgeOffset - ScreenUtils.dp2px(arrowWidth / 2.0f));
            }
        } else {
            iv.setTranslationX(PopupWindowWidth / 2 - ScreenUtils.dp2px(arrowWidth / 2.0f));
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ScreenUtils.dp2px(arrowHeight));
        layoutView.addView(iv, layoutParams);
        //实例化弹出窗口并显示
        popupListWindow = new PopupWindow(layoutView, PopupWindowWidth,
                PopupWindowHeight, true);
        popupListWindow.setTouchable(true);
        //设置背景以便在外面包裹一层可监听触屏等事件的容器
        popupListWindow.setBackgroundDrawable(new BitmapDrawable());
        //确保可以显示后，进行最终的显示
        popupListWindow.showAtLocation(parent, Gravity.CENTER,
                (int) rawX - ScreenUtils.getScreenWidth(mContext) / 2,
                (int) rawY - ScreenUtils.getScreenHeight(mContext) / 2 - PopupWindowHeight / 2);
    }

    /**
     * 隐藏气泡式弹出菜单PopupWindow
     */
    public void hiddenPopupWindow() {
        //预防性Bug修复，详见http://blog.csdn.net/shangmingchao/article/details/50947418
       /* if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }*/
        if (popupListWindow != null) {
            popupListWindow.dismiss();
        }
    }

    public PopupWindow getPopupListWindow() {
        return popupListWindow;
    }


    /**
     * 给view绑定长按弹出(在手指按下的位置)横向气泡式菜单
     *
     * @param context                  上下文，一般为Activity
     * @param popupMenuItemList        要弹出的菜单项列表
     * @param popupListOnClickListener 弹出菜单的点击监听接口
     */
    public void initViewPopupList(Context context,final int contextPosition, final View view, final List<String> popupMenuItemList,int type, PopupListAdapter.OnPopupListClickListener popupListOnClickListener) {
        if (view == null || popupMenuItemList == null || popupMenuItemList.isEmpty()) {
            return;
        }
        view.setOnTouchListener(this);
        view.setOnClickListener(new MyOnLongClickListener(context, contextPosition, popupMenuItemList, type));

        setOnPopupListClickListener(popupListOnClickListener);
    }
    public class MyOnLongClickListener implements View.OnClickListener {
        int p,type;
        Context context;
        List<String> popupMenuItemList;
        public MyOnLongClickListener(final Context context,int p,final List<String> popupMenuItemList,int type){
            this.context=context;
            this.p=p;
            this.popupMenuItemList=popupMenuItemList;
            this.type=type;

        }
        @Override
        public void onClick(View v) {
            if(type==3){
                SharedPreferences sp = DemoApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
                int voiceMode = sp.getInt("voicemode", 1);
                popupMenuItemList.remove(0);
                if (voiceMode == 1) {
                    popupMenuItemList.add(0,"听筒");
                }else {
                    popupMenuItemList.add(0,"扬声");
                }
            }
            showPopupWindow(context, v, p, popupMenuItemList, rawX, rawY,type);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        rawX = event.getRawX();
        rawY = event.getRawY();
        return false;
    }

}
