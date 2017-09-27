package com.demo.cl.bakingtime.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by CL on 9/14/17.
 */

public class DynamicHeightTextView extends AppCompatTextView {
    private float widthHeightRatio=1.5f;

    public DynamicHeightTextView(Context context) {
        super(context);
    }

    public DynamicHeightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth/widthHeightRatio));
    }
}
