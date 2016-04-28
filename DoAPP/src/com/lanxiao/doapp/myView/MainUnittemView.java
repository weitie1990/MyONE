package com.lanxiao.doapp.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doapp.R;
import com.zhy.autolayout.AutoLinearLayout;


public class MainUnittemView extends AutoLinearLayout {

    private TextView unreadMsgView,middleView;

    public MainUnittemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainUnittemView(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainUnittemView);
        String name = ta.getString(R.styleable.MainUnittemView_unitItemName2);
        // Drawable image = ta.getDrawable(R.styleable.ContactItemView_contactItemImage);
        ta.recycle();

        LayoutInflater.from(context).inflate(R.layout.unit_widget_main_item, this);
        unreadMsgView = (TextView) findViewById(R.id.tv_weight_left);
        unreadMsgView.setText(name);
        setBackgroundResource(R.drawable.main_linklayout_selector);
    }
    public  void settext(String name){
        unreadMsgView.setText(name);
    }

    
}
