package com.maxiaobu.healthclub.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.maxiaobu.healthclub.BaseFrg;
import com.maxiaobu.healthclub.R;
import com.maxiaobu.healthclub.ui.activity.MyBespeakActivity;
import com.maxiaobu.healthclub.ui.activity.OrderListActivity;
import com.maxiaobu.healthclub.ui.weiget.observablescrollview.ObservableScrollView;
import com.maxiaobu.healthclub.ui.weiget.observablescrollview.ScrollViewListener;
import com.maxiaobu.healthclub.utils.storage.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * maxiaobu 2016-9-5
 */
public class MineFragment extends BaseFrg implements View.OnClickListener, ScrollViewListener {
    @Bind(R.id.iv_header)
    ImageView mIvHeader;
    @Bind(R.id.tv_header_edit)
    TextView mTvHeaderEdit;
    @Bind(R.id.tv_cart_num)
    TextView mTvCartNum;
    @Bind(R.id.ly_cart)
    LinearLayout mLyCart;
    @Bind(R.id.tv_order_num)
    TextView mTvOrderNum;
    @Bind(R.id.ly_order)
    LinearLayout mLyOrder;
    @Bind(R.id.tv_appointment_num)
    TextView mTvAppointmentNum;
    @Bind(R.id.ly_appointment)
    LinearLayout mLyAppointment;
    @Bind(R.id.tv_balance_num)
    TextView mTvBalanceNum;
    @Bind(R.id.ly_balance)
    LinearLayout mLyBalance;
    @Bind(R.id.ly_my_friend)
    LinearLayout mLyMyFriend;
    @Bind(R.id.ly_persional_info)
    LinearLayout mLyPersionalInfo;
    @Bind(R.id.ly_my_cart)
    LinearLayout mLyMyCart;
    @Bind(R.id.ly_qiandao)
    LinearLayout mLyQiandao;
    @Bind(R.id.ly_authentication)
    LinearLayout mLyAuthentication;
    @Bind(R.id.ly_blacklist)
    LinearLayout mLyBlacklist;
    @Bind(R.id.ly_set_top)
    LinearLayout mLySetTop;
    @Bind(R.id.ly_login_out)
    LinearLayout mLyLoginOut;
    @Bind(R.id.scroll_view)
    ObservableScrollView mScrollView;
    @Bind(R.id.tv_title_common)
    TextView mTvTitleCommon;
    @Bind(R.id.toolbar_common)
    Toolbar mToolbarCommon;
    @Bind(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;


    private View mRootView;

    public MineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void initView() {
        mScrollView.setScrollViewListener(this);


    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.ly_order,R.id.ly_login_out,R.id.ly_appointment})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_order:
                startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.ly_login_out:
                SPUtils.clearAllData(getActivity());
                Toast.makeText(getActivity(), "njhjj", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ly_appointment:
                startActivity(new Intent(getActivity(), MyBespeakActivity.class));
                break;
            default:
                break;
        }

    }

    public static Fragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        float a = y;
        float b = a / 300;
        float max = (float) Math.max(0, 1 - b);
        Log.d("MineFragment", String.valueOf(b));
        mAppBarLayout.setAlpha(max);
    }
}