package com.lanxiao.doapp.capricorn;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.doapp.R;
import com.lidroid.xutils.util.LogUtils;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.utils.AutoLayoutHelper;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by Thinkpad on 2016/3/12.
 */

public class AutoArcLayout extends ArcLayout{
    private int mChildSize;

    private int mChildPadding = 5;

    private int mLayoutPadding = 10;

    public static final float DEFAULT_FROM_DEGREES = 270.0f;

    public static final float DEFAULT_TO_DEGREES = 360.0f;

    private float mFromDegrees = DEFAULT_FROM_DEGREES;

    private float mToDegrees = DEFAULT_TO_DEGREES;

    private static final int MIN_RADIUS = 100;//调节弹出的距离

    /* the distance between the layout's center and any child's center */
    private int mRadius;

    private boolean mExpanded = false;
    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);
    public AutoArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArcLayout, 0, 0);
            mFromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees, DEFAULT_FROM_DEGREES);
            mToDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees, DEFAULT_TO_DEGREES);
            mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, 0), 0);

            a.recycle();
        }
    }

    public AutoArcLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            int autoChildSize= AutoUtils.getPercentHeightSize(mChildSize);
            LogUtils.i(autoChildSize+"");
            setChildSize(autoChildSize);
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {

        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends ArcLayout.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams
    {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo()
        {
            return mAutoLayoutInfo;
        }


        public LayoutParams(int width, int height)
        {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

    }
    public static int computeRadius(final float arcDegrees, final int childCount, final int childSize,
                                    final int childPadding, final int minRadius) {
        if (childCount < 2) {
            return minRadius;
        }

        final float perDegrees = arcDegrees / (childCount - 1);
        final float perHalfDegrees = perDegrees / 2;
        final int perSize = childSize + childPadding;

        final int radius = (int) ((perSize / 2) / Math.sin(Math.toRadians(perHalfDegrees)));

        return Math.max(radius, minRadius);
    }

    public static Rect computeChildFrame(final int centerX, final int centerY, final int radius, final float degrees,
                                         final int size) {

        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));

        return new Rect((int) (childCenterX - size / 2), (int) (childCenterY - size / 2),
                (int) (childCenterX + size / 2), (int) (childCenterY + size / 2));
    }

}
