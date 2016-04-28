package com.lanxiao.doapp.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.easeui.R;

/**
 * 标题栏
 *
 */
public class ApplyItemRoot extends RelativeLayout{
    public ApplyItemRoot(Context context) {
        super(context);
    }

   /* protected ImageView iv_iconforapply,ib_addapply;
    protected TextView tv_appforname,tv_appforinfo;

    public ApplyItemRoot(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public ApplyItemRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ApplyItemRoot(Context context) {
        super(context);
        init(context, null);
    }
    
    private void init(Context context, AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.rootitemapply, this);
        iv_iconforapply= (ImageView) findViewById(R.id.iv_iconforapply);
        ib_addapply= (ImageView) findViewById(R.id.ib_addapply);
        tv_appforname= (TextView) findViewById(R.id.tv_appforname);
        tv_appforinfo= (TextView) findViewById(R.id.tv_appforinfo);
        parseStyle(context, attrs);
    }
    
    private void parseStyle(Context context, AttributeSet attrs){
        if(attrs != null){
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.applayitem);
            String maintitle = ta.getString(R.styleable.maintitle);
            String smalltitle = ta.getString(R.styleable.smalltitle);
            Drawable bigimage = ta.getDrawable(R.styleable.bigimage);
            Drawable addimage = ta.getDrawable(R.styleable.addimage);

            titleView.setText(title);
            
            Drawable leftDrawable = ta.getDrawable(R.styleable.EaseTitleBar_titleBarLeftImage);
            if (null != leftDrawable) {
                leftImage.setImageDrawable(leftDrawable);
            }
            Drawable rightDrawable = ta.getDrawable(R.styleable.EaseTitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                rightImage.setImageDrawable(rightDrawable);
            }
        
            Drawable background = ta.getDrawable(R.styleable.EaseTitleBar_titleBarBackground);
            if(null != background) {
                titleLayout.setBackgroundDrawable(background);
            }
            
            ta.recycle();
        }*/
 /*   }
    
    public void setLeftImageResource(int resId) {
        leftImage.setImageResource(resId);
    }
    
    public void setRightImageResource(int resId) {
        rightImage.setImageResource(resId);
    }
    
    public void setLeftLayoutClickListener(OnClickListener listener){
        leftLayout.setOnClickListener(listener);
    }
    
    public void setRightLayoutClickListener(OnClickListener listener){
        rightLayout.setOnClickListener(listener);
    }
    
    public void setLeftLayoutVisibility(int visibility){
        leftLayout.setVisibility(visibility);
    }
    
    public void setRightLayoutVisibility(int visibility){
        rightLayout.setVisibility(visibility);
    }
    
    public void setTitle(String title){
        titleView.setText(title);
    }
    
    public void setBackgroundColor(int color){
        titleLayout.setBackgroundColor(color);
    }
    
    public RelativeLayout getLeftLayout(){
        return leftLayout;
    }
    
    public RelativeLayout getRightLayout(){
        return rightLayout;
    }*/
}
