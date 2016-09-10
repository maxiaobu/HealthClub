package com.maxiaobu.healthclub.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseAty;
import com.maxiaobu.healthclub.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CoachcertApplyActivity extends BaseAty {

    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.ed_name)
    AppCompatEditText mEdName;
    @Bind(R.id.ed_phone_num)
    AppCompatEditText mEdPhoneNum;
    @Bind(R.id.tv_introduction)
    TextView mTvIntroduction;
    @Bind(R.id.ed_describe)
    AppCompatEditText mEdDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachcert_apply);
        ButterKnife.bind(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
