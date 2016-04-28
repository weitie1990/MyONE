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
import com.zhy.autolayout.AutoRelativeLayout;


public class UserInfoItemView extends AutoRelativeLayout {

    private TextView title,middleView;

    public UserInfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public UserInfoItemView(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.userInfoItemView);
        String name = ta.getString(R.styleable.userInfoItemView_userTitle);
        ta.recycle();
        LayoutInflater.from(context).inflate(R.layout.userinfo_main_item, this);
        title = (TextView) findViewById(R.id.tv_userinfo_left);
        middleView= (TextView) findViewById(R.id.tv_userinfo_name);
        title.setText(name);
    }
    

    public void setMiddleText(String text){
        middleView.setText(text);
    }
    public String getMiddleText(){
        return middleView.getText().toString();
    }
    
}
