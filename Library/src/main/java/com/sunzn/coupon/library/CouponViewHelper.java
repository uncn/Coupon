package com.sunzn.coupon.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunzn on 2017/12/14.
 */

public class CouponViewHelper {

    private static final int DEFAULT_OUTER_HOLDER_RADIUS = 15;
    private static final int DEFAULT_INNER_HOLDER_RADIUS = 15;
    private static final int DEFAULT_OUTER_CIRCLE_RADIUS = 15;
    private static final int DEFAULT_INNER_CIRCLE_RADIUS = 15;
    private static final int DEFAULT_INNER_MARGIN_INTER = 10;
    private static final int DEFAULT_INNER_DASHED_INTER = 5;
    private static final int DEFAULT_OUTER_HOLDER_COLOR = 0XFFFF0000;
    private static final int DEFAULT_OUTER_CIRCLE_COLOR = 0XFF0000FF;
    private static final int DEFAULT_INNER_DASHED_COLOR = 0XFFFFFF00;
    private static final float DEFAULT_INNER_DIVIDE_RATES = 0.3F;

    private Context context;

    private CouponView view;

    // CouponView 宽度
    private int viewWidth;

    // CouponView 高度
    private int viewHeight;

    // 外层圆角矩形画笔
    private Paint outerHolderPaint;

    // 内层层圆角矩形画笔
    private Paint innerRoundPaint;

    // 外层半圆缺口画笔
    private Paint outerCirclePaint;

    // 内层虚线画笔
    private Paint innerDashedPaint;

    // 垂直分割线比率
    private float mInnerDivideRates = DEFAULT_INNER_DIVIDE_RATES;

    // 外层圆角矩形圆角半径
    private float mOuterHolderRadius = DEFAULT_OUTER_HOLDER_RADIUS;

    // 外层圆角矩形填充色
    private int mOuterHolderColor;

    // 内层圆角矩形圆角半径
    private float mInnerHolderRadius = DEFAULT_INNER_HOLDER_RADIUS;

    // 外层半圆缺口半径
    private float mOuterCircleRadius = DEFAULT_OUTER_CIRCLE_RADIUS;

    // 外层半圆缺口颜色
    private int mOuterCircleColor = DEFAULT_OUTER_CIRCLE_COLOR;

    // 内层虚线颜色
    private int mInnerDashedColor = DEFAULT_INNER_DASHED_COLOR;

    // 分割线的 X 坐标值
    private int dividerAxisX;

    // 内层虚线距离外层的距离
    private float mInnerMarginInter = DEFAULT_INNER_MARGIN_INTER;

    // 内层虚线的实线长度
    private float mInnerDashedSolid;

    // 内层虚线的虚线长度
    private float mInnerDashedBlank;

    // 内层虚线路径
    private Path innerPath;


    public CouponViewHelper(CouponView view, Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;
        this.view = view;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CouponView, defStyleAttr, 0);
        mOuterHolderRadius = a.getDimensionPixelSize(R.styleable.CouponView_cv_outer_holder_radius, dp2Px(DEFAULT_OUTER_HOLDER_RADIUS));
        mOuterCircleRadius = a.getDimensionPixelSize(R.styleable.CouponView_cv_outer_circle_radius, dp2Px(DEFAULT_OUTER_CIRCLE_RADIUS));
        mInnerHolderRadius = a.getDimensionPixelSize(R.styleable.CouponView_cv_inner_holder_radius, dp2Px(DEFAULT_INNER_CIRCLE_RADIUS));
        mInnerDivideRates = a.getFloat(R.styleable.CouponView_cv_inner_divide_rates, DEFAULT_INNER_DIVIDE_RATES);
        mInnerMarginInter = a.getFloat(R.styleable.CouponView_cv_inner_margin_inter, DEFAULT_INNER_MARGIN_INTER);
        mInnerDashedSolid = a.getFloat(R.styleable.CouponView_cv_inner_dashed_solid, DEFAULT_INNER_DASHED_INTER);
        mInnerDashedBlank = a.getFloat(R.styleable.CouponView_cv_inner_dashed_blank, DEFAULT_INNER_DASHED_INTER);
        mOuterHolderColor = a.getColor(R.styleable.CouponView_cv_outer_holder_color, DEFAULT_OUTER_HOLDER_COLOR);
        mOuterCircleColor = a.getColor(R.styleable.CouponView_cv_outer_circle_color, DEFAULT_OUTER_CIRCLE_COLOR);
        mInnerDashedColor = a.getColor(R.styleable.CouponView_cv_inner_dashed_color, DEFAULT_INNER_DASHED_COLOR);
        a.recycle();
        init();
    }

    private void init() {
        outerHolderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerHolderPaint.setDither(true);
        outerHolderPaint.setColor(mOuterHolderColor);
        outerHolderPaint.setStyle(Paint.Style.FILL);

        outerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerCirclePaint.setDither(true);
        outerCirclePaint.setColor(mOuterCircleColor);
        outerCirclePaint.setStyle(Paint.Style.FILL);

        innerRoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerRoundPaint.setStyle(Paint.Style.STROKE);
        innerRoundPaint.setStrokeWidth(1);
        innerRoundPaint.setDither(true);
        innerRoundPaint.setColor(mInnerDashedColor);
        innerRoundPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));

        innerDashedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerDashedPaint.setStyle(Paint.Style.STROKE);
        innerDashedPaint.setStrokeWidth(1);
        innerDashedPaint.setDither(true);
        innerDashedPaint.setColor(mInnerDashedColor);
        innerDashedPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));

        innerPath = new Path();
    }

    public void onSizeChanged(int w, int h) {
        viewWidth = w;
        viewHeight = h;
        calculate();
        view.invalidate();
    }

    private void calculate() {
        dividerAxisX = (int) (viewWidth * mInnerDivideRates);
        reSizeChildLayoutParams();
    }

    private void reSizeChildLayoutParams() {
        if (view.getChildCount() == 2) {
            View childL = view.getChildAt(0);
            View childR = view.getChildAt(1);
            ViewGroup.LayoutParams paramsL = childL.getLayoutParams();
            paramsL.width = dividerAxisX;
            paramsL.height = viewHeight;
            childL.setLayoutParams(paramsL);

            ViewGroup.LayoutParams paramsR = childR.getLayoutParams();
            paramsR.width = viewWidth - dividerAxisX;
            paramsR.height = viewHeight;
            childR.setLayoutParams(paramsR);
        } else {
            throw new RuntimeException("CouponView can only have two children");
        }
    }

    @SuppressLint("DrawAllocation")
    public void onDraw(Canvas canvas) {

        RectF rectOuter = new RectF(0, 0, viewWidth, viewHeight);
        canvas.drawRoundRect(rectOuter, mOuterHolderRadius, mOuterHolderRadius, outerHolderPaint);

        canvas.drawCircle(dividerAxisX, 0, mOuterCircleRadius, outerCirclePaint);
        canvas.drawCircle(dividerAxisX, viewHeight, mOuterCircleRadius, outerCirclePaint);

        canvas.drawCircle(dividerAxisX, 0, mOuterCircleRadius, outerCirclePaint);
        canvas.drawCircle(dividerAxisX, viewHeight, mOuterCircleRadius, outerCirclePaint);

        RectF rectArcBC = new RectF(getBtmInnerCircleLeft(), getBtmInnerCircleTop(), getBtmInnerCircleRight(), getBtmInnerCircleBottom());
        canvas.drawArc(rectArcBC, getBtmInnerCircleStartAngle(), getBtmInnerSweepAngle(), false, innerRoundPaint);

        RectF rectArcTC = new RectF(getTopInnerCircleLeft(), getTopInnerCircleTop(), getTopInnerCircleRight(), getTopInnerCircleBottom());
        canvas.drawArc(rectArcTC, getTopInnerCircleStartAngle(), getTopInnerSweepAngle(), false, innerRoundPaint);

        RectF rectArcTL = new RectF(getInnerCircleTlLeft(), getInnerCircleTlTop(), getInnerCircleTlRight(), getInnerCircleTlBottom());
        canvas.drawArc(rectArcTL, getInnerCircleTlStartAngle(), getInnerTlSweepAngle(), false, innerRoundPaint);

        RectF rectArcTR = new RectF(getInnerCircleTrLeft(), getInnerCircleTrTop(), getInnerCircleTrRight(), getInnerCircleTrBottom());
        canvas.drawArc(rectArcTR, getInnerCircleTrStartAngle(), getInnerTrSweepAngle(), false, innerRoundPaint);

        RectF rectArcBL = new RectF(getInnerCircleBlLeft(), getInnerCircleBlTop(), getInnerCircleBlRight(), getInnerCircleBlBottom());
        canvas.drawArc(rectArcBL, getInnerCircleBlStartAngle(), getInnerBlSweepAngle(), false, innerRoundPaint);

        RectF rectArcBR = new RectF(getInnerCircleBrLeft(), getInnerCircleBrTop(), getInnerCircleBrRight(), getInnerCircleBrBottom());
        canvas.drawArc(rectArcBR, getInnerCircleBrStartAngle(), getInnerBrSweepAngle(), false, innerRoundPaint);

        innerPath.reset();
        innerPath.moveTo(getInnerCircleTlLeft(), getInnerRectTlCenterY());
        innerPath.lineTo(getInnerCircleBlLeft(), getInnerRectBlCenterY());
        innerPath.moveTo(getInnerCircleTrRight(), getInnerRectTrCenterY());
        innerPath.lineTo(getInnerCircleBrRight(), getInnerRectBrCenterY());
        innerPath.moveTo(getInnerRectTlCenterX(), getInnerCircleTlTop());
        innerPath.lineTo(getInnerConnectLeftX(), getInnerCircleTlTop());
        innerPath.moveTo(getInnerRectBlCenterX(), getInnerCircleBlBottom());
        innerPath.lineTo(getInnerConnectLeftX(), getInnerCircleBlBottom());
        innerPath.moveTo(getInnerRectTrCenterX(), getInnerCircleTrTop());
        innerPath.lineTo(getInnerConnectRightX(), getInnerCircleTrTop());
        innerPath.moveTo(getInnerRectBrCenterX(), getInnerCircleBrBottom());
        innerPath.lineTo(getInnerConnectRightX(), getInnerCircleBrBottom());
        innerPath.moveTo(getInnerDashedCircleX(), getInnerDashedCircleTY());
        innerPath.lineTo(getInnerDashedCircleX(), getInnerDashedCircleBY());
        canvas.drawPath(innerPath, innerDashedPaint);
    }

    // =============================================================================================

    private float getInnerDashedCircleX() {
        return dividerAxisX;
    }

    private float getInnerDashedCircleTY() {
        return mOuterCircleRadius + mInnerMarginInter;
    }

    private float getInnerDashedCircleBY() {
        return viewHeight - mOuterCircleRadius - mInnerMarginInter;
    }

    // =============================================================================================

    private float getInnerConnectLeftX() {
        double x = Math.sqrt(Math.pow(mOuterCircleRadius + mInnerMarginInter, 2) - Math.pow(mInnerMarginInter, 2));
        return (float) (dividerAxisX - x);
    }

    private float getInnerConnectRightX() {
        double x = Math.sqrt(Math.pow(mOuterCircleRadius + mInnerMarginInter, 2) - Math.pow(mInnerMarginInter, 2));
        return (float) (dividerAxisX + x);
    }

    // =============================================================================================

    private float getInnerRectTlCenterX() {
        return mInnerMarginInter + mInnerHolderRadius;
    }

    private float getInnerRectTlCenterY() {
        return mInnerMarginInter + mInnerHolderRadius;
    }

    private float getInnerRectTrCenterX() {
        return viewWidth - mInnerHolderRadius - mInnerMarginInter;
    }

    private float getInnerRectTrCenterY() {
        return mInnerMarginInter + mInnerHolderRadius;
    }

    private float getInnerRectBlCenterX() {
        return mInnerMarginInter + mInnerHolderRadius;
    }

    private float getInnerRectBlCenterY() {
        return viewHeight - mInnerMarginInter - mInnerHolderRadius;
    }

    private float getInnerRectBrCenterX() {
        return viewWidth - mInnerHolderRadius - mInnerMarginInter;
    }

    private float getInnerRectBrCenterY() {
        return viewHeight - mInnerMarginInter - mInnerHolderRadius;
    }

    // =============================================================================================

    private float getInnerCircleBrLeft() {
        return viewWidth - mInnerHolderRadius * 2 - mInnerMarginInter;
    }

    private float getInnerCircleBrTop() {
        return viewHeight - mInnerHolderRadius * 2 - mInnerMarginInter;
    }

    private float getInnerCircleBrRight() {
        return viewWidth - mInnerMarginInter;
    }

    private float getInnerCircleBrBottom() {
        return viewHeight - mInnerMarginInter;
    }

    private float getInnerCircleBrStartAngle() {
        return 0;
    }

    private float getInnerBrSweepAngle() {
        return 90;
    }

    // =============================================================================================

    private float getInnerCircleBlLeft() {
        return mInnerMarginInter;
    }

    private float getInnerCircleBlTop() {
        return viewHeight - mInnerHolderRadius * 2 - mInnerMarginInter;
    }

    private float getInnerCircleBlRight() {
        return mInnerMarginInter + mInnerHolderRadius * 2;
    }

    private float getInnerCircleBlBottom() {
        return viewHeight - mInnerMarginInter;
    }

    private float getInnerCircleBlStartAngle() {
        return 90;
    }

    private float getInnerBlSweepAngle() {
        return 90;
    }

    // =============================================================================================

    private float getInnerCircleTrLeft() {
        return viewWidth - mInnerHolderRadius * 2 - mInnerMarginInter;
    }

    private float getInnerCircleTrTop() {
        return mInnerMarginInter;
    }

    private float getInnerCircleTrRight() {
        return viewWidth - mInnerMarginInter;
    }

    private float getInnerCircleTrBottom() {
        return mInnerMarginInter + mInnerHolderRadius * 2;
    }

    private float getInnerCircleTrStartAngle() {
        return -90;
    }

    private float getInnerTrSweepAngle() {
        return 90;
    }

    // =============================================================================================

    private float getInnerCircleTlLeft() {
        return mInnerMarginInter;
    }

    private float getInnerCircleTlTop() {
        return mInnerMarginInter;
    }

    private float getInnerCircleTlRight() {
        return mInnerMarginInter + mInnerHolderRadius * 2;
    }

    private float getInnerCircleTlBottom() {
        return mInnerMarginInter + mInnerHolderRadius * 2;
    }

    private float getInnerCircleTlStartAngle() {
        return -180;
    }

    private float getInnerTlSweepAngle() {
        return 90;
    }

    // =============================================================================================

    private float getBtmInnerCircleLeft() {
        return dividerAxisX - mOuterCircleRadius - mInnerMarginInter;
    }

    private float getBtmInnerCircleTop() {
        return viewHeight - mOuterCircleRadius - mInnerMarginInter;
    }

    private float getBtmInnerCircleRight() {
        return dividerAxisX + mOuterCircleRadius + mInnerMarginInter;
    }

    private float getBtmInnerCircleBottom() {
        return viewHeight + mOuterCircleRadius + mInnerMarginInter;
    }

    private int getBtmInnerCircleStartAngle() {
        double cos = mInnerMarginInter / (mOuterCircleRadius + mInnerMarginInter);
        return (int) (90 + Math.acos(cos) * 180 / Math.PI) * -1;
    }

    private int getBtmInnerSweepAngle() {
        double cos = mInnerMarginInter / (mOuterCircleRadius + mInnerMarginInter);
        return (int) (Math.acos(cos) * 180 / Math.PI) * 2;
    }

    // =============================================================================================

    private float getTopInnerCircleLeft() {
        return dividerAxisX - mOuterCircleRadius - mInnerMarginInter;
    }

    private float getTopInnerCircleTop() {
        return -1 * (mOuterCircleRadius + mInnerMarginInter);
    }

    private float getTopInnerCircleRight() {
        return dividerAxisX + mOuterCircleRadius + mInnerMarginInter;
    }

    private float getTopInnerCircleBottom() {
        return mOuterCircleRadius + mInnerMarginInter;
    }

    private int getTopInnerCircleStartAngle() {
        double cos = mInnerMarginInter / (mOuterCircleRadius + mInnerMarginInter);
        return (int) (90 - Math.acos(cos) * 180 / Math.PI);
    }

    private int getTopInnerSweepAngle() {
        double cos = mInnerMarginInter / (mOuterCircleRadius + mInnerMarginInter);
        return (int) (Math.acos(cos) * 180 / Math.PI) * 2;
    }

    // =============================================================================================

    private int dp2Px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    private int px2Dp(float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    // =============================================================================================

    public void setOuterHolderColor(int color) {
        if (this.mOuterHolderColor != color) {
            this.mOuterHolderColor = color;
            outerHolderPaint.setColor(mOuterHolderColor);
            calculate();
            this.view.invalidate();
        }
    }

    public void setInnerDashedColor(int color) {
        if (this.mInnerDashedColor != color) {
            this.mInnerDashedColor = color;
            innerRoundPaint.setColor(mInnerDashedColor);
            innerDashedPaint.setColor(mInnerDashedColor);
            calculate();
            this.view.invalidate();
        }
    }

    public void setInnerDivideRates(float rate) {
        if (this.mInnerDivideRates != rate) {
            this.mInnerDivideRates = rate;
            calculate();
            this.view.invalidate();
        }
    }

    public void setInnerDashedSolid(float solid) {
        if (this.mInnerDashedSolid != solid) {
            this.mInnerDashedSolid = solid;
            innerRoundPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));
            innerDashedPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));
            calculate();
            this.view.invalidate();
        }
    }

    public void setInnerDashedBlank(float blank) {
        if (this.mInnerDashedBlank != blank) {
            this.mInnerDashedBlank = blank;
            innerRoundPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));
            innerDashedPaint.setPathEffect(new DashPathEffect(new float[]{mInnerDashedSolid, mInnerDashedBlank}, 0));
            calculate();
            this.view.invalidate();
        }
    }

    public void setOuterCircleColor(int color) {
        if (this.mOuterCircleColor != color) {
            this.mOuterCircleColor = color;
            outerCirclePaint.setColor(mOuterCircleColor);
            calculate();
            this.view.invalidate();
        }
    }
}
