package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.ui.activity.CateringActivity;
import com.maxiaobu.healthclub.ui.activity.ClubListActivity;
import com.maxiaobu.healthclub.ui.activity.CoachesListActivity;
import com.maxiaobu.healthclub.ui.weiget.RectImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * maxiaobu 2016.9.5
 */
public class HomeFragment extends BaseFrg implements View.OnClickListener {
    @Bind(R.id.searchId)
    RelativeLayout mSearchId;
    @Bind(R.id.qrCodeId)
    LinearLayout mQrCodeId;
    @Bind(R.id.home_title_tools)
    LinearLayout mHomeTitleTools;
    @Bind(R.id.t1)
    TextView mT1;
    @Bind(R.id.t2)
    TextView mT2;
    @Bind(R.id.t3)
    TextView mT3;
    @Bind(R.id.ttId)
    LinearLayout mTtId;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.rl_top)
    RelativeLayout mRlTop;
    @Bind(R.id.img_more1)
    ImageView mImgMore1;
    @Bind(R.id.iv_course)
    RectImageView mIvCourse;
    @Bind(R.id.iv_club)
    RectImageView mIvClub;
    @Bind(R.id.smallId3)
    RectImageView mSmallId3;
    @Bind(R.id.iv_catering)
    RectImageView mIvCatering;
    @Bind(R.id.home_iv1)
    RectImageView mHomeIv1;
    @Bind(R.id.home_iv2)
    RectImageView mHomeIv2;
    @Bind(R.id.home_iv3)
    RectImageView mHomeIv3;
    @Bind(R.id.home_iv4)
    RectImageView mHomeIv4;
    @Bind(R.id.home_iv5)
    RectImageView mHomeIv5;
    @Bind(R.id.ll_content)
    LinearLayout mLlContent;
    @Bind(R.id.fl_bottom)
    FrameLayout mFlBottom;


    private View mRootView;

    public HomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void initView() {
        initTopView();
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_course, R.id.iv_catering,R.id.iv_club})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_course:
                startActivity(new Intent(getActivity(), CoachesListActivity.class));
                break;
            case R.id.iv_catering:
                startActivity(new Intent(getActivity(), CateringActivity.class));
                break;
            case R.id.iv_club:
                startActivity(new Intent(getActivity(), ClubListActivity.class));
                break;

            default:
                break;
        }

    }

    public static Fragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initTopView() {

    }


}
