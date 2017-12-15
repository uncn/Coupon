package com.sunzn.coupon.library;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sunzn on 2017/12/14.
 */

public class CouponView extends FrameLayout {

    private CouponViewHelper helper;

    public CouponView(@NonNull Context context) {
        this(context, null);
    }

    public CouponView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        helper = new CouponViewHelper(this, context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        helper.onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        helper.onDraw(canvas);
    }

    public void setOuterHolderColor(int color) {
        helper.setOuterHolderColor(color);
    }

    public void setOuterCircleColor(int color) {
        helper.setOuterCircleColor(color);
    }

    public void setInnerDashedColor(int color) {
        helper.setInnerDashedColor(color);
    }

    public void setInnerDivideRates(float rate) {
        helper.setInnerDivideRates(rate);
    }

    public void setInnerDashedSolid(float solid) {
        helper.setInnerDashedSolid(solid);
    }

    public void setInnerDashedBlank(float blank) {
        helper.setInnerDashedBlank(blank);
    }

}
