package com.lanxiao.doapp.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.doapp.R;
import com.zhy.autolayout.AutoLinearLayout;


public class MainSetItemView extends AutoLinearLayout {

    private TextView unreadMsgView,middleView;

    public MainSetItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainSetItemView(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainSetItemView);
        String name = ta.getString(R.styleable.MainSetItemView_unitItemName);
        String visity = ta.getString(R.styleable.MainSetItemView_unitvisity);
        // Drawable image = ta.getDrawable(R.styleable.ContactItemView_contactItemImage);
        ta.recycle();
        
        LayoutInflater.from(context).inflate(R.layout.doapp_widget_main_item, this);
        unreadMsgView = (TextView) findViewById(R.id.tv_weight_left);
        middleView= (TextView) findViewById(R.id.tv_weight_middle);
        ImageView tv_weight_right= (ImageView) findViewById(R.id.tv_weight_right);
        unreadMsgView.setText(name);
        if(visity!=null){
            tv_weight_right.setVisibility(GONE);
        }
        setBackgroundResource(R.drawable.main_linklayout_selector);
    }
    
    public void setUnreadCount(int unreadCount){
        unreadMsgView.setText(String.valueOf(unreadCount));
    }
    public void setMiddleText(String text){
        middleView.setText(text);
    }
    public String getMiddleText(){
        return middleView.getText().toString();
    }
    public void showUnreadMsgView(){
        unreadMsgView.setVisibility(View.VISIBLE);
    }
    public void hideUnreadMsgView(){
        unreadMsgView.setVisibility(View.INVISIBLE);
    }
    
}
