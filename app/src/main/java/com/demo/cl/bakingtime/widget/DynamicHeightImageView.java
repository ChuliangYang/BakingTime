package com.demo.cl.bakingtime.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * Created by CL on 9/14/17.
 */

public class DynamicHeightImageView extends AppCompatImageView {
    private float widthHeightRatio=1.5f;

    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public DynamicHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth/widthHeightRatio));
    }

    public void setWightHeightRatio(float widthHeightRatio) {
        this.widthHeightRatio = widthHeightRatio;
    }

    public float getWightHeightRatio() {
        return widthHeightRatio;
    }
}
