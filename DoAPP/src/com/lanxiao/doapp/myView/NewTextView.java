package com.lanxiao.doapp.myView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Thinkpad on 2016/4/19.
 */
public class NewTextView extends EditText {
    public NewTextView(Context context) {
        super(context);
    }

    public NewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean getDefaultEditable() {//禁止EditText被编辑
        return false;
    }
}
