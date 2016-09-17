package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoachesOnlineManageActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.et_name)
    AppCompatEditText mEtName;
    @Bind(R.id.et_times)
    AppCompatEditText mEtTimes;
    @Bind(R.id.ly_times)
    LinearLayout mLyTimes;
    @Bind(R.id.et_days)
    AppCompatEditText mEtDays;
    @Bind(R.id.ly_days)
    LinearLayout mLyDays;
    @Bind(R.id.et_price)
    AppCompatEditText mEtPrice;
    @Bind(R.id.ly_price)
    LinearLayout mLyPrice;
    @Bind(R.id.ed_content)
    AppCompatEditText mEdContent;
    @Bind(R.id.tv_online)
    TextView mTvOnline;
    @Bind(R.id.tv_delete)
    TextView mTvDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_online_manage);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {
        setCommonBackToolBar(mToolbarCommon, mTvTitleCommon, "编辑课程");

    }

    @Override
    public void initData() {

    }
}
