package com.sunzn.coupon.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.sunzn.coupon.library.CouponView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.coupon_view)
    CouponView mCouponView;

    @BindView(R.id.seekBar_holder_color)
    SeekBar mSeekBarHolderColor;

    @BindView(R.id.seekBar_circle_color)
    SeekBar mSeekBarCircleColor;

    @BindView(R.id.seekBar_dashed_color)
    SeekBar mSeekBarDashedColor;

    @BindView(R.id.seekBar_divide)
    SeekBar mSeekBarDivide;

    @BindView(R.id.seekBar_solid)
    SeekBar mSeekBarSolid;

    @BindView(R.id.seekBar_blank)
    SeekBar mSeekBarBlank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSeekBarHolderColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (0 <= i && i < 20) {
                    mCouponView.setOuterHolderColor(Color.parseColor("#FFE4B5"));
                } else if (20 <= i && i < 40) {
                    mCouponView.setOuterHolderColor(Color.parseColor("#FF8C00"));
                } else if (40 <= i && i < 60) {
                    mCouponView.setOuterHolderColor(Color.parseColor("#FF7F50"));
                } else if (60 <= i && i < 80) {
                    mCouponView.setOuterHolderColor(Color.parseColor("#CD5C5C"));
                } else {
                    mCouponView.setOuterHolderColor(Color.parseColor("#B22222"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarCircleColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (0 <= i && i < 20) {
                    mCouponView.setOuterCircleColor(Color.parseColor("#FAFAFA"));
                } else if (20 <= i && i < 40) {
                    mCouponView.setOuterCircleColor(Color.parseColor("#F5F5F5"));
                } else if (40 <= i && i < 60) {
                    mCouponView.setOuterCircleColor(Color.parseColor("#DCDCDC"));
                } else if (60 <= i && i < 80) {
                    mCouponView.setOuterCircleColor(Color.parseColor("#D3D3D3"));
                } else {
                    mCouponView.setOuterCircleColor(Color.parseColor("#C0C0C0"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarDashedColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (0 <= i && i < 20) {
                    mCouponView.setInnerDashedColor(Color.parseColor("#008B8B"));
                } else if (20 <= i && i < 40) {
                    mCouponView.setInnerDashedColor(Color.parseColor("#FF8C00"));
                } else if (40 <= i && i < 60) {
                    mCouponView.setInnerDashedColor(Color.parseColor("#FF4500"));
                } else if (60 <= i && i < 80) {
                    mCouponView.setInnerDashedColor(Color.parseColor("#B22222"));
                } else {
                    mCouponView.setInnerDashedColor(Color.parseColor("#DC143C"));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarDivide.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCouponView.setInnerDivideRates((float) i / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarSolid.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCouponView.setInnerDashedSolid((float) i / 5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarBlank.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCouponView.setInnerDashedBlank((float) i / 5);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
